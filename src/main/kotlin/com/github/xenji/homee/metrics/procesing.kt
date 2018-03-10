package com.github.xenji.homee.metrics

import com.github.xenji.homee.api.Node
import io.prometheus.client.Gauge
import java.net.URLDecoder

private val homeeGauge: Gauge = Gauge
    .build("current_homee", "Tracks the homee status")
    .labelNames("status_id", "status_name")
    .register()

private val energyGauge: Gauge = Gauge
    .build("current_energy", "Tracks the current energy level of a meter")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

private val statusGauge = Gauge
    .build("current_binarystate", "Tracks the current state of a switch")
    .labelNames("node_id", "attribute_id", "name")
    .register()

private val motionGauge = Gauge
    .build("current_motion", "Tracks the current state of a motion detector")
    .labelNames("node_id", "attribute_id", "name")
    .register()

private val lightGauge = Gauge
    .build("current_light", "Tracks the current light level of a sensor")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

private val temperatureGauge = Gauge
    .build("current_temperature", "Tracks the current state of a thermometer")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

private val batteryGauge = Gauge
    .build("current_battery", "Tracks the current battery level")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

private val linkQualityGauge = Gauge
    .build("current_linkquality", "Tracks the current battery level")
    .labelNames("node_id", "attribute_id", "name", "unit")
    .register()

fun updateMetrics(nodes: List<Node>, onlyInGroup: Int = -1) = nodes
    .filter {
        if (onlyInGroup > 0) it.id in groupToNodeMembership.get(onlyInGroup)
        else true
    }
    .forEach {
        when (it.id) {
            -1 -> handleHomee(it)
        }
        when (it.profile) {
            11, 12, 13, 19, 22, 2004 -> handleElectric(it)
            2003, 3001, 3005, 3009, 4016, 4017 -> handleTemperature(it)
            4010, 4015 -> {
                handleTemperature(it)
                handleLight(it)
            }
            1000 -> handleLight(it)
        }
        // todo: iterate only once over attrs
        handleBatteryLevel(it)
        handleLinkQuality(it)
        handleBinarySwitch(it)
        handleMotionALarm(it)
    }

private fun handleHomee(node: Node) = when (node.attributes.find { it.type == 205 }!!.current_value) {
    0.0 -> homeeGauge.labels("0", "at_home").set(0.0)
    1.0 -> homeeGauge.labels("1", "sleeping").set(1.0)
    2.0 -> homeeGauge.labels("2", "away").set(2.0)
    3.0 -> homeeGauge.labels("3", "vacation").set(3.0)
    else -> {
    }
}

private fun handleLight(node: Node) = node
    .attributes
    .forEach {
        when (it.type) {
            11 -> {
                lightGauge.labels(
                    node.id.toString(),
                    it.id.toString(),
                    node.name.urlDecoded(),
                    it.unit.urlDecoded()
                )
                    .set(it.current_value)
            }
        }
    }

private fun handleTemperature(node: Node) = node
    .attributes
    .forEach {
        when (it.type) {
            5 -> {
                temperatureGauge.labels(
                    node.id.toString(),
                    it.id.toString(),
                    node.name.urlDecoded(),
                    it.unit.urlDecoded()
                )
                    .set(it.current_value)
            }
        }
    }

private fun handleElectric(node: Node) = node
    .attributes
    .forEach {
        when (it.type) {
            3, 4 -> {
                energyGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded(), it.unit.urlDecoded())
                    .set(it.current_value)
            }
        }
    }

private fun handleBatteryLevel(node: Node) = node
    .attributes
    .filter {
        it.type == 8
    }
    .forEach {
        batteryGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded(), it.unit.urlDecoded())
            .set(it.current_value)
    }

private fun handleLinkQuality(node: Node) = node
    .attributes
    .filter {
        it.type == 33
    }
    .forEach {
        linkQualityGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded(), it.unit.urlDecoded())
            .set(it.current_value)
    }

private fun handleBinarySwitch(node: Node) = node
    .attributes
    .filter {
        it.type == 1 || it.type == 14 || it.type == 19
    }
    .forEach {
        statusGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded())
            .set(it.current_value)
    }

private fun handleMotionALarm(node: Node) = node
    .attributes
    .filter {
        it.type == 25
    }
    .forEach {
        motionGauge.labels(node.id.toString(), it.id.toString(), node.name.urlDecoded())
            .set(it.current_value)
    }

fun String.urlDecoded(): String = URLDecoder.decode(this, "utf-8")