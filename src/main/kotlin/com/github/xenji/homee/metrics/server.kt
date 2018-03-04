package com.github.xenji.homee.metrics

import io.prometheus.client.exporter.HTTPServer

fun startServer(host: String, port: Int) = HTTPServer(host, port, false)
