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
    val source1: Value,
    val source2: Value
) : Value {
  override fun get(): Double = source1.get() - source2.get()
}

private data class Subtract2(
    val source1: Value2,
    val source2: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = source1[x, y] - source2[x, y]
}

private data class Subtract3(
    val source1: Value3,
    val source2: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = source1[x, y, z] - source2[x, y, z]
}

operator fun Value.minus(valueModule: Value): Value = Subtract(this, valueModule)
operator fun Value.minus(value: Double): Value = this - Constant(value)

operator fun Value2.minus(valueModule: Value2): Value2 = Subtract2(this, valueModule)
operator fun Value2.minus(value: Double): Value2 = this - Constant(value)

operator fun Value3.minus(valueModule: Value3): Value3 = Subtract3(this, valueModule)
operator fun Value3.minus(value: Double): Value3 = this - Constant(value)

operator fun NoiseModule.minus(valueModule: Value): NoiseModule = Subtract3(this, valueModule).asNoiseModule()
operator fun NoiseModule.minus(valueModule: Value3): NoiseModule = Subtract3(this, valueModule).asNoiseModule()
operator fun NoiseModule.minus(value: Double): NoiseModule = (this - Constant(value)).asNoiseModule()

operator fun Double.minus(valueModule: Value): Value = Constant(this) - valueModule
operator fun Double.minus(valueModule: Value2): Value2 = Constant(this) - valueModule
operator fun Double.minus(valueModule: Value3): Value3 = Constant(this) - valueModule
