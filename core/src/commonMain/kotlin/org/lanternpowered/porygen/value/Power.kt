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
    val source: Value,
    val exponent: Value
) : Value {
  override fun get(): Double = source.get().pow(exponent.get())
}

private data class Power2(
    val source: Value2,
    val exponent: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = source[x, y].pow(exponent[x, y])
}

private data class Power3(
    val source: Value3,
    val exponent: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = source[x, y, z].pow(exponent[x, y, z])
}

fun Value.pow(exponent: Value): Value = Power(this, exponent)
fun Value.pow(exponent: Double): Value = pow(Constant(exponent))

fun Value2.pow(exponent: Value2): Value2 = Power2(this, exponent)
fun Value2.pow(exponent: Double): Value2 = pow(Constant(exponent))

fun Value3.pow(exponent: Value3): Value3 = Power3(this, exponent)
fun Value3.pow(exponent: Double): Value3 = pow(Constant(exponent))

fun NoiseModule.pow(exponent: Value3): NoiseModule = Power3(this, exponent).asNoiseModule()
fun NoiseModule.pow(exponent: Double): NoiseModule = Power3(this, Constant(exponent)).asNoiseModule()
