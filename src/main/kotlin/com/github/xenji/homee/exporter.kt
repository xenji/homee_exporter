package com.github.xenji.homee

import com.github.xenji.homee.api.authenticatedHomeeWebsocket
import com.github.xenji.homee.api.findHomee
import com.github.xenji.homee.api.homeeAccessToken
import com.github.xenji.homee.api.webSocket
import com.github.xenji.homee.cli.ExporterArguments
import com.github.xenji.homee.metrics.startServer
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import mu.KotlinLogging
import org.http4k.client.OkHttp
import org.http4k.core.BodyMode
import java.time.Duration
import kotlin.concurrent.thread

val httpClient = OkHttp(bodyMode = BodyMode.Stream)
val logger = KotlinLogging.logger("Main")
fun main(args: Array<String>) = mainBody("homee_exporter") {
    ArgParser(args).parseInto(::ExporterArguments).run {
        val (homeeUrl, homeeWs) = findHomee(homeeId)
        val accessToken = homeeAccessToken(homeeUrl, username, password, httpClient)
        val wsClient = webSocket(authenticatedHomeeWebsocket(accessToken, homeeWs))

        // get initially all nodes
        logger.info { "Starting all-nodes call" }
        thread(isDaemon = true) {
            while (true) {
                wsClient.send("GET:nodes")
                Thread.sleep(Duration.ofSeconds(checkInterval).toMillis())
            }
        }
        startServer(bindHost, bindPort)

    }
    Unit
}