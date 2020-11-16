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

data class Abs(val source: Value) : Value {
  override fun get(): Double = abs(source.get())
}

data class Abs2(val source: Value2) : Value2 {
  override fun get(x: Double, y: Double): Double = abs(source[x, y])
}

data class Abs3(val source: Value3) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = abs(source[x, y, z])
}

fun abs(source: Value): Value = Abs(source)
fun abs(source: Value2): Value2 = Abs2(source)
fun abs(source: Value3): Value3 = Abs3(source)
fun abs(source: NoiseModule): NoiseModule = Abs3(source).asNoiseModule()
