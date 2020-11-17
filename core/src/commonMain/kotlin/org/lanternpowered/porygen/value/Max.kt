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
    val source1: Value,
    val source2: Value
) : Value {
  override fun get(): Double = max(source1.get(), source2.get())
}

private data class Max2(
    val source1: Value2,
    val source2: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = max(source1[x, y], source2[x, y])
}

private data class Max3(
    val source1: Value3,
    val source2: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = max(source1[x, y, z], source2[x, y, z])
}

fun max(source1: Value, source2: Value): Value = Max(source1, source2)
fun max(source1: Value2, source2: Value2): Value2 = Max2(source1, source2)
fun max(source1: Value3, source2: Value3): Value3 = Max3(source1, source2)
fun max(source1: NoiseModule, source2: NoiseModule): NoiseModule = Max3(source1, source2).asNoiseModule()

fun max(source1: Value, value2: Double): Value = max(source1, Constant(value2))
fun max(value1: Double, source2: Value): Value = max(source2, value1)

fun max(source1: Value2, value2: Double): Value2 = max(source1, Constant(value2))
fun max(value1: Double, source2: Value2): Value2 = max(source2, value1)

fun max(source1: Value3, value2: Double): Value3 = max(source1, Constant(value2))
fun max(value1: Double, source2: Value3): Value3 = max(source2, value1)

fun max(source1: NoiseModule, value2: Double): NoiseModule = Max3(source1, Constant(value2)).asNoiseModule()
fun max(value1: Double, source2: NoiseModule): NoiseModule = max(source2, value1)
