package com.github.xenji.homee.api

import com.github.xenji.homee.metrics.updateMetrics
import com.github.xenji.homee.metrics.updateRelationships
import mu.KLogging
import mu.KotlinLogging
import org.http4k.format.Moshi
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.protocols.Protocol
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.net.URI
import java.time.Duration
import java.util.Timer
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread

/**
 * Implementation for the websocket connection to the homee server.
 */
private class HomeeWsClient(
    uri: URI,
    private val pingInterval: Long,
    private val onlyGroup: Int
) : WebSocketClient(uri, draftWithProtocol, homeeWebsocketHeaders, homeeWebsocketTimeout) {
    companion object : KLogging()

    @Volatile
    private lateinit var pingThread: Timer

    /**
     * On open, we start a thread that sends a ping every 5 seconds.
     *
     * This is a daemon thread that should not keep the JVM up when e.g. CTRL+C
     * is pressed.
     */
    override fun onOpen(handshakedata: ServerHandshake) {
        Runtime.getRuntime().addShutdownHook(thread(start = false, isDaemon = false) {
            this.closeConnection(-1, "JVM Shutdown")
        })

        if (::pingThread.isInitialized) {
            pingThread.cancel()
        }

        pingThread =
            fixedRateTimer(name = "ping_timer", daemon = true, period = Duration.ofSeconds(pingInterval).toMillis()) {
                sendPing()
            }
    }

    /**
     * On close, we try to reconnect up to three times.
     *
     * TODO: Find out if any case exists where remote==false and we still want to reconnect!
     */
    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        logger.info { "Closed WS connection: {code: $code, reason: '$reason', remote: $remote}" }
        logger.debug { "Shutting down ping thread" }
        if (::pingThread.isInitialized) {
            pingThread.cancel()
        }
    }

    /**
     * When any message is received, we just want those that start with `{"nodes":[{`.
     *
     * Kotlin has no real pattern matching mechanics, therefore we do the poor-mans `startsWith`.
     *
     * This is all pretty wonky and needs a more sophisticated approach later.
     */
    override fun onMessage(message: String?) {
        logger.trace { "Received $message" }
        if (message != null) {
            if (message.startsWith("""{"nodes":[{""")) {
                val nr = Moshi.asA<NodesResponse>(message)
                updateMetrics(nr.nodes, onlyGroup)
            }
            if (message.startsWith("""{"relationships":""")) {
                val relationships = Moshi.asA<Relationships>(message)
                updateRelationships(relationships.relationships)
            }
        }
    }

    /**
     * On error, we just rethrow for now.
     */
    override fun onError(ex: Exception) = throw ex
}

class HomeeConnection(
    private val uri: URI,
    private val pingInterval: Long,
    private val onlyGroup: Int
) {
    private val wsClient: AtomicReference<HomeeWsClient> = AtomicReference()

    @Synchronized
    fun connect() {
        wsClient.get()?.closeBlocking()
        wsClient.set(HomeeWsClient(uri, pingInterval, onlyGroup).apply {
            connectBlocking()
        })
    }

    fun groups() {
        sendRelationshipRequest()
    }

    fun asyncData() {
        sendNodeRequest()
    }

    private fun sendNodeRequest(currentTry: Int = 0, maxRetry: Int = 3) {
        try {
            wsClient.get()?.send("GET:nodes")
        } catch (e: WebsocketNotConnectedException) {
            when {
                currentTry < maxRetry -> {
                    connect()
                    sendNodeRequest(currentTry + 1, maxRetry)
                }
                else -> throw RuntimeException("Cannot reconnect", e)
            }
        }
    }

    private fun sendRelationshipRequest(currentTry: Int = 0, maxRetry: Int = 3) {
        try {
            wsClient.get()?.send("GET:relationships")
        } catch (e: WebsocketNotConnectedException) {
            when {
                currentTry < maxRetry -> {
                    connect()
                    sendRelationshipRequest(currentTry + 1, maxRetry)
                }
                else -> throw RuntimeException("Cannot reconnect", e)
            }
        }
    }
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
fun webSocket(homeeWs: String, pingInterval: Long, onlyGroup: Int): HomeeConnection {
    logger.trace { "Using WS URI: $homeeWs" }
    return HomeeConnection(URI.create(homeeWs), pingInterval, onlyGroup)
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
