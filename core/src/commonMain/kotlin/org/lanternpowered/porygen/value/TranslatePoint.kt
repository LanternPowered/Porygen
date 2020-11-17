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
    val oz = z + zOffset[x, y, z]
    return source[ox, oy, oz]
  }
}

fun Value2.translatePoint(
    xTranslation: Value2,
    yTranslation: Value2
): Value2 =
    TranslatePoint2(this, xTranslation, yTranslation)

fun Value2.translatePoint(
    xTranslation: Value2,
    yTranslation: Double = 0.0
): Value2 =
    TranslatePoint2(this, xTranslation, Constant(yTranslation))

fun Value2.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Value2
): Value2 =
    TranslatePoint2(this, Constant(xTranslation), yTranslation)

fun Value2.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Double = 0.0
): Value2 =
    TranslatePoint2(this, Constant(xTranslation), Constant(yTranslation))

fun Value3.translatePoint(
    xTranslation: Value3,
    yTranslation: Value3,
    zTranslation: Value3
): Value3 =
    TranslatePoint3(this, xTranslation, yTranslation, zTranslation)

fun Value3.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Value3,
    zTranslation: Value3
): Value3 =
    TranslatePoint3(this, Constant(xTranslation), yTranslation, zTranslation)

fun Value3.translatePoint(
    xTranslation: Value3,
    yTranslation: Double = 0.0,
    zTranslation: Value3
): Value3 =
    TranslatePoint3(this, xTranslation, Constant(yTranslation), zTranslation)

fun Value3.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Double = 0.0,
    zTranslation: Value3
): Value3 =
    TranslatePoint3(this, Constant(xTranslation), Constant(yTranslation), zTranslation)

fun Value3.translatePoint(
    xTranslation: Value3,
    yTranslation: Value3,
    zTranslation: Double = 0.0
): Value3 =
    TranslatePoint3(this, xTranslation, yTranslation, Constant(zTranslation))

fun Value3.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Value3,
    zTranslation: Double = 0.0
): Value3 =
    TranslatePoint3(this, Constant(xTranslation), yTranslation, Constant(zTranslation))

fun Value3.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Double = 0.0,
    zTranslation: Double = 0.0
): Value3 =
    TranslatePoint3(this, Constant(xTranslation), Constant(yTranslation), Constant(zTranslation))

fun Value3.translatePoint(
    xTranslation: Value3,
    yTranslation: Double = 0.0,
    zTranslation: Double = 0.0
): Value3 =
    TranslatePoint3(this, xTranslation, Constant(yTranslation), Constant(zTranslation))

fun NoiseModule.translatePoint(
    xTranslation: Value3,
    yTranslation: Value3,
    zTranslation: Value3
): NoiseModule =
    TranslatePoint3(this, xTranslation, yTranslation, zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Value3,
    zTranslation: Value3
): NoiseModule =
    TranslatePoint3(this, Constant(xTranslation), yTranslation, zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Value3,
    yTranslation: Double = 0.0,
    zTranslation: Value3
): NoiseModule =
    TranslatePoint3(this, xTranslation, Constant(yTranslation), zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Double = 0.0,
    zTranslation: Value3
): NoiseModule =
    TranslatePoint3(this, Constant(xTranslation), Constant(yTranslation), zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Value3,
    yTranslation: Value3,
    zTranslation: Double = 0.0
): NoiseModule =
    TranslatePoint3(this, xTranslation, yTranslation, Constant(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Value3,
    zTranslation: Double = 0.0
): NoiseModule =
    TranslatePoint3(this, Constant(xTranslation), yTranslation, Constant(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Double = 0.0,
    yTranslation: Double = 0.0,
    zTranslation: Double = 0.0
): NoiseModule =
    TranslatePoint3(this, Constant(xTranslation), Constant(yTranslation), Constant(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
    xTranslation: Value3,
    yTranslation: Double = 0.0,
    zTranslation: Double = 0.0
): NoiseModule =
    TranslatePoint3(this, xTranslation, Constant(yTranslation), Constant(zTranslation)).asNoiseModule()
