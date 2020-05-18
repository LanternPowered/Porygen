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

import org.lanternpowered.porygen.value.Multiply
import org.lanternpowered.porygen.value.Multiply2
import org.lanternpowered.porygen.value.Multiply3
import org.lanternpowered.porygen.value.Value
import org.lanternpowered.porygen.value.Value2
import org.lanternpowered.porygen.value.Value3

object MultiplyType : I2Value("math/multiply", "Multiply") {
  override fun combine(in1: Value, in2: Value): Value = Multiply(in1, in2)
}

object Multiply2Type : I2Value2("math/2d/multiply", "Multiply (2D coordinates)") {
  override fun combine(in1: Value2, in2: Value2): Value2 = Multiply2(in1, in2)
}

object Multiply3Type : I2Value3("math/3d/multiply", "Multiply (3D coordinates)") {
  override fun combine(in1: Value3, in2: Value3): Value3 = Multiply3(in1, in2)
}
