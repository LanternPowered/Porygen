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
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.zoomable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.doubleTapGestureFilter
import androidx.compose.ui.gesture.rawDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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
  var active by remember { mutableStateOf(false) }
  TopAppBar {
    DropdownMenu(
      toggle = {
        Button(
          onClick = { active = !active },
          modifier = Modifier.padding(start = 5.dp, top = 10.dp),
          content = { Text("File") }
        )
      },
      expanded = active,
      onDismissRequest = { active = false },
      dropdownContent = {
        DropdownMenuItem(
          onClick = {
            println("Save")
          },
          content = {
            Text("Save")
          }
        )
      }
    )
  }
}

@Composable
fun NodeArea() {
  var scale by remember { mutableStateOf(1f) }
  var translate by remember { mutableStateOf(Offset.Zero) }
  val dragLock by remember { mutableStateOf(DragLock()) }

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .mouseScroll { delta, _ ->
        scale = (scale + delta * -0.006f).coerceAtLeast(0f)
        true
      }
      .zoomable(onZoomDelta = { scale *= it })
      .rawDragGestureFilter(object : DragObserver {
        override fun onDrag(dragDistance: Offset): Offset {
          if (!dragLock.isLocked)
            translate += dragDistance
          return Offset.Zero
        }
      })
      .doubleTapGestureFilter {
        // Re-center
        translate = Offset.Zero
      }
      .background(EditorColors.Background),
    children = {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().drawLayer(
          scaleX = scale,
          scaleY = scale,
          translationX = translate.x,
          translationY = translate.y
        ),
        children = {
          for (i in 1..2) {
            var position by remember { mutableStateOf(Offset.Zero) }
            var title by remember { mutableStateOf("Title $i") }
            Node(
              title = title,
              dragLock = dragLock,
              position = position,
              onUpdateTitle = { title = it },
              onUpdatePosition = { position = it }
            )
          }
        }
      )
    }
  )
}

object EditorColors {

  val Background = Color(31, 31, 31)

  val Node = Color(60, 60, 60)
  val NodeBorder = Color(20, 20, 20)
  val NodeInputs = Node
  val NodeOutputs = Color(45, 45, 45)

  val NodeText = Color(195, 195, 195)
}

class DragLock {
  var isLocked: Boolean = false
    private set

  fun lock() {
    isLocked = true
  }

  fun unlock() {
    isLocked = false
  }
}

@Composable
fun Node(
  dragLock: DragLock,
  title: String = "Title",
  onUpdateTitle: (String) -> Unit,
  position: Offset = Offset.Zero,
  onUpdatePosition: (Offset) -> Unit
) {
  val textStyle = TextStyle(color = EditorColors.NodeText)

  val paddingSize = 12.dp
  val borderSize = 1.dp
  val roundedCornerShape = RoundedCornerShape(4.dp)

  Column(
    modifier = Modifier
      .drawLayer(
        translationX = position.x,
        translationY = position.y
      )
      .preferredHeight(IntrinsicSize.Min)
      .preferredWidth(IntrinsicSize.Min),
  ) {
    Column(
      modifier = Modifier
        .rawDragGestureFilter(object : DragObserver {
          override fun onStart(downPosition: Offset) {
            dragLock.lock()
          }
          override fun onStop(velocity: Offset) {
            dragLock.unlock()
          }
          override fun onDrag(dragDistance: Offset): Offset {
            onUpdatePosition(position + Offset(dragDistance.x, dragDistance.y))
            return Offset.Zero
          }
        })
        .background(
          color = EditorColors.Node,
          shape = roundedCornerShape
        )
        .border(
          width = borderSize,
          color = EditorColors.NodeBorder,
          shape = roundedCornerShape
        ),
    ) {
      Row(
        modifier = Modifier
          .padding(paddingSize),
      ) {
        var editTitle by remember { mutableStateOf(false) }
        fun finishEdit() {
          editTitle = false
          if (title.isEmpty())
            onUpdateTitle("Title")
        }
        if (editTitle) {
          BasicTextField(
            value = title,
            textStyle = textStyle,
            onValueChange = { onUpdateTitle(title) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onImeActionPerformed = { finishEdit() }
          )
        } else {
          Text(
            text = title,
            color = EditorColors.NodeText,
            modifier = Modifier
              .doubleTapGestureFilter {
                editTitle = true
                // TODO: Why is this not triggering?
                println("EDIT")
              }
          )
        }
      }

      HorizontalDivider(
        thickness = borderSize,
        color = EditorColors.NodeBorder
      )

      // Inputs and outputs
      Row(
        modifier = Modifier
          .preferredHeight(IntrinsicSize.Min)
      ) {
        // Inputs
        Column(
          modifier = Modifier
            .background(EditorColors.Node)
        ) {
          Column(
            modifier = Modifier
              .padding(paddingSize)
          ) {
            Text("Input")
          }
        }
        VerticalDivider(
          thickness = borderSize,
          color = EditorColors.NodeBorder
        )
        // Outputs
        Column(
          modifier = Modifier
            .background(EditorColors.NodeOutputs)
        ) {
          Column(
            modifier = Modifier
              .padding(paddingSize)
          ) {
            Text("Output")
          }
        }
      }

      HorizontalDivider(
        thickness = borderSize,
        color = EditorColors.NodeBorder
      )

      Box(Modifier.preferredHeight(6.dp))
    }
  }
}

@Composable
fun HorizontalDivider(
  thickness: Dp = 1.dp,
  color: Color = Color.Black
) {
  Box(
    Modifier
      .preferredHeight(thickness)
      .fillMaxWidth()
      .background(color)
  )
}

@Composable
fun VerticalDivider(
  thickness: Dp = 1.dp,
  color: Color = Color.Black
) {
  Box(
    Modifier
      .preferredWidth(thickness)
      .fillMaxHeight()
      .background(color)
  )
}
