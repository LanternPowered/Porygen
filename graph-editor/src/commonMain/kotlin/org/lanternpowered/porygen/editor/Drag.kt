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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.rawDragGestureFilter

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
  onStop: (velocity: Offset) -> Unit = {},
  onConsumeDrag: (dragDistance: Offset) -> Offset = { Offset.Zero },
  onCancel: () -> Unit = {},
  onDrag: (dragDistance: Offset) -> Unit = {},
) = composed {
  val hasLock = remember { mutableStateOf(false) }
  rawDragGestureFilter(object : DragObserver {
    override fun onCancel() {
      if (lock != null) {
        if (!hasLock.value)
          return
        lock.unlock()
        hasLock.value = false
      }
      onCancel()
    }
    override fun onStart(downPosition: Offset) {
      if (lock != null) {
        val success = lock.lock()
        if (!success)
          return
        hasLock.value = true
      }
      onStart(downPosition)
    }
    override fun onStop(velocity: Offset) {
      if (lock != null) {
        if (!hasLock.value)
          return
        lock.unlock()
        hasLock.value = false
      }
      onStop(velocity)
    }
    override fun onDrag(dragDistance: Offset): Offset {
      if (lock != null && !hasLock.value)
        return Offset.Zero
      onDrag(dragDistance)
      return onConsumeDrag(dragDistance)
    }
  })
}
