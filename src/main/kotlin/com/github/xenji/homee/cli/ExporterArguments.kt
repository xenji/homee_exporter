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
        help = "your SHA-512 hashed homee password. You can generate the hashed password by e.g. " +
            "using https://passwordsgenerator.net/sha512-hash-generator/"
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

    val pingInterval: Long by parser.storing(
        "--ping-interval",
        help = "The interval in seconds used to ping the homee. Default: 10",
        transform = { toLong() }
    ).default(10)

    val exportOnlyGroup: Int by parser.storing(
        "--export-group-id",
        help = "Exports only metrics from the given group ID. If the group is not set, " +
            "all supported devices are exported.",
        transform = { toInt() }
    ).default(-1)
}
