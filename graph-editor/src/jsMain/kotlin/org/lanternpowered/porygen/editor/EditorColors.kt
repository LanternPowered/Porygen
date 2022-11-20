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

object EditorColors {

  val Background = rgb(31, 31, 31)

  val Node = rgb(60, 60, 60)
  val NodeBorder = rgb(20, 20, 20)
  val NodeInnerDivider = rgb(30, 30, 30)
  val NodeInputs = Node

  val NodeOutputs = rgb(45, 45, 45)
  val NodeSelectionOutline = rgb(90, 130, 150)

  val NodeText = rgb(195, 195, 195)

  val NodePortInner = rgb(36, 36, 36)
  val NodePortDisabledBorder = rgb(23, 23, 23)
}
