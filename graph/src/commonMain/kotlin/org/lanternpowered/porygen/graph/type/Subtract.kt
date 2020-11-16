/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.type

import org.lanternpowered.porygen.value.Value
import org.lanternpowered.porygen.value.Value2
import org.lanternpowered.porygen.value.Value3
import org.lanternpowered.porygen.value.minus

object SubtractType : I2Value("math/subtract", "Subtract") {
  override fun combine(in1: Value, in2: Value): Value = in1 - in2
}

object Subtract2Type : I2Value2("math/2d/subtract", "Subtract (2D coordinates)") {
  override fun combine(in1: Value2, in2: Value2): Value2 = in1 - in2
}

object Subtract3Type : I2Value3("math/3d/subtract", "Subtract (3D coordinates)") {
  override fun combine(in1: Value3, in2: Value3): Value3 = in1 - in2
}
