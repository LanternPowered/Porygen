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

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener

fun main() = Window(title = "Porygen", size = IntSize(600, 1000)) {
  Root()

  /*
  // https://github.com/JetBrains/compose-jb/issues/134
  // Get the wrapped ComposeLayer
  val window = AppWindowAmbient.current!!.window.contentPane.getComponent(0)
  // Get the original motion listener
  val listener = window.mouseMotionListeners.first()
  window.addMouseMotionListener(object : MouseMotionListener {
    override fun mouseDragged(e: MouseEvent) {
      // Also trigger mouse moved so the so we don't get locked
      // out of hover features while dragging
      listener.mouseMoved(e)
    }
    override fun mouseMoved(e: MouseEvent) {
    }
  })*/
}
