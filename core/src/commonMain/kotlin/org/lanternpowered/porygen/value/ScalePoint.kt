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

import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.math.vector.Vec3d
import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.asNoiseModule

private class ScalePoint2(
  val source: Vec2dToDouble,
  val xScale: Double,
  val yScale: Double
) : Vec2dToDouble {
  override fun get(x: Double, y: Double): Double {
    val ox = x * xScale
    val oy = y * yScale
    return source[ox, oy]
  }
}

private class ScalePoint3(
  val source: Vec3dToDouble,
  val xScale: Double,
  val yScale: Double,
  val zScale: Double
) : Vec3dToDouble {
  override fun get(x: Double, y: Double, z: Double): Double {
    val ox = x * xScale
    val oy = y * yScale
    val oz = z * zScale
    return source[ox, oy, oz]
  }
}

fun Vec2dToDouble.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0): Vec2dToDouble =
    ScalePoint2(this, xScale, yScale)

fun Vec2dToDouble.scalePoint(scale: Vec2d): Vec2dToDouble =
    ScalePoint2(this, scale.x, scale.y)

fun Vec2dToDouble.scalePoint(scale: Double): Vec2dToDouble =
    ScalePoint2(this, scale, scale)

fun Vec3dToDouble.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0, zScale: Double = 1.0): Vec3dToDouble =
    ScalePoint3(this, xScale, yScale, zScale)

fun Vec3dToDouble.scalePoint(scale: Double): Vec3dToDouble =
    ScalePoint3(this, scale, scale, scale)

fun Vec3dToDouble.scalePoint(scale: Vec3d): Vec3dToDouble =
    ScalePoint3(this, scale.x, scale.y, scale.z)

fun NoiseModule.scalePoint(xScale: Double = 1.0, yScale: Double = 1.0, zScale: Double = 1.0): NoiseModule =
    ScalePoint3(this, xScale, yScale, zScale).asNoiseModule()

fun NoiseModule.scalePoint(scale: Double): NoiseModule =
    ScalePoint3(this, scale, scale, scale).asNoiseModule()

fun NoiseModule.scalePoint(scale: Vec3d): NoiseModule =
    ScalePoint3(this, scale.x, scale.y, scale.z).asNoiseModule()
