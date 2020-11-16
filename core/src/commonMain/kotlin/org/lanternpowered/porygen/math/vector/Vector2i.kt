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
import kotlin.math.sign
import kotlin.math.sqrt

@Serializable
data class Vector2i(
    val x: Int,
    val y: Int
) : Comparable<Vector2i> {

  val length: Double
    get() = sqrt(this.lengthSquared.toDouble())

  val lengthSquared: Int
    get() = this.x * this.x + this.y * this.y

  operator fun div(value: Int): Vector2d =
      Vector2d(this.x / value.toDouble(), this.y / value.toDouble())

  operator fun div(value: Vector2i): Vector2d =
      Vector2d(this.x / value.x.toDouble(), this.y / value.y.toDouble())

  operator fun div(value: Double): Vector2d =
      Vector2d(this.x / value, this.y / value)

  operator fun div(value: Vector2d): Vector2d =
      Vector2d(this.x / value.x, this.y / value.y)

  operator fun times(value: Int): Vector2i =
      Vector2i(this.x * value, this.y * value)

  operator fun times(value: Vector2i): Vector2i =
      Vector2i(this.x * value.x, this.y * value.y)

  operator fun times(value: Double): Vector2d =
      Vector2d(this.x * value, this.y * value)

  operator fun times(value: Vector2d): Vector2d =
      Vector2d(this.x * value.x, this.y * value.y)

  operator fun plus(value: Vector2i): Vector2i =
      Vector2i(this.x + value.x, this.y + value.y)

  operator fun minus(value: Vector2i): Vector2i =
      Vector2i(this.x - value.x, this.y - value.y)

  operator fun unaryMinus(): Vector2i =
      Vector2i(-this.x, -this.y)

  operator fun unaryPlus(): Vector2i = this

  fun toDouble(): Vector2d =
      Vector2d(this.x.toDouble(), this.y.toDouble())

  override fun compareTo(other: Vector2i): Int =
      sign((this.lengthSquared - other.lengthSquared).toDouble()).toInt()

  override fun toString(): String = "($x, $y)"

  companion object {

    val ONE = Vector2i(1, 1)
    val ZERO = Vector2i(0, 0)
  }
}
