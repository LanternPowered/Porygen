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
data class Vec2i(
  val x: Int,
  val y: Int,
) : Comparable<Vec2i> {

  val length: Double
    get() = sqrt(this.lengthSquared.toDouble())

  val lengthSquared: Int
    get() = this.x * this.x + this.y * this.y

  operator fun div(value: Int): Vec2d =
    Vec2d(this.x / value.toDouble(), this.y / value.toDouble())

  operator fun div(value: Vec2i): Vec2d =
    Vec2d(this.x / value.x.toDouble(), this.y / value.y.toDouble())

  operator fun div(value: Double): Vec2d =
    Vec2d(this.x / value, this.y / value)

  operator fun div(value: Vec2d): Vec2d =
    Vec2d(this.x / value.x, this.y / value.y)

  operator fun times(value: Int): Vec2i =
    Vec2i(this.x * value, this.y * value)

  operator fun times(value: Vec2i): Vec2i =
    Vec2i(this.x * value.x, this.y * value.y)

  operator fun times(value: Double): Vec2d =
    Vec2d(this.x * value, this.y * value)

  operator fun times(value: Vec2d): Vec2d =
    Vec2d(this.x * value.x, this.y * value.y)

  operator fun plus(value: Vec2i): Vec2i =
    Vec2i(this.x + value.x, this.y + value.y)

  operator fun minus(value: Vec2i): Vec2i =
    Vec2i(this.x - value.x, this.y - value.y)

  operator fun unaryMinus(): Vec2i =
    Vec2i(-this.x, -this.y)

  operator fun unaryPlus(): Vec2i = this

  fun toDouble(): Vec2d =
    Vec2d(this.x.toDouble(), this.y.toDouble())

  override fun compareTo(other: Vec2i): Int =
    sign((this.lengthSquared - other.lengthSquared).toDouble()).toInt()

  override fun toString(): String = "($x, $y)"

  companion object {

    val One = Vec2i(1, 1)
    val Zero = Vec2i(0, 0)
  }
}
