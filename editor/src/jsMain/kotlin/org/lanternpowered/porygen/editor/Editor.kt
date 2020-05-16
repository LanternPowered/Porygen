package org.lanternpowered.porygen.editor

import org.lanternpowered.porygen.editor.litegraph.Graph
import org.lanternpowered.porygen.editor.litegraph.GraphCanvas
import org.lanternpowered.porygen.editor.litegraph.LiteGraph
import react.dom.h1
import react.dom.render
import kotlin.browser.document

fun main() {
  render(document.getElementById("root")) {
    h1 {
      +"Hello world!"
    }
  }

  val graph = Graph()
  val graphCanvas = GraphCanvas("#editor-canvas", graph)

  val node = LiteGraph.createNode("basic/const")
  node.pos = arrayOf(10.0, 10.0)
  node.setValue(4.5)

  graph.add(node)
  graph.start()
}
