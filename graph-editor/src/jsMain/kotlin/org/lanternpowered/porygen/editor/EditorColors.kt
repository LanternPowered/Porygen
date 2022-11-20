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

import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba

object EditorColors {

  val Background = rgb(31, 31, 31)

  val Node = rgba(83, 83, 83, 0.75)
  val NodeBorder = rgb(20, 20, 20)
  val NodeInnerDivider = rgb(30, 30, 30)
  val NodeInputs = Node

  val NodeOutputs = rgba(32, 32, 32, 0.5)
  val NodeSelectionOutline = rgb(90, 130, 150)

  val NodeText = rgb(195, 195, 195)

  val NodePortInner = rgb(36, 36, 36)
  val NodePortDisabledBorder = rgb(23, 23, 23)
}
