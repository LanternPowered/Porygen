/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs

import org.lanternpowered.porygen.value.DoubleSupplier
import org.lanternpowered.porygen.value.Vec2dToDouble
import org.lanternpowered.porygen.value.Vec3dToDouble
import org.lanternpowered.porygen.value.times

object MultiplyDoubleSpec : I2Value("math/multiply", "Multiply") {
  override fun combine(in1: DoubleSupplier, in2: DoubleSupplier): DoubleSupplier = in1 * in2
}

object MultiplyVec2ToDoubleSpec : I2Value2("math/2d/multiply", "Multiply (2D coordinates)") {
  override fun combine(in1: Vec2dToDouble, in2: Vec2dToDouble): Vec2dToDouble = in1 * in2
}

object MultiplyVec3ToDoubleSpec : I2Value3("math/3d/multiply", "Multiply (3D coordinates)") {
  override fun combine(in1: Vec3dToDouble, in2: Vec3dToDouble): Vec3dToDouble = in1 * in2
}
