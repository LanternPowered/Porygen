/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.editor

import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpec
import org.lanternpowered.porygen.graph.serializer.toJson
import org.lanternpowered.porygen.graph.serializer.toNodeGraph

private const val NodeGraph = "nodeGraph"

fun load(spec: NodeGraphSpec): NodeGraph? {
  return when (val nodeGraphJson = localStorage.getItem(NodeGraph)) {
    null -> null
    else -> Json.decodeFromString<JsonObject>(nodeGraphJson).toNodeGraph(spec)
  }
}

fun save(graph: NodeGraph) {
  localStorage.setItem(NodeGraph, Json.encodeToString(graph.toJson()))
}
