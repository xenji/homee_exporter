package com.github.xenji.homee.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.SystemExitException
import com.xenomachina.argparser.default

class ExporterArguments(parser: ArgParser) {
    val username: String by parser.storing(
        "--username",
        help = "homee username"
    )

    val password: String by parser.storing(
        "--password",
        help = "your SHA-512 hashed homee password. You can generate the hashed password by e.g. using https://passwordsgenerator.net/sha512-hash-generator/"
    )

    val bindHost: String by parser.storing(
        "--bind-host",
        help = "The IP or hostname to bind the web server to. Default: all interfaces"

    ).default("0.0.0.0")

    val bindPort: Int by parser.storing(
        "--bind-port",
        help = "The port to bind the web server to. Default: 7100",
        transform = { toInt() }
    ).default(7100)

    val homeeId: String by parser.storing(
        "--homee-id",
        help = "The unique ID of your homee (find it on the bottom of your cube)"
    ).addValidator {
        if (value.length != 12) {
            throw SystemExitException("The id must be 12 hex-digits", -1)
        }
    }

    val checkInterval: Long by parser.storing(
        "--check-interval",
        help = "Check interval in seconds. Default: 15",
        transform = { toLong() }
    ).default(15)
}