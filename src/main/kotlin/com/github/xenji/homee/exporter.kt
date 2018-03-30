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
package com.github.xenji.homee

import com.github.xenji.homee.api.authenticatedHomeeWebsocket
import com.github.xenji.homee.api.findHomee
import com.github.xenji.homee.api.homeeAccessToken
import com.github.xenji.homee.api.useGivenHomee
import com.github.xenji.homee.api.webSocket
import com.github.xenji.homee.cli.ExporterArguments
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.prometheus.client.exporter.HTTPServer
import mu.KotlinLogging
import org.http4k.client.OkHttp
import org.http4k.core.BodyMode
import java.time.Duration
import kotlin.concurrent.fixedRateTimer

val httpClient = OkHttp(bodyMode = BodyMode.Stream)
val logger = KotlinLogging.logger("Main")
fun main(args: Array<String>) = mainBody("homee_exporter") {
    ArgParser(args).parseInto(::ExporterArguments).run {
        val (homeeUrl, homeeWs) = when (predefinedHomeeIP) {
            "" -> findHomee(homeeId)
            else -> useGivenHomee(predefinedHomeeIP)
        }
        val accessToken = homeeAccessToken(homeeUrl, username, password, httpClient)
        val homeeConnection =
            webSocket(authenticatedHomeeWebsocket(accessToken, homeeWs), pingInterval, exportOnlyGroup)

        homeeConnection.connect()
        homeeConnection.groups()

        logger.info { "Starting metrics collection..." }
        fixedRateTimer(name = "node_data", daemon = true, period = Duration.ofSeconds(checkInterval).toMillis()) {
            homeeConnection.asyncData()
        }

        logger.info { "Starting webserver at $bindHost:$bindPort" }
        HTTPServer(bindHost, bindPort)
    }
    Unit
}
