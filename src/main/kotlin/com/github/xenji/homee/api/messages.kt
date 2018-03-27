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

data class Attribute(
    val id: Int,
    val state: Int,
    val node_id: Int,  // associated node
    val instance: Int,  // instance of same types
    val minimum: Double,  // minimum of value
    val maximum: Double,  // maximum of value
    val current_value: Double,  // current value
    val target_value: Double,  // target value
    val last_value: Double,  // last value
    val data: String?,  // possible string value
    val unit: String,  // unit or "n%2Fa"
    val step_value: Double,  // step value of value
    val editable: Int,  // value is editable or not
    val type: Int,  // type of attribute
    val last_changed: Int,  // timestamp in sec from 1970
    val changed_by: Int,  // e.g. homeegram, user, itself
    val changed_by_id: Int,  // homeegram id, user id or 0
    val based_on: Int,  // events, interval or polling
    val options: List<String>
)

data class Node(
    val id: Int,  // unique ID
    val name: String,  // user-defined name
    val profile: Int,  // type of node
    val image: String,  // reserved for icons
    val favorite: Int,  // favorited true/false
    val order: Int,  // sorting mechanism
    val protocol: Int,  // radio protocol
    val history: Int,  // save data
    val cube_type: Int,  // defines icon color
    val routing: Int,  // routing-enabled
    val state: Int,  // ok, initializing, ...
    val state_changed: Int,  // timestamp in sec from 1970
    val services: Short,
    val phonetic_name: String,
    val added: Int,  // timestamp in sec from 1970
    val owner: Int,  // user who added the node
    val note: String,
    val denied_user_ids: List<Int>, // array of denied users
    val attributes: List<Attribute> = listOf()
)

data class NodesResponse(val nodes: List<Node>)

data class Relationship(val id: Int, val group_id: Int, val node_id: Int, val homeegram_id: Int, val order: Int)

data class Relationships(val relationships: List<Relationship> = listOf())
