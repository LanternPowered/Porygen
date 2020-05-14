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

import kotlin.math.sign
import kotlin.math.sqrt

data class Vector2i(
    val x: Int,
    val y: Int
) : Comparable<Vector2i> {

  val length: Double
    get() = sqrt(lengthSquared.toDouble())

  val lengthSquared: Int
    get() = x * x + y * y

  operator fun times(value: Int): Vector2i =
      Vector2i(x * value, y * value)

  operator fun times(value: Vector2i): Vector2i =
      Vector2i(x * value.x, y * value.x)

  operator fun times(value: Double): Vector2d =
      Vector2d(x * value, y * value)

  operator fun times(value: Vector2d): Vector2d =
      Vector2d(x * value.x, y * value.x)

  operator fun plus(value: Vector2i): Vector2i =
      Vector2i(x + value.x, y + value.y)

  operator fun minus(value: Vector2i): Vector2i =
      Vector2i(x - value.x, y - value.y)

  operator fun unaryMinus(): Vector2i =
      Vector2i(-x, -y)

  operator fun unaryPlus(): Vector2i = this

  fun toDouble(): Vector2d =
      Vector2d(x.toDouble(), y.toDouble())

  override fun compareTo(other: Vector2i): Int =
      sign((lengthSquared - other.lengthSquared).toDouble()).toInt()

  companion object {

    val ONE = Vector2i(1, 1)
    val ZERO = Vector2i(0, 0)
  }
}
