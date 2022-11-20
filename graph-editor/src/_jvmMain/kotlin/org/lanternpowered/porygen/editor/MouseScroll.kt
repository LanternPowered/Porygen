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

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.mouse.MouseScrollUnit
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.unit.IntSize

actual fun Modifier.mouseScroll(
  onMouseScroll: (delta: Float, bounds: IntSize) -> Boolean
): Modifier =
  mouseScrollFilter(onMouseScroll = { event, bounds ->
    val delta = event.delta
    val deltaValue = if (delta is MouseScrollUnit.Line) {
      delta.value
    } else {
      0f
    }
    onMouseScroll(deltaValue, bounds)
  })
