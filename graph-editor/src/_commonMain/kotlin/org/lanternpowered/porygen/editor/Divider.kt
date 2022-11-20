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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDivider(
  thickness: Dp = 1.dp,
  color: Color = Color.Black
) {
  Box(
    Modifier
      .requiredHeight(thickness)
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
      .requiredWidth(thickness)
      .fillMaxHeight()
      .background(color)
  )
}
