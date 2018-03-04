package com.github.xenji.homee.api

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.body.form
import org.http4k.core.cookie.cookies
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.urlEncoded

fun homeeAccessToken(
    homeeUrl: String,
    user: String,
    encPassword: String,
    client: HttpHandler
): String {
    val authClient = ClientFilters.BasicAuth(user.urlEncoded(), encPassword).then(client)
    val request = Request(Method.POST, "$homeeUrl/access_token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .header("Referer", "https://my.hom.ee/")
        .header("Origin", "https://my.hom.ee")
        .form("device_hardware_id", "82b07059ef69a6b2e9285f05de76fe3b")
        .form("device_name", "Prometheus Exporter")
        .form("device_type", "0")
        .form("device_os", "0")
        .form("device_app", "1")
    return authClient(request).cookies().first { it.name == "access_token" }.value
}
