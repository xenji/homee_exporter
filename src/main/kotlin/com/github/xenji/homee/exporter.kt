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
        val homeeConnection = webSocket(authenticatedHomeeWebsocket(accessToken, homeeWs), pingInterval, exportOnlyGroup)

        homeeConnection.connect()

        homeeConnection.groups()
        // get initially all nodes
        logger.info { "Starting metrics collection..." }
        thread(isDaemon = true) {
            while (true) {
                logger.debug { "Calling API for metrics..." }
                homeeConnection.asyncData()
                logger.trace { "Sleeping $checkInterval seconds" }
                Thread.sleep(Duration.ofSeconds(checkInterval).toMillis())
            }
        }

        logger.info { "Starting webserver at $bindHost:$bindPort" }
        startServer(bindHost, bindPort)
    }
    Unit
}