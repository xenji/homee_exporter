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
package com.github.xenji.homee.metrics

import com.github.xenji.homee.api.Attribute
import com.github.xenji.homee.api.Node
import io.prometheus.client.Gauge
import java.net.URLDecoder

private val homeeGauge: Gauge =
    Gauge.build("current_homee", "Tracks the homee status").labelNames("status_id", "status_name").register()
private val energyGauge: Gauge = meteredGauge("current_energy", "Tracks the current energy level of a meter")
private val statusGauge = binaryGauge("current_binarystate", "Tracks the current state of a switch")
private val motionGauge = binaryGauge("current_motion", "Tracks the current state of a motion detector")
private val lightGauge = meteredGauge("current_light", "Tracks the current light level of a sensor")
private val temperatureGauge = meteredGauge("current_temperature", "Tracks the current state of a thermometer")
private val batteryGauge = meteredGauge("current_battery", "Tracks the current battery level")
private val linkQualityGauge = meteredGauge("current_linkquality", "Tracks the current battery level")

fun updateMetrics(nodes: List<Node>, onlyInGroup: Int = -1) = nodes
    .filter { if (onlyInGroup > 0) it.id in groupToNodeMembership.get(onlyInGroup) else true }
    .forEach {
        when (it.id) {
            -1 -> handleHomee(it)
            else -> it.attributes.forEach { attr ->
                when (attr.type) {
                    1 -> updateHavingBinaryStatus(statusGauge, it.id, it.name, attr)
                    3, 4 -> updateHavingMeteredValue(energyGauge, it.id, it.name, attr)
                    5 -> updateHavingMeteredValue(temperatureGauge, it.id, it.name, attr)
                    8 -> updateHavingMeteredValue(batteryGauge, it.id, it.name, attr)
                    11 -> updateHavingMeteredValue(lightGauge, it.id, it.name, attr)
                    14, 19 -> updateHavingBinaryStatus(statusGauge, it.id, it.name, attr)
                    25 -> updateHavingBinaryStatus(motionGauge, it.id, it.name, attr)
                    33 -> updateHavingMeteredValue(linkQualityGauge, it.id, it.name, attr)
                }
            }
        }
    }

private fun handleHomee(node: Node) = when (node.attributes.find { it.type == 205 }!!.current_value) {
    0.0 -> homeeGauge.labels("0", "at_home").set(0.0)
    1.0 -> homeeGauge.labels("1", "sleeping").set(1.0)
    2.0 -> homeeGauge.labels("2", "away").set(2.0)
    3.0 -> homeeGauge.labels("3", "vacation").set(3.0)
    else -> {
        // om-nom-nom
    }
}

private fun updateHavingBinaryStatus(gauge: Gauge, nodeId: Int, nodeName: String, attr: Attribute) =
    gauge.labels(
        nodeId.toString(),
        attr.id.toString(),
        nodeName.urlDecoded()
    ).set(attr.current_value)

private fun updateHavingMeteredValue(gauge: Gauge, nodeId: Int, nodeName: String, attr: Attribute) =
    gauge.labels(
        nodeId.toString(),
        attr.id.toString(),
        nodeName.urlDecoded(),
        attr.unit.urlDecoded()
    ).set(attr.current_value)

private fun meteredGauge(name: String, help: String) =
    Gauge.build(name, help).labelNames("node_id", "attribute_id", "name", "unit").register()

private fun binaryGauge(name: String, help: String) =
    Gauge.build(name, help).labelNames("node_id", "attribute_id", "name").register()

fun String.urlDecoded(): String = URLDecoder.decode(this, Charsets.UTF_8.name())