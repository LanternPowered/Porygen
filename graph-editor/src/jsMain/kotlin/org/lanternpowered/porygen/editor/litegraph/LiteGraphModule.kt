/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:JsModule("litegraph.js")
@file:JsNonModule
package org.lanternpowered.porygen.editor.litegraph

@JsName("LiteGraph")
internal external object LiteGraph {

  fun createNode(type: String): GraphNode
}

@JsName("LGraph")
external class Graph {

  fun add(node: GraphNode)

  fun start()
}

@JsName("LGraphCanvas")
external class GraphCanvas(id: String, graph: Graph)

@JsName("LGraphNode")
external class GraphNode {

  var pos: Array<Double>

  fun setValue(value: Any)
}
