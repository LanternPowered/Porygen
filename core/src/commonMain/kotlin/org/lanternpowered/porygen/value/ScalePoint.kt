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

import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector3d
import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.asNoiseModule

private class ScalePoint2(
    val source: Value2,
    val xScale: Double,
    val yScale: Double
) : Value2 {
  override fun get(x: Double, y: Double): Double {
    val ox = x * xScale
    val oy = y * yScale
    return source[ox, oy]
  }
}

private class ScalePoint3(
    val source: Value3,
    val xScale: Double,
    val yScale: Double,
    val zScale: Double
) : Value3 {
  override fun get(x: Double, y: Double, z: Double): Double {
    val ox = x * xScale
    val oy = y * yScale
    val oz = z * zScale
    return source[ox, oy, oz]
  }
}

fun Value2.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0): Value2 =
    ScalePoint2(this, xScale, yScale)

fun Value2.scalePoint(scale: Vector2d): Value2 =
    ScalePoint2(this, scale.x, scale.y)

fun Value2.scalePoint(scale: Double): Value2 =
    ScalePoint2(this, scale, scale)

fun Value3.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0, zScale: Double = 1.0): Value3 =
    ScalePoint3(this, xScale, yScale, zScale)

fun Value3.scalePoint(scale: Double): Value3 =
    ScalePoint3(this, scale, scale, scale)

fun Value3.scalePoint(scale: Vector3d): Value3 =
    ScalePoint3(this, scale.x, scale.y, scale.z)

fun NoiseModule.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0, zScale: Double = 1.0): NoiseModule =
    ScalePoint3(this, xScale, yScale, zScale).asNoiseModule()

fun NoiseModule.scalePoint(scale: Double): NoiseModule =
    ScalePoint3(this, scale, scale, scale).asNoiseModule()

fun NoiseModule.scalePoint(scale: Vector3d): NoiseModule =
    ScalePoint3(this, scale.x, scale.y, scale.z).asNoiseModule()
