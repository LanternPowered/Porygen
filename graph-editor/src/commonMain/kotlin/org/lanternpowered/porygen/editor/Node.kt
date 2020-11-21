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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.doubleTapGestureFilter
import androidx.compose.ui.gesture.rawDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NodeHeaderPadding = 8.dp
private val NodeTextStyle = TextStyle(
  color = EditorColors.NodeText,
  fontSize = 16.sp
)

@Composable
fun NodeTitle(
  title: String,
  onUpdateTitle: (String) -> Unit
) {
  var editTitle by remember { mutableStateOf(false) }
  fun finishEdit() {
    editTitle = false
    // Reset to the default title if needed
    if (title.isEmpty())
      onUpdateTitle("Title")
  }
  val horizontalTextPadding = 4.dp
  if (editTitle) {
    BasicTextField(
      value = title,
      textStyle = NodeTextStyle + TextStyle(textDecoration = TextDecoration.Underline),
      onValueChange = { onUpdateTitle(it) },
      singleLine = true,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      onImeActionPerformed = { finishEdit() },
      modifier = Modifier
        // Without this, content after the text field may
        // be pushed a bit out of bounds
        .preferredWidth(IntrinsicSize.Max)
        .padding(horizontal = horizontalTextPadding)
    )
  } else {
    Box(
      modifier = Modifier
        .clickable(
          onDoubleClick = { editTitle = true },
          onClick = {}
        )
        .doubleTapGestureFilter { editTitle = true }
    ) {
      BasicText(
        text = title,
        style = NodeTextStyle,
        softWrap = false,
        modifier = Modifier
          .padding(horizontal = horizontalTextPadding)
          // Make sure that the complete text is visible
          .preferredWidth(IntrinsicSize.Max)
      )
    }
  }
}

@Composable
fun NodeHeader(
  title: String,
  onUpdateTitle: (String) -> Unit,
  expanded: Boolean,
  onUpdateExpanded: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .padding(NodeHeaderPadding)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    NodeTitle(title, onUpdateTitle)

    Icon(
      modifier = Modifier
        .size(24.dp)
        .clickable(onClick = {
          onUpdateExpanded(!expanded)
        }),
      imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowLeft,
      tint = EditorColors.NodeText
    )
  }
}

@Composable
fun Node(
  dragLock: DragLock,
  title: String = "Title",
  onUpdateTitle: (String) -> Unit,
  position: Offset = Offset.Zero,
  onUpdatePosition: (Offset) -> Unit,
  scale: Float,
  onHoverInputPort: (Int) -> Unit,
  onStopOutputPortDrag: (Int) -> Unit
) {
  var size by remember { mutableStateOf(Size.Zero) }

  val paddingSize = 12.dp
  val borderSize = 1.dp
  val roundedCornerShape = RoundedCornerShape(4.dp)

  Canvas(Modifier) {
    drawLine(Color.White, start = Offset.Zero, end = position + Offset(0f, size.height / 2f))
  }

  Box(
    modifier = Modifier
      .absoluteOffset(
        x = { position.x },
        y = { position.y }
      )
      .preferredHeight(IntrinsicSize.Min)
      .preferredWidth(IntrinsicSize.Min)
      .onGloballyPositioned { coordinates ->
        size = coordinates.boundsInParent.size
      },
  ) {
    var expanded by remember { mutableStateOf(true) }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          color = EditorColors.Node,
          shape = roundedCornerShape
        )
        .border(
          width = borderSize,
          color = EditorColors.NodeBorder,
          shape = roundedCornerShape
        )
        // This just holds the lock so the node area
        // can't acquire the lock when dragging
        .onDrag(dragLock),
    ) {
      Box(modifier = Modifier
        .onDrag(dragLock) { dragDistance ->
          onUpdatePosition(position + Offset(dragDistance.x, dragDistance.y) / scale)
        }
      ) {
        NodeHeader(
          title = title,
          onUpdateTitle = onUpdateTitle,
          expanded = expanded,
          onUpdateExpanded = { expanded = it }
        )
      }

      if (expanded) {
        HorizontalDivider(
          thickness = borderSize,
          color = EditorColors.NodeInnerDivider
        )

        // Inputs and outputs
        Row(
          modifier = Modifier
            .preferredHeight(IntrinsicSize.Min)
        ) {
          // Inputs
          Column(
            modifier = Modifier
              .background(EditorColors.NodeInputs)
              .weight(1f)
              .fillMaxHeight()
          ) {
            Column(
              modifier = Modifier
                .padding(7.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.preferredSize(10.dp).clip(CircleShape).background(Color.Red))
                Spacer(Modifier.width(5.dp))
                Text("in1")
              }
              Row(verticalAlignment = Alignment.CenterVertically) {
                var isHovering by remember { mutableStateOf(false) }
                var hasDrawLock by remember { mutableStateOf(false) }
                Box(
                  modifier = Modifier
                    .preferredSize(10.dp)
                    .clip(CircleShape)
                    .background(if (isHovering) Color.Blue else Color.Red)
                    .onHover(
                      onEnter = {
                        isHovering = true
                        onHoverInputPort(1)
                      },
                      onExit = {
                        isHovering = false
                      }
                    )
                    .rawDragGestureFilter(object : DragObserver {
                      override fun onStart(downPosition: Offset) {
                        println("START B")
                        if (dragLock.lock())
                          hasDrawLock = true
                      }

                      override fun onStop(velocity: Offset) {
                        if (!hasDrawLock)
                          return
                        dragLock.unlock()
                        hasDrawLock = false
                        onStopOutputPortDrag(0)
                      }
                    })
                )
                Spacer(Modifier.width(5.dp))
                Text("in2")
              }
            }
          }
          VerticalDivider(
            thickness = borderSize,
            color = EditorColors.NodeInnerDivider
          )
          // Outputs
          Column(
            modifier = Modifier
              .background(EditorColors.NodeOutputs)
              .weight(1f)
              .fillMaxHeight()
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
          color = EditorColors.NodeInnerDivider
        )

        Box(Modifier.preferredHeight(6.dp))
      }
    }
  }
}
