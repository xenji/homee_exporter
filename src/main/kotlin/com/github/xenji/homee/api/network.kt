/**
 * Copyright 2018 Mario Mueller <mario@xenji.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.xenji.homee.api

import mu.KotlinLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.net.URI
import java.time.Duration

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

private const val BROADCAST_PORT = 15263
private const val HOMEE_LOCAL_PORT = 7681
private val logger = KotlinLogging.logger("network")

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
private fun localUrl(ip: String) = "http://$ip:$HOMEE_LOCAL_PORT"

/**
 * Generate global websocket connection URI
 */
private fun globalWss(homeeId: String) = "wss://$homeeId.hom.ee/connection"

/**
 * Generate local websocket connection URI
 */
private fun localWs(ip: String) = "ws://$ip:$HOMEE_LOCAL_PORT/connection"

/**
 * Checks the availability of a local homee instance via broadcast.
 */
private fun homeeCommType(broadcastMessage: String, address: InetAddress): HomeeCommType =
    DatagramSocket().use { socket ->
        val buffer = broadcastMessage.toByteArray()
        val packet = DatagramPacket(buffer, buffer.size, address, BROADCAST_PORT)
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
        when (responsePacket.address) {
            null -> HomeeGlobal()
            else -> HomeeLocal(responsePacket.address.hostAddress)
        }
    }
