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

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

class DragLock {
  private var _isLocked = false

  val isLocked: Boolean
    get() = _isLocked

  fun lock(): Boolean {
    if (_isLocked)
      return false
    _isLocked = true
    return true
  }

  fun unlock() {
    _isLocked = false
  }
}

@Composable
fun Modifier.onDrag(
  lock: DragLock? = null,
  onStart: (downPosition: Offset) -> Unit = {},
  onEnd: () -> Unit = {},
  onCancel: () -> Unit = {},
  onDrag: (dragDistance: Offset) -> Unit = {},
) = composed {
  val hasLock = remember { mutableStateOf(false) }
  pointerInput(Unit) {
    detectDragGestures(
      onDragStart = { downPosition ->
        if (lock != null) {
          val success = lock.lock()
          if (!success)
            return@detectDragGestures
          hasLock.value = true
        }
        onStart(downPosition)
      },
      onDragEnd = {
        if (lock != null) {
          if (!hasLock.value)
            return@detectDragGestures
          lock.unlock()
          hasLock.value = false
        }
        onEnd()
      },
      onDragCancel = {
        if (lock != null) {
          if (!hasLock.value)
            return@detectDragGestures
          lock.unlock()
          hasLock.value = false
        }
        onCancel()
      },
      onDrag = { _, offset ->
        if (lock != null && !hasLock.value)
          return@detectDragGestures
        onDrag(offset)
      }
    )
  }
}
