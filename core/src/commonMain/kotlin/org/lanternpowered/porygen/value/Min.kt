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
    val source1: Value,
    val source2: Value
) : Value {
  override fun get(): Double = min(source1.get(), source2.get())
}

private data class Min2(
    val source1: Value2,
    val source2: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = min(source1[x, y], source2[x, y])
}

private data class Min3(
    val source1: Value3,
    val source2: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = min(source1[x, y, z], source2[x, y, z])
}

fun min(source1: Value, source2: Value): Value = Min(source1, source2)
fun min(source1: Value2, source2: Value2): Value2 = Min2(source1, source2)
fun min(source1: Value3, source2: Value3): Value3 = Min3(source1, source2)
fun min(source1: NoiseModule, source2: NoiseModule): Value3 = Min3(source1, source2).asNoiseModule()

fun min(source1: Value, value2: Double): Value = min(source1, Constant(value2))
fun min(value1: Double, source2: Value): Value = min(source2, value1)

fun min(source1: Value2, value2: Double): Value2 = min(source1, Constant(value2))
fun min(value1: Double, source2: Value2): Value2 = min(source2, value1)

fun min(source1: Value3, value2: Double): Value3 = min(source1, Constant(value2))
fun min(value1: Double, source2: Value3): Value3 = min(source2, value1)

fun min(source1: NoiseModule, value2: Double): NoiseModule = Min3(source1, Constant(value2)).asNoiseModule()
fun min(value1: Double, source2: NoiseModule): NoiseModule = min(source2, value1)
