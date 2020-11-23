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
import kotlin.math.pow

private data class Power(
  val source: DoubleSupplier,
  val exponent: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double = source.get().pow(exponent.get())
}

private data class Power2(
  val source: Vec2dToDouble,
  val exponent: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = source[x, y].pow(exponent[x, y])
}

private data class Power3(
  val source: Vec3dToDouble,
  val exponent: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = source[x, y, z].pow(exponent[x, y, z])
}

fun DoubleSupplier.pow(exponent: DoubleSupplier): DoubleSupplier = Power(this, exponent)
fun DoubleSupplier.pow(exponent: Double): DoubleSupplier = pow(ConstantDouble(exponent))

fun Vec2dToDouble.pow(exponent: Vec2dToDouble): Vec2dToDouble = Power2(this, exponent)
fun Vec2dToDouble.pow(exponent: Double): Vec2dToDouble = pow(ConstantDouble(exponent))

fun Vec3dToDouble.pow(exponent: Vec3dToDouble): Vec3dToDouble = Power3(this, exponent)
fun Vec3dToDouble.pow(exponent: Double): Vec3dToDouble = pow(ConstantDouble(exponent))

fun NoiseModule.pow(exponent: Vec3dToDouble): NoiseModule = Power3(this, exponent).asNoiseModule()
fun NoiseModule.pow(exponent: Double): NoiseModule = Power3(this, ConstantDouble(exponent)).asNoiseModule()
