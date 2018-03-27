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