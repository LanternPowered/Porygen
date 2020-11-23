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
data class Vec3i(
    val x: Int,
    val y: Int,
    val z: Int
) : Comparable<Vec3i> {

  val length: Double
    get() = sqrt(lengthSquared.toDouble())

  val lengthSquared: Int
    get() = x * x + y * y + z * z

  operator fun div(value: Vec3i): Vec3d =
      Vec3d(this.x / value.x.toDouble(), this.y / value.y.toDouble(), this.z / value.z.toDouble())

  operator fun div(value: Int): Vec3d =
      Vec3d(this.x / value.toDouble(), this.y / value.toDouble(), this.z / value.toDouble())

  operator fun div(value: Vec3d): Vec3d =
      Vec3d(this.x / value.x, this.y / value.y, this.z / value.z)

  operator fun div(value: Double): Vec3d =
      Vec3d(this.x / value, this.y / value, this.z / value)

  operator fun times(value: Vec3d): Vec3d =
      Vec3d(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun times(value: Double): Vec3d =
      Vec3d(this.x * value, this.y * value, this.z * value)

  operator fun times(value: Vec3i): Vec3i =
      Vec3i(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun times(value: Int): Vec3i =
      Vec3i(this.x * value, this.y * value, this.z * value)

  operator fun plus(value: Vec3d): Vec3d =
      Vec3d(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Double, y: Double, z: Double): Vec3d =
      Vec3d(this.x + x, this.y + y, this.z + z)

  operator fun plus(value: Vec3i): Vec3i =
      Vec3i(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Int, y: Int, z: Int): Vec3i =
      Vec3i(this.x + x, this.y + y, this.z + z)

  operator fun minus(value: Vec3d): Vec3d =
      Vec3d(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Double, y: Double, z: Double): Vec3d =
      Vec3d(this.x - x, this.y - y, this.z - z)

  operator fun minus(value: Vec3i): Vec3i =
      Vec3i(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Int, y: Int, z: Int): Vec3i =
      Vec3i(this.x - x, this.y - y, this.z - z)

  operator fun unaryMinus(): Vec3i =
      Vec3i(-x, -y, -z)

  operator fun unaryPlus(): Vec3i = this

  fun toDouble(): Vec3d =
      Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

  override fun compareTo(other: Vec3i): Int =
      sign((lengthSquared - other.lengthSquared).toDouble()).toInt()

  override fun toString(): String = "($x, $y, $z)"

  companion object {

    val One = Vec3i(1, 1, 1)
    val Zero = Vec3i(0, 0, 0)
  }
}
