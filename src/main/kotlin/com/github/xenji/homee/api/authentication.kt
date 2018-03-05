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

    val response = authClient(request)
    if (response.status.code > 399) { // FIXME: This should be more precise. What if we get 5xx?
        throw RuntimeException("Authentication request failure: ${response.status}")
    }
    val token = response.cookies().first { it.name == "access_token" }.value
    response.close()
    return token
}
