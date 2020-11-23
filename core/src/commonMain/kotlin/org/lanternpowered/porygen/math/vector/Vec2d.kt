/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.math.vector

import kotlinx.serialization.Serializable
import org.lanternpowered.porygen.math.floorToInt
import kotlin.math.sign
import kotlin.math.sqrt

@Serializable
data class Vec2d(
    val x: Double,
    val y: Double
) : Comparable<Vec2d> {

  val floorX: Int
    get() = floorToInt(x)

  val floorY: Int
    get() = floorToInt(y)

  val length: Double
    get() = sqrt(lengthSquared)

  val lengthSquared: Double
    get() = x * x + y * y

  operator fun div(value: Double): Vec2d =
      Vec2d(x / value, y / value)

  operator fun div(value: Vec2d): Vec2d =
      Vec2d(x / value.x, y / value.y)

  operator fun div(value: Vec2i): Vec2d =
      Vec2d(x / value.x, y / value.y)

  operator fun times(value: Double): Vec2d =
      Vec2d(x * value, y * value)

  operator fun times(value: Vec2d): Vec2d =
      Vec2d(x * value.x, y * value.y)

  operator fun times(value: Vec2i): Vec2d =
      Vec2d(x * value.x, y * value.y)

  operator fun plus(value: Vec2d): Vec2d =
      Vec2d(x + value.x, y + value.y)

  fun plus(x: Double, y: Double): Vec2d =
      Vec2d(this.x + x, this.y + y)

  operator fun plus(value: Vec2i): Vec2d =
      Vec2d(x + value.x, y + value.y)

  fun plus(x: Int, y: Int): Vec2d =
      Vec2d(this.x + x, this.y + y)

  operator fun minus(value: Vec2d): Vec2d =
      Vec2d(x - value.x, y - value.y)

  fun minus(x: Double, y: Double): Vec2d =
      Vec2d(this.x - x, this.y - y)

  operator fun minus(value: Vec2i): Vec2d =
      Vec2d(x - value.x, y - value.y)

  fun minus(x: Int, y: Int): Vec2d =
      Vec2d(this.x - x, this.y - y)

  operator fun unaryMinus(): Vec2d =
      Vec2d(-x, -y)

  operator fun unaryPlus(): Vec2d = this

  fun dot(v: Vec2d): Double = dot(v.x, v.y)

  fun dot(x: Double, y: Double): Double = this.x * x + this.y * y

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param vector The other vector
   * @return The cross product
   */
  fun cross(vector: Vec2i): Double = cross(vector.x.toDouble(), vector.y.toDouble())

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param vector The other vector
   * @return The cross product
   */
  fun cross(vector: Vec2d): Double = cross(vector.x, vector.y)

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param x The other x coordinate
   * @param y The other y coordinate
   * @return The cross product
   */
  fun cross(x: Double, y: Double): Double =
      this.y * x - this.x * y

  fun toInt(): Vec2i =
      Vec2i(x.toInt(), y.toInt())

  fun floorToInt(): Vec2i =
      Vec2i(floorToInt(x), floorToInt(y))

  override fun compareTo(other: Vec2d): Int =
      sign(lengthSquared - other.lengthSquared).toInt()

  override fun toString(): String = "($x, $y)"

  companion object {

    val One = Vec2d(1.0, 1.0)
    val Zero = Vec2d(0.0, 0.0)
  }
}
