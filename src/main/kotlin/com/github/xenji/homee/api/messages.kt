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