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

import org.java_websocket.exceptions.WebsocketNotConnectedException
import java.net.URI
import java.util.concurrent.atomic.AtomicReference

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