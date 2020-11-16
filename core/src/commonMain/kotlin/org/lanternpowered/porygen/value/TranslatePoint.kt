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

private class TranslatePoint2(
    val source: Value2,
    val xOffset: Value2,
    val yOffset: Value2
) : Value2 {
  override fun get(x: Double, y: Double): Double {
    val ox = x + xOffset[x, y]
    val oy = y + yOffset[x, y]
    return source[ox, oy]
  }
}

private class TranslatePoint3(
    val source: Value3,
    val xOffset: Value3,
    val yOffset: Value3,
    val zOffset: Value3
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double {
    val ox = x + xOffset[x, y, z]
    val oy = y + yOffset[x, y, z]
    val oz = y + zOffset[x, y, z]
    return source[ox, oy, oz]
  }
}

fun Value2.translatePoint(x: Value2, y: Value2): Value2 =
    TranslatePoint2(this, x, y)

fun Value2.translatePoint(x: Value2, y: Double = 0.0): Value2 =
    TranslatePoint2(this, x, Constant(y))

fun Value2.translatePoint(x: Double = 0.0, y: Value2): Value2 =
    TranslatePoint2(this, Constant(x), y)

fun Value2.translatePoint(x: Double = 0.0, y: Double = 0.0): Value2 =
    TranslatePoint2(this, Constant(x), Constant(y))

fun Value3.translatePoint(x: Value3, y: Value3, z: Value3): Value3 =
    TranslatePoint3(this, x, y, z)

fun Value3.translatePoint(x: Double = 0.0, y: Value3, z: Value3): Value3 =
    TranslatePoint3(this, Constant(x), y, z)

fun Value3.translatePoint(x: Value3, y: Double = 0.0, z: Value3): Value3 =
    TranslatePoint3(this, x, Constant(y), z)

fun Value3.translatePoint(x: Double = 0.0, y: Double = 0.0, z: Value3): Value3 =
    TranslatePoint3(this, Constant(x), Constant(y), z)

fun Value3.translatePoint(x: Value3, y: Value3, z: Double = 0.0): Value3 =
    TranslatePoint3(this, x, y, Constant(z))

fun Value3.translatePoint(x: Double = 0.0, y: Value3, z: Double = 0.0): Value3 =
    TranslatePoint3(this, Constant(x), y, Constant(z))

fun Value3.translatePoint(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Value3 =
    TranslatePoint3(this, Constant(x), Constant(y), Constant(z))

fun Value3.translatePoint(x: Value3, y: Double = 0.0, z: Double = 0.0): Value3 =
    TranslatePoint3(this, x, Constant(y), Constant(z))

fun NoiseModule.translatePoint(x: Value3, y: Value3, z: Value3): NoiseModule =
    TranslatePoint3(this, x, y, z).asNoiseModule()

fun NoiseModule.translatePoint(x: Double = 0.0, y: Value3, z: Value3): NoiseModule =
    TranslatePoint3(this, Constant(x), y, z).asNoiseModule()

fun NoiseModule.translatePoint(x: Value3, y: Double = 0.0, z: Value3): NoiseModule =
    TranslatePoint3(this, x, Constant(y), z).asNoiseModule()

fun NoiseModule.translatePoint(x: Double = 0.0, y: Double = 0.0, z: Value3): NoiseModule =
    TranslatePoint3(this, Constant(x), Constant(y), z).asNoiseModule()

fun NoiseModule.translatePoint(x: Value3, y: Value3, z: Double = 0.0): NoiseModule =
    TranslatePoint3(this, x, y, Constant(z)).asNoiseModule()

fun NoiseModule.translatePoint(x: Double = 0.0, y: Value3, z: Double = 0.0): NoiseModule =
    TranslatePoint3(this, Constant(x), y, Constant(z)).asNoiseModule()

fun NoiseModule.translatePoint(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): NoiseModule =
    TranslatePoint3(this, Constant(x), Constant(y), Constant(z)).asNoiseModule()

fun NoiseModule.translatePoint(x: Value3, y: Double = 0.0, z: Double = 0.0): NoiseModule =
    TranslatePoint3(this, x, Constant(y), Constant(z)).asNoiseModule()
