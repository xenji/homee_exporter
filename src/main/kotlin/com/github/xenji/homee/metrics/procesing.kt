package com.github.xenji.homee.metrics

import com.github.xenji.homee.api.Node
import io.prometheus.client.Gauge
import java.net.URLDecoder

private val energyGauge: Gauge = Gauge
    .build("current_energy", "Tracks the current energy level of a meter")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

private val statusGauge = Gauge
    .build("current_state", "Tracks the current state of a switch")
    .labelNames("node_id", "attribute_id", "name")
    .register()

private val temperatureGauge = Gauge
    .build("current_temperature", "Tracks the current state of a thermometer")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

fun updateMetrics(nodes: List<Node>) {
    nodes.forEach {
        when (it.profile) {
            11, 12, 13, 19, 22, 2004 -> handleElectric(it)
        }
    }
}

private fun handleElectric(node: Node) {
    node.attributes.forEach {
        when (it.type) {
            3, 4 -> {
                energyGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded(), it.unit.urlDecoded())
                    .set(it.last_value.toDouble())
            }
        }
    }
}

fun String.urlDecoded(): String = URLDecoder.decode(this, "utf-8")