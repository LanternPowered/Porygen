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

private data class Add(
    val source1: Value,
    val source2: Value
) : Value {
  override fun get(): Double = source1.get() + source2.get()
}

private data class Add2(
    val source1: Value2,
    val source2: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double = source1[x, y] + source2[x, y]
}

private data class Add3(
    val source1: Value3,
    val source2: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double = source1[x, y, z] + source2[x, y, z]
}

operator fun Value.plus(valueModule: Value): Value = Add(this, valueModule)
operator fun Value.plus(value: Double): Value = this + Constant(value)

operator fun Value2.plus(valueModule: Value2): Value2 = Add2(this, valueModule)
operator fun Value2.plus(value: Double): Value2 = this + Constant(value)

operator fun Value3.plus(valueModule: Value3): Value3 = Add3(this, valueModule)
operator fun Value3.plus(value: Double): Value3 = this + Constant(value)

operator fun NoiseModule.plus(valueModule: Value): NoiseModule = Add3(this, valueModule).asNoiseModule()
operator fun NoiseModule.plus(valueModule: Value3): NoiseModule = Add3(this, valueModule).asNoiseModule()
operator fun NoiseModule.plus(value: Double): NoiseModule = (this + Constant(value)).asNoiseModule()

operator fun Double.plus(valueModule: Value): Value = Constant(this) + valueModule
operator fun Double.plus(valueModule: Value2): Value2 = Constant(this) + valueModule
operator fun Double.plus(valueModule: Value3): Value3 = Constant(this) + valueModule
