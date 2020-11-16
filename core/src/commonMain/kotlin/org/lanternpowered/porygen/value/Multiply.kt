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
    val source1: Value,
    val source2: Value
) : Value {
  override fun get(): Double = source1.get() * source2.get()
}

private data class Multiply2(
    val source1: Value2,
    val source2: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = source1[x, y] * source2[x, y]
}

private data class Multiply3(
    val source1: Value3,
    val source2: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = source1[x, y, z] * source2[x, y, z]
}

operator fun Value.times(valueModule: Value): Value = Multiply(this, valueModule)
operator fun Value.times(value: Double): Value = this * Constant(value)
operator fun Value.times(valueModule: NoiseModule): NoiseModule = Multiply3(this, valueModule).asNoiseModule()

operator fun Value2.times(valueModule: Value2): Value2 = Multiply2(this, valueModule)
operator fun Value2.times(value: Double): Value2 = this * Constant(value)

operator fun Value3.times(valueModule: Value3): Value3 = Multiply3(this, valueModule)
operator fun Value3.times(value: Double): Value3 = this * Constant(value)

operator fun NoiseModule.times(valueModule: Value): NoiseModule = Multiply3(this, valueModule).asNoiseModule()
operator fun NoiseModule.times(valueModule: Value3): NoiseModule = Multiply3(this, valueModule).asNoiseModule()

operator fun Double.times(valueModule: Value): Value = Constant(this) * valueModule
operator fun Double.times(valueModule: Value2): Value2 = Constant(this) * valueModule
operator fun Double.times(valueModule: Value3): Value3 = Constant(this) * valueModule

operator fun NoiseModule.times(value: Double): NoiseModule = (this * Constant(value)).asNoiseModule()

operator fun Value.unaryMinus(): Value = this * -1.0
operator fun Value2.unaryMinus(): Value2 = this * -1.0
operator fun Value3.unaryMinus(): Value3 = this * -1.0
operator fun Value.unaryPlus(): Value = this
operator fun Value2.unaryPlus(): Value2 = this
operator fun Value3.unaryPlus(): Value3 = this
