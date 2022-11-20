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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexFlow
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpec
import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.ConstantDoubleSpec
import org.lanternpowered.porygen.graph.specs.noise.PerlinSpec
import org.lanternpowered.porygen.math.vector.Vec2d

@Composable
fun GraphEditor() {
  var graphValue = load(NodeGraphSpec.Default)
  if (graphValue == null) {
    graphValue = NodeGraph()

    val const1 = graphValue.create(ConstantDoubleSpec)
    const1.property(ConstantDoubleSpec.value)!!.value = 100.0

    val const2 = graphValue.create(ConstantDoubleSpec)
    const2.property(ConstantDoubleSpec.value)!!.value = 50.0

    val add = graphValue.create(AddDoubleSpec)
    add.title = "Add Doubles"

    const2.outputs.first().connectTo(add.inputs.first())

    graphValue.create(PerlinSpec, position = Vec2d(100.0, 100.0))
  }

  val graph by mutableStateOf(graphValue)

  Div(
    attrs = {
      style {
        backgroundColor(EditorColors.Background)
        color(EditorColors.NodeText)
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Column, FlexWrap.Nowrap)
        height(100.vh)
        width(100.vw)
      }
      onContextMenu { event ->
        // TODO: Commented for testing purposes
        // event.preventDefault()
      }
    },
  content = {
    Menu()
    NodeGrid(graph)
  })
}

@Composable
fun Menu() {
  MenuBar(
    attrs = {
      style {
        property("z-index", 100)
      }
    },
    items = listOf(
      MenuItem(
        content = { Text("File") },
        subItems = listOf(
          MenuItem(
            content = { Text("Save") },
            onClick = { println("Save") }
          ),
          MenuItem(
            content = { Text("Load") },
            onClick = { println("Load") },
            subItems = listOf(
              MenuItem(
                content = { Text("Text") },
                onClick = { println("Load Raw") }
              ),
              MenuItem(
                content = { Text("Local File") },
                onClick = { println("Load Local File") }
              )
            )
          )
        )
      ),
      MenuItem(
        content = { Text("Edit") },
        subItems = listOf(
          MenuItem(
            content = { Text("Copy") },
            onClick = { println("Copy") }
          )
        )
      )
    )
  )
}


