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
import kotlin.math.max

private data class Max(
  val source1: DoubleSupplier,
  val source2: DoubleSupplier
) : DoubleSupplier {
  override fun get(): Double = max(source1.get(), source2.get())
}

private data class Max2(
  val source1: Vec2dToDouble,
  val source2: Vec2dToDouble
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = max(source1[x, y], source2[x, y])
}

private data class Max3(
  val source1: Vec3dToDouble,
  val source2: Vec3dToDouble
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = max(source1[x, y, z], source2[x, y, z])
}

fun max(source1: DoubleSupplier, source2: DoubleSupplier): DoubleSupplier = Max(source1, source2)
fun max(source1: Vec2dToDouble, source2: Vec2dToDouble): Vec2dToDouble = Max2(source1, source2)
fun max(source1: Vec3dToDouble, source2: Vec3dToDouble): Vec3dToDouble = Max3(source1, source2)
fun max(source1: NoiseModule, source2: NoiseModule): NoiseModule = Max3(source1, source2).asNoiseModule()

fun max(source1: DoubleSupplier, value2: Double): DoubleSupplier = max(source1, ConstantDouble(value2))
fun max(value1: Double, source2: DoubleSupplier): DoubleSupplier = max(source2, value1)

fun max(source1: Vec2dToDouble, value2: Double): Vec2dToDouble = max(source1, ConstantDouble(value2))
fun max(value1: Double, source2: Vec2dToDouble): Vec2dToDouble = max(source2, value1)

fun max(source1: Vec3dToDouble, value2: Double): Vec3dToDouble = max(source1, ConstantDouble(value2))
fun max(value1: Double, source2: Vec3dToDouble): Vec3dToDouble = max(source2, value1)

fun max(source1: NoiseModule, value2: Double): NoiseModule = Max3(source1, ConstantDouble(value2)).asNoiseModule()
fun max(value1: Double, source2: NoiseModule): NoiseModule = max(source2, value1)
