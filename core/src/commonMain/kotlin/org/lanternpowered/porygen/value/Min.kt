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
import kotlin.math.min

private data class Min(
  val source1: DoubleSupplier,
  val source2: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double = min(source1.get(), source2.get())
}

private data class Min2(
  val source1: Vec2dToDouble,
  val source2: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = min(source1[x, y], source2[x, y])
}

private data class Min3(
  val source1: Vec3dToDouble,
  val source2: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = min(source1[x, y, z], source2[x, y, z])
}

fun min(source1: DoubleSupplier, source2: DoubleSupplier): DoubleSupplier = Min(source1, source2)
fun min(source1: Vec2dToDouble, source2: Vec2dToDouble): Vec2dToDouble = Min2(source1, source2)
fun min(source1: Vec3dToDouble, source2: Vec3dToDouble): Vec3dToDouble = Min3(source1, source2)
fun min(source1: NoiseModule, source2: NoiseModule): NoiseModule = Min3(source1, source2).asNoiseModule()

fun min(source1: DoubleSupplier, value2: Double): DoubleSupplier = min(source1, ConstantDouble(value2))
fun min(value1: Double, source2: DoubleSupplier): DoubleSupplier = min(source2, value1)

fun min(source1: Vec2dToDouble, value2: Double): Vec2dToDouble = min(source1, ConstantDouble(value2))
fun min(value1: Double, source2: Vec2dToDouble): Vec2dToDouble = min(source2, value1)

fun min(source1: Vec3dToDouble, value2: Double): Vec3dToDouble = min(source1, ConstantDouble(value2))
fun min(value1: Double, source2: Vec3dToDouble): Vec3dToDouble = min(source2, value1)

fun min(source1: NoiseModule, value2: Double): NoiseModule = Min3(source1, ConstantDouble(value2)).asNoiseModule()
fun min(value1: Double, source2: NoiseModule): NoiseModule = min(source2, value1)
