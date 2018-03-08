package com.github.xenji.homee.metrics

import com.github.xenji.homee.api.Relationship
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder

typealias GroupToNodeMultiMap = Multimap<Int, Int>

/**
 * Group Membership
 */
val groupToNodeMembership: GroupToNodeMultiMap = MultimapBuilder.hashKeys().hashSetValues().build()

fun updateRelationships(relationships: List<Relationship>) {
    relationships.forEach {
        groupToNodeMembership.get(it.group_id).add(it.node_id)
    }
}
