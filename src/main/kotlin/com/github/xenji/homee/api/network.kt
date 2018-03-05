package com.github.xenji.homee.api

import com.github.xenji.homee.metrics.updateMetrics
import mu.KLogging
import mu.KotlinLogging
import org.http4k.format.Moshi
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.protocols.Protocol
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.net.URI
import java.time.Duration
import kotlin.concurrent.thread

class HomeeWsClient(uri: URI) : WebSocketClient(uri, draftWithProtocol, homeeWebsocketHeaders, homeeWebsocketTimeout) {
    companion object : KLogging()

    override fun onOpen(handshakedata: ServerHandshake) {
        thread(isDaemon = true) {
            logger.debug { "Starting ping/pong thread" }
            while (true) {
                logger.trace { "sending ping" }
                sendPing()
                Thread.sleep(Duration.ofSeconds(5).toMillis())
            }
        }
    }

    /**
     * On close, we try to reconnect up to three times.
     *
     * TODO: Find out if any case exists where remote==false and we still want to reconnect!
     */
    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        logger.info { "Closed WS connection: {code: $code, reason: '$reason', remote: $remote}" }
        var maxRetry = 3
        var connectionSuccessful = false
        while (remote && maxRetry > 0) {
            connectionSuccessful = connectBlocking()
            --maxRetry
        }

        if (!connectionSuccessful) {
            throw RuntimeException("Reconnecting the websocket failed after $maxRetry attempts. Remote connection was closed with [code: $code | reason: $reason | remote: $remote]")
    }

    override fun onMessage(message: String?) {
        logger.trace { "Received $message" }
        if (message != null && message.startsWith("""{"nodes":[{""")) {
            val nr = Moshi.asA<NodesResponse>(message)
            updateMetrics(nr.nodes)
        }
    }

    override fun onError(ex: Exception) = throw ex
}

/**
 * Finds the correct homee to talk to (either via local network or via the internet)
 */
fun findHomee(homeeId: String): Pair<String, String> = findHomeeSelective(homeeId)

/**
 * Adds the access token to the url
 */
fun authenticatedHomeeWebsocket(accessToken: String, websocketUri: String) = "$websocketUri?access_token=$accessToken"

/**
 * Create the websocket connection and wait for the connection.
 *
 * We don't do this async, because without a connection this whole thing makes no sense.
 */
fun webSocket(homeeWs: String): HomeeWsClient {
    logger.trace { "Using WS URI: $homeeWs" }
    return HomeeWsClient(URI.create(homeeWs)).apply { connectBlocking() }
}

private const val broadcastPort = 15263
private const val homeeLocalPort = 7681
private val logger = KotlinLogging.logger("network")
private val draftWithProtocol = Draft_6455().apply { knownProtocols.add(Protocol("v2")) }
private val homeeWebsocketHeaders = mapOf("Sec-WebSocket-Protocol" to "v2", "Sec-WebSocket-Version" to "13")
private const val homeeWebsocketTimeout = 5_000

private sealed class HomeeCommType
private data class HomeeLocal(val ip: String) : HomeeCommType()
private class HomeeGlobal : HomeeCommType()

/**
 * Generate local http url
 */
private fun findHomeeSelective(homeeId: String): Pair<String, String> {
    val commType = homeeCommType(homeeId, InetAddress.getByName("255.255.255.255"))
    return when (commType) {
        is HomeeLocal -> localUrl(commType.ip) to localWs(commType.ip)
        is HomeeGlobal -> globalUrl(homeeId) to globalWss(homeeId)
    }
}

/**
 * Generate global http url
 */
private fun globalUrl(homeeId: String) = "https://$homeeId.hom.ee"

/**
 * Generate local http url
 */
private fun localUrl(ip: String) = "http://$ip:$homeeLocalPort"

/**
 * Generate global websocket connection URI
 */
private fun globalWss(homeeId: String) = "wss://$homeeId.hom.ee/connection"

/**
 * Generate local websocket connection URI
 */
private fun localWs(ip: String) = "ws://$ip:$homeeLocalPort/connection"

/**
 * Checks the availability of a local homee instance via broadcast.
 */
private fun homeeCommType(broadcastMessage: String, address: InetAddress): HomeeCommType =
    DatagramSocket().use { socket ->
        val buffer = broadcastMessage.toByteArray()
        val packet = DatagramPacket(buffer, buffer.size, address, broadcastPort)
        val responsePacket = DatagramPacket(ByteArray(4096), 4096)

        with(socket) {
            soTimeout = Duration.ofSeconds(1).toMillis().toInt()
            broadcast = true
            send(packet)
            try {
                receive(responsePacket)
            } catch (e: SocketTimeoutException) {
                // no-op
            }
        }
        // FIXME: Security problem: We need to check the response if it matches the desired pattern.
        when (responsePacket.address) {
            null -> HomeeGlobal()
            else -> HomeeLocal(responsePacket.address.hostAddress)
        }
    }
