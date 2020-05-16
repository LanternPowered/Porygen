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
