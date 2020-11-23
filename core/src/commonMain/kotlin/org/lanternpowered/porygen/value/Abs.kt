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
import kotlin.math.abs

data class Abs(val source: DoubleSupplier) : DoubleSupplier {
  override fun get(): Double = abs(source.get())
}

data class Abs2(val source: Vec2dToDouble) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double = abs(source[x, y])
}

data class Abs3(val source: Vec3dToDouble) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double = abs(source[x, y, z])
}

fun abs(source: DoubleSupplier): DoubleSupplier = Abs(source)
fun abs(source: Vec2dToDouble): Vec2dToDouble = Abs2(source)
fun abs(source: Vec3dToDouble): Vec3dToDouble = Abs3(source)
fun abs(source: NoiseModule): NoiseModule = Abs3(source).asNoiseModule()
