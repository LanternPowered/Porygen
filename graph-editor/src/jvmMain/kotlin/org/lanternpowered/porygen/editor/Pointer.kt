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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerMoveFilter

actual fun Modifier.onHover(
  onMove: (position: Offset) -> Unit,
  onExit: () -> Unit,
  onEnter: () -> Unit
): Modifier = pointerMoveFilter(
  onMove = { onMove(it); false; },
  onExit = { onExit(); false; },
  onEnter = { onEnter(); false; }
)
