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

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSizeIn
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ripple.rememberRippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.globalBounds
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

data class MenuItem(
  val content: @Composable () -> Unit,
  val subItems: List<MenuItem>? = null,
  val onClick: (() -> Unit)? = null
)

@Composable
fun MenuBar(
  modifier: Modifier = Modifier,
  items: List<MenuItem>
) {
  Row(modifier) {
    // For now don't use a delegate here
    // https://github.com/JetBrains/compose-jb/issues/132
    val active: MutableState<Int?> = remember { mutableStateOf(null) }
    for ((index, item) in items.withIndex()) {
      var popupOffset by remember { mutableStateOf(Offset.Zero) }
      Button(
        onClick = {
          active.value = index
          item.onClick?.invoke()
        },
        modifier = Modifier
          .onGloballyPositioned { coordinates ->
            popupOffset = coordinates.boundsInRoot.bottomLeft
          },
      ) {
        item.content()
      }
      if (active.value != index || item.subItems == null)
        continue
      Popup(
        isFocusable = true,
        alignment = Alignment.TopStart,
        onDismissRequest = { active.value = null }
      ) {
        InnerMenuBar(
          items = item.subItems,
          offset = popupOffset
        )
      }
    }
  }
}

@Composable
private fun InnerMenuBar(
  items: List<MenuItem>,
  offset: Offset
) {
  var active: Int? by remember { mutableStateOf(null) }
  val popupOffsets: Array<Offset> by remember { mutableStateOf(Array(items.size) { Offset.Zero}) }
  Box(
    modifier = Modifier
      .absoluteOffset(
        x = { offset.x },
        y = { offset.y }
      )
  ) {
    Card(elevation = 0.dp) {
      ScrollableColumn(
        modifier = Modifier
          .preferredWidth(IntrinsicSize.Max),
      ) {
        for ((index, menuItem) in items.withIndex()) {
          // We need to wrap into another box to get the proper bounds,
          // if the padding is applied to the same box, its ignored in
          // the bounds
          Box(
            modifier = Modifier
              .onGloballyPositioned { coordinates ->
                popupOffsets[index] = coordinates.globalBounds.topRight
              }
          ) {
            Box(
              modifier = Modifier
                .clickable(
                  onClick = {
                    menuItem.onClick?.invoke()
                    active = index
                  },
                  indication = rememberRippleIndication(true)
                )
                .fillMaxWidth()
                .preferredSizeIn(minWidth = 100.dp)
                .padding(horizontal = 10.dp, vertical = 6.dp),
              contentAlignment = Alignment.CenterStart
            ) {
              menuItem.content()
            }
          }
        }
      }
    }
  }
  val active0 = active
  if (active0 != null) {
    val menuItem = items[active0]
    if (menuItem.subItems != null) {
      InnerMenuBar(
        items = menuItem.subItems,
        offset = popupOffsets[active0]
      )
    }
  }
}
