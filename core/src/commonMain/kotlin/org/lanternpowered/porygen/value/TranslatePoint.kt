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
  val source: Vec2dToDouble,
  val xOffset: Vec2dToDouble,
  val yOffset: Vec2dToDouble,
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double {
    val ox = x + xOffset[x, y]
    val oy = y + yOffset[x, y]
    return source[ox, oy]
  }
}

private class TranslatePoint3(
  val source: Vec3dToDouble,
  val xOffset: Vec3dToDouble,
  val yOffset: Vec3dToDouble,
  val zOffset: Vec3dToDouble,
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double {
    val ox = x + xOffset[x, y, z]
    val oy = y + yOffset[x, y, z]
    val oz = z + zOffset[x, y, z]
    return source[ox, oy, oz]
  }
}

fun Vec2dToDouble.translatePoint(
  xTranslation: Vec2dToDouble,
  yTranslation: Vec2dToDouble,
): Vec2dToDouble =
  TranslatePoint2(this, xTranslation, yTranslation)

fun Vec2dToDouble.translatePoint(
  xTranslation: Vec2dToDouble,
  yTranslation: Double = 0.0
): Vec2dToDouble =
  TranslatePoint2(this, xTranslation, ConstantDouble(yTranslation))

fun Vec2dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Vec2dToDouble,
): Vec2dToDouble =
  TranslatePoint2(this, ConstantDouble(xTranslation), yTranslation)

fun Vec2dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Double = 0.0,
): Vec2dToDouble =
  TranslatePoint2(this, ConstantDouble(xTranslation), ConstantDouble(yTranslation))

fun Vec3dToDouble.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Vec3dToDouble,
  zTranslation: Vec3dToDouble,
): Vec3dToDouble =
  TranslatePoint3(this, xTranslation, yTranslation, zTranslation)

fun Vec3dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Vec3dToDouble,
  zTranslation: Vec3dToDouble,
): Vec3dToDouble =
  TranslatePoint3(this, ConstantDouble(xTranslation), yTranslation, zTranslation)

fun Vec3dToDouble.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Double = 0.0,
  zTranslation: Vec3dToDouble,
): Vec3dToDouble =
  TranslatePoint3(this, xTranslation, ConstantDouble(yTranslation), zTranslation)

fun Vec3dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Double = 0.0,
  zTranslation: Vec3dToDouble,
): Vec3dToDouble =
  TranslatePoint3(this, ConstantDouble(xTranslation), ConstantDouble(yTranslation), zTranslation)

fun Vec3dToDouble.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Vec3dToDouble,
  zTranslation: Double = 0.0,
): Vec3dToDouble =
  TranslatePoint3(this, xTranslation, yTranslation, ConstantDouble(zTranslation))

fun Vec3dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Vec3dToDouble,
  zTranslation: Double = 0.0,
): Vec3dToDouble =
  TranslatePoint3(this, ConstantDouble(xTranslation), yTranslation, ConstantDouble(zTranslation))

fun Vec3dToDouble.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Double = 0.0,
  zTranslation: Double = 0.0,
): Vec3dToDouble =
  TranslatePoint3(this, ConstantDouble(xTranslation), ConstantDouble(yTranslation), ConstantDouble(zTranslation))

fun Vec3dToDouble.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Double = 0.0,
  zTranslation: Double = 0.0,
): Vec3dToDouble =
  TranslatePoint3(this, xTranslation, ConstantDouble(yTranslation), ConstantDouble(zTranslation))

fun NoiseModule.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Vec3dToDouble,
  zTranslation: Vec3dToDouble,
): NoiseModule =
  TranslatePoint3(this, xTranslation, yTranslation, zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Vec3dToDouble,
  zTranslation: Vec3dToDouble,
): NoiseModule =
  TranslatePoint3(this, ConstantDouble(xTranslation), yTranslation, zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Double = 0.0,
  zTranslation: Vec3dToDouble,
): NoiseModule =
  TranslatePoint3(this, xTranslation, ConstantDouble(yTranslation), zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Double = 0.0,
  zTranslation: Vec3dToDouble,
): NoiseModule =
  TranslatePoint3(this, ConstantDouble(xTranslation), ConstantDouble(yTranslation), zTranslation).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Vec3dToDouble,
  zTranslation: Double = 0.0,
): NoiseModule =
  TranslatePoint3(this, xTranslation, yTranslation, ConstantDouble(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Vec3dToDouble,
  zTranslation: Double = 0.0,
): NoiseModule =
  TranslatePoint3(this, ConstantDouble(xTranslation), yTranslation, ConstantDouble(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Double = 0.0,
  yTranslation: Double = 0.0,
  zTranslation: Double = 0.0,
): NoiseModule =
  TranslatePoint3(this, ConstantDouble(xTranslation), ConstantDouble(yTranslation), ConstantDouble(zTranslation)).asNoiseModule()

fun NoiseModule.translatePoint(
  xTranslation: Vec3dToDouble,
  yTranslation: Double = 0.0,
  zTranslation: Double = 0.0,
): NoiseModule =
  TranslatePoint3(this, xTranslation, ConstantDouble(yTranslation), ConstantDouble(zTranslation)).asNoiseModule()
