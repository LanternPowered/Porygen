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

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.zoomable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.doubleTapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

expect fun Modifier.mouseScroll(onMouseScroll: (delta: Float, bounds: IntSize) -> Boolean): Modifier

@Composable
fun Root() {
  MaterialTheme {
    Column {
      Menu()
      Row {
        NodeArea()
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

@Composable
fun NodeArea() {
  var scale by remember { mutableStateOf(1f) }
  var translate by remember { mutableStateOf(Offset.Zero) }
  val dragLock by remember { mutableStateOf(DragLock()) }

  Box(
    modifier = Modifier
      .mouseScroll { delta, _ ->
        scale *= 1f + (-delta * 0.006f)
        true
      }
      .zoomable(onZoomDelta = { scale *= it })
      .onDrag(dragLock) { dragDistance ->
        translate += dragDistance
      }
      .doubleTapGestureFilter {
        // Re-center
        translate = Offset.Zero
      }
      .background(EditorColors.Background),
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .absoluteOffset(
          x = { translate.x },
          y = { translate.y }
        )
        .drawLayer(
          // https://github.com/JetBrains/compose-jb/issues/127
          //   scale will currently break tap/clickables
          scaleX = scale,
          scaleY = scale
        ),
    ) {
      for (i in 1..2) {
        var position by remember { mutableStateOf(Offset.Zero) }
        var title by remember { mutableStateOf("Title $i") }
        Node(
          title = title,
          dragLock = dragLock,
          position = position,
          onUpdateTitle = { title = it },
          onUpdatePosition = { position = it },
          scale = scale,
          onStopOutputPortDrag = {
            println("STOP OUTPUT DRAG")
          },
          onHoverInputPort = {
            print("HOVER INPUT")
          }
        )
      }
    }
  }
}

object EditorColors {

  val Background = Color(31, 31, 31)

  val Node = Color(60, 60, 60)
  val NodeBorder = Color(20, 20, 20)
  val NodeInnerDivider = Color(30, 30, 30)
  val NodeInputs = Node
  val NodeOutputs = Color(45, 45, 45)

  val NodeText = Color(195, 195, 195)
}
