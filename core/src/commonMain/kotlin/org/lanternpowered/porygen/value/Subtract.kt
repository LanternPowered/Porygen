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

private data class Subtract(
  val source1: DoubleSupplier,
  val source2: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double = source1.get() - source2.get()
}

private data class Subtract2(
  val source1: Vec2dToDouble,
  val source2: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = source1[x, y] - source2[x, y]
}

private data class Subtract3(
  val source1: Vec3dToDouble,
  val source2: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = source1[x, y, z] - source2[x, y, z]
}

operator fun DoubleSupplier.minus(valueModule: DoubleSupplier): DoubleSupplier =
  Subtract(this, valueModule)
operator fun DoubleSupplier.minus(value: Double): DoubleSupplier =
  this - ConstantDouble(value)

operator fun Vec2dToDouble.minus(valueModule: Vec2dToDouble): Vec2dToDouble =
  Subtract2(this, valueModule)
operator fun Vec2dToDouble.minus(value: Double): Vec2dToDouble =
  this - ConstantDouble(value)

operator fun Vec3dToDouble.minus(valueModule: Vec3dToDouble): Vec3dToDouble =
  Subtract3(this, valueModule)
operator fun Vec3dToDouble.minus(value: Double): Vec3dToDouble =
  this - ConstantDouble(value)

operator fun NoiseModule.minus(valueModule: DoubleSupplier): NoiseModule =
  Subtract3(this, valueModule).asNoiseModule()
operator fun NoiseModule.minus(valueModule: Vec3dToDouble): NoiseModule =
  Subtract3(this, valueModule).asNoiseModule()
operator fun NoiseModule.minus(value: Double): NoiseModule =
  (this - ConstantDouble(value)).asNoiseModule()

operator fun Double.minus(valueModule: DoubleSupplier): DoubleSupplier =
  ConstantDouble(this) - valueModule
operator fun Double.minus(valueModule: Vec2dToDouble): Vec2dToDouble =
  ConstantDouble(this) - valueModule
operator fun Double.minus(valueModule: Vec3dToDouble): Vec3dToDouble =
  ConstantDouble(this) - valueModule
