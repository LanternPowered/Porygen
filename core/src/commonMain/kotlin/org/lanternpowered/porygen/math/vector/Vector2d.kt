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
data class Vector2d(
    val x: Double,
    val y: Double
) : Comparable<Vector2d> {

  val floorX: Int
    get() = floorToInt(x)

  val floorY: Int
    get() = floorToInt(y)

  val length: Double
    get() = sqrt(lengthSquared)

  val lengthSquared: Double
    get() = x * x + y * y

  operator fun div(value: Double): Vector2d =
      Vector2d(x / value, y / value)

  operator fun div(value: Vector2d): Vector2d =
      Vector2d(x / value.x, y / value.y)

  operator fun div(value: Vector2i): Vector2d =
      Vector2d(x / value.x, y / value.y)

  operator fun times(value: Double): Vector2d =
      Vector2d(x * value, y * value)

  operator fun times(value: Vector2d): Vector2d =
      Vector2d(x * value.x, y * value.y)

  operator fun times(value: Vector2i): Vector2d =
      Vector2d(x * value.x, y * value.y)

  operator fun plus(value: Vector2d): Vector2d =
      Vector2d(x + value.x, y + value.y)

  fun plus(x: Double, y: Double): Vector2d =
      Vector2d(this.x + x, this.y + y)

  operator fun plus(value: Vector2i): Vector2d =
      Vector2d(x + value.x, y + value.y)

  fun plus(x: Int, y: Int): Vector2d =
      Vector2d(this.x + x, this.y + y)

  operator fun minus(value: Vector2d): Vector2d =
      Vector2d(x - value.x, y - value.y)

  fun minus(x: Double, y: Double): Vector2d =
      Vector2d(this.x - x, this.y - y)

  operator fun minus(value: Vector2i): Vector2d =
      Vector2d(x - value.x, y - value.y)

  fun minus(x: Int, y: Int): Vector2d =
      Vector2d(this.x - x, this.y - y)

  operator fun unaryMinus(): Vector2d =
      Vector2d(-x, -y)

  operator fun unaryPlus(): Vector2d = this

  fun dot(v: Vector2d): Double = dot(v.x, v.y)

  fun dot(x: Double, y: Double): Double = this.x * x + this.y * y

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param vector The other vector
   * @return The cross product
   */
  fun cross(vector: Vector2i): Double = cross(vector.x.toDouble(), vector.y.toDouble())

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param vector The other vector
   * @return The cross product
   */
  fun cross(vector: Vector2d): Double = cross(vector.x, vector.y)

  /**
   * Computes the 2D pseudo cross product of this and the given vector.
   *
   * @param x The other x coordinate
   * @param y The other y coordinate
   * @return The cross product
   */
  fun cross(x: Double, y: Double): Double =
      this.y * x - this.x * y

  fun toInt(): Vector2i =
      Vector2i(x.toInt(), y.toInt())

  fun floorToInt(): Vector2i =
      Vector2i(floorToInt(x), floorToInt(y))

  override fun compareTo(other: Vector2d): Int =
      sign(lengthSquared - other.lengthSquared).toInt()

  override fun toString(): String = "($x, $y)"

  companion object {

    val ONE = Vector2d(1.0, 1.0)
    val ZERO = Vector2d(0.0, 0.0)
  }
}
