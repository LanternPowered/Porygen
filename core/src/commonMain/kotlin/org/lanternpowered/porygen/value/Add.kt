/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.value

import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.asNoiseModule

private data class Add(
  val source1: DoubleSupplier,
  val source2: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double =
    source1.get() + source2.get()
}

private data class Add2(
  val source1: Vec2dToDouble,
  val source2: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double =
    source1[x, y] + source2[x, y]
}

private data class Add3(
  val source1: Vec3dToDouble,
  val source2: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double =
    source1[x, y, z] + source2[x, y, z]
}

operator fun DoubleSupplier.plus(valueModule: DoubleSupplier): DoubleSupplier =
  Add(this, valueModule)
operator fun DoubleSupplier.plus(value: Double): DoubleSupplier =
  this + ConstantDouble(value)

operator fun Vec2dToDouble.plus(valueModule: Vec2dToDouble): Vec2dToDouble =
  Add2(this, valueModule)
operator fun Vec2dToDouble.plus(value: Double): Vec2dToDouble =
  this + ConstantDouble(value)

operator fun Vec3dToDouble.plus(valueModule: Vec3dToDouble): Vec3dToDouble =
  Add3(this, valueModule)
operator fun Vec3dToDouble.plus(value: Double): Vec3dToDouble =
  this + ConstantDouble(value)

operator fun NoiseModule.plus(valueModule: DoubleSupplier): NoiseModule =
  Add3(this, valueModule).asNoiseModule()
operator fun NoiseModule.plus(valueModule: Vec3dToDouble): NoiseModule =
  Add3(this, valueModule).asNoiseModule()
operator fun NoiseModule.plus(value: Double): NoiseModule =
  (this + ConstantDouble(value)).asNoiseModule()

operator fun Double.plus(valueModule: DoubleSupplier): DoubleSupplier =
  ConstantDouble(this) + valueModule
operator fun Double.plus(valueModule: Vec2dToDouble): Vec2dToDouble =
  ConstantDouble(this) + valueModule
operator fun Double.plus(valueModule: Vec3dToDouble): Vec3dToDouble =
  ConstantDouble(this) + valueModule
