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

private data class Multiply(
  val source1: DoubleSupplier,
  val source2: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double = source1.get() * source2.get()
}

private data class Multiply2(
  val source1: Vec2dToDouble,
  val source2: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = source1[x, y] * source2[x, y]
}

private data class Multiply3(
  val source1: Vec3dToDouble,
  val source2: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = source1[x, y, z] * source2[x, y, z]
}

operator fun DoubleSupplier.times(valueModule: DoubleSupplier): DoubleSupplier =
  Multiply(this, valueModule)
operator fun DoubleSupplier.times(value: Double): DoubleSupplier =
  this * ConstantDouble(value)
operator fun DoubleSupplier.times(valueModule: NoiseModule): NoiseModule =
  Multiply3(this, valueModule).asNoiseModule()

operator fun Vec2dToDouble.times(valueModule: Vec2dToDouble): Vec2dToDouble =
  Multiply2(this, valueModule)
operator fun Vec2dToDouble.times(value: Double): Vec2dToDouble =
  this * ConstantDouble(value)

operator fun Vec3dToDouble.times(valueModule: Vec3dToDouble): Vec3dToDouble =
  Multiply3(this, valueModule)
operator fun Vec3dToDouble.times(value: Double): Vec3dToDouble =
  this * ConstantDouble(value)

operator fun NoiseModule.times(valueModule: DoubleSupplier): NoiseModule =
  Multiply3(this, valueModule).asNoiseModule()
operator fun NoiseModule.times(valueModule: Vec3dToDouble): NoiseModule =
  Multiply3(this, valueModule).asNoiseModule()

operator fun Double.times(valueModule: DoubleSupplier): DoubleSupplier =
  ConstantDouble(this) * valueModule
operator fun Double.times(valueModule: Vec2dToDouble): Vec2dToDouble =
  ConstantDouble(this) * valueModule
operator fun Double.times(valueModule: Vec3dToDouble): Vec3dToDouble =
  ConstantDouble(this) * valueModule

operator fun NoiseModule.times(value: Double): NoiseModule =
  (this * ConstantDouble(value)).asNoiseModule()

operator fun DoubleSupplier.unaryMinus(): DoubleSupplier = this * -1.0
operator fun Vec2dToDouble.unaryMinus(): Vec2dToDouble = this * -1.0
operator fun Vec3dToDouble.unaryMinus(): Vec3dToDouble = this * -1.0
operator fun DoubleSupplier.unaryPlus(): DoubleSupplier = this
operator fun Vec2dToDouble.unaryPlus(): Vec2dToDouble = this
operator fun Vec3dToDouble.unaryPlus(): Vec3dToDouble = this
