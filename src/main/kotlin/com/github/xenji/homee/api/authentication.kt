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
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.body.form
import org.http4k.core.cookie.cookies
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.urlEncoded

private val logger = KotlinLogging.logger("Authentication")

/**
 * Generate the access token and retrieve it from the cookie that is
 * sent back from the homee server.
 *
 * This token is valid for a year at the time of writing this.
 *
 * TODO: Remember the token validity and reissue a token if needed.
 */
fun homeeAccessToken(
    homeeUrl: String,
    user: String,
    encPassword: String,
    client: HttpHandler
): String {
    logger.trace { "Using $homeeUrl for requesting the access_token" }
    val authClient = ClientFilters.BasicAuth(user.urlEncoded(), encPassword.toLowerCase()).then(client)
    val request = Request(Method.POST, "$homeeUrl/access_token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .header("Referer", "https://my.hom.ee/")
        .header("Origin", "https://my.hom.ee")
        .form("device_hardware_id", "82b07059ef69a6b2e9285f05de76fe3b")
        .form("device_name", "Web App | Firefox (58.0)")
        .form("device_type", "4")
        .form("device_os", "6")
        .form("device_app", "1")

    return with(authClient(request)) {
        if (status.code > 399) {
            throw AuthenticationException("Authentication request failure: $status")
        }
        val cookie = cookies().first { it.name == "access_token" }
        close()
        cookie.value
    }
}

class AuthenticationException(msg: String) : RuntimeException(msg)
