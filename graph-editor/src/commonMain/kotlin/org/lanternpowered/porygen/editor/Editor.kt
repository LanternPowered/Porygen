/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.porygen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.spec.DefaultNodeGraphSpec
import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.ConstantDoubleSpec

expect fun Modifier.mouseScroll(onMouseScroll: (delta: Float, bounds: IntSize) -> Boolean): Modifier

@Composable
fun Root() {
  val graph = NodeGraph(DefaultNodeGraphSpec)

  val const1 = graph.create(ConstantDoubleSpec)
  const1.property(ConstantDoubleSpec.value)!!.value = 100.0

  val const2 = graph.create(ConstantDoubleSpec)
  const2.property(ConstantDoubleSpec.value)!!.value = 50.0

  val add = graph.create(AddDoubleSpec)
  add.title = "Add Doubles"

  MaterialTheme {
    Column {
      Menu()
      Row {
        DraggableNodeGraphGrid(graph)
      }
    }
  }
}

@Composable
fun Menu() {
  TopAppBar(
    modifier = Modifier
      .zIndex(1f)
  ) {
    MenuBar(
      modifier = Modifier
        .padding(start = 5.dp, top = 10.dp),
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
}

object EditorColors {

  val Background = Color(31, 31, 31)

  val Node = Color(60, 60, 60)
  val NodeBorder = Color(20, 20, 20)
  val NodeInnerDivider = Color(30, 30, 30)
  val NodePortInner = Color(30, 30, 30)
  val NodeInputs = Node
  val NodeOutputs = Color(45, 45, 45)
  val NodeSelectionOutline = Color(90, 130, 150)

  val NodeText = Color(195, 195, 195)
}
