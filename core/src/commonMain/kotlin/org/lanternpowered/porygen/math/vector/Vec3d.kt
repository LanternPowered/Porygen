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
data class Vec3d(
    val x: Double,
    val y: Double,
    val z: Double
) : Comparable<Vec3d> {

  val floorX: Int
    get() = floorToInt(this.x)

  val floorY: Int
    get() = floorToInt(this.y)

  val floorZ: Int
    get() = floorToInt(this.z)

  val length: Double
    get() = sqrt(this.lengthSquared)

  val lengthSquared: Double
    get() = this.x * this.x + this.y * this.y + this.z * this.z

  operator fun div(value: Double): Vec3d =
      Vec3d(this.x / value, this.y / value, this.z / value)

  operator fun div(value: Vec3d): Vec3d =
      Vec3d(this.x / value.x, this.y / value.y, this.z / value.z)

  operator fun div(value: Vec3i): Vec3d =
      Vec3d(this.x / value.x, this.y / value.y, this.z / value.z)

  operator fun times(value: Double): Vec3d =
      Vec3d(this.x * value, this.y * value, this.z * value)

  operator fun times(value: Vec3d): Vec3d =
      Vec3d(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun times(value: Vec3i): Vec3d =
      Vec3d(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun plus(value: Vec3d): Vec3d =
      Vec3d(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Double, y: Double, z: Double): Vec3d =
      Vec3d(this.x + x, this.y + y, this.z + z)

  operator fun plus(value: Vec3i): Vec3d =
      Vec3d(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Int, y: Int, z: Int): Vec3d =
      Vec3d(this.x + x, this.y + y, this.z + z)

  operator fun minus(value: Vec3d): Vec3d =
      Vec3d(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Double, y: Double, z: Double): Vec3d =
      Vec3d(this.x - x, this.y - y, this.z - z)

  operator fun minus(value: Vec3i): Vec3d =
      Vec3d(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Int, y: Int, z: Int): Vec3d =
      Vec3d(this.x - x, this.y - y, this.z - z)

  operator fun unaryMinus(): Vec3d =
      Vec3d(-this.x, -this.y, -this.z)

  operator fun unaryPlus(): Vec3d = this

  fun toInt(): Vec3i =
      Vec3i(this.x.toInt(), this.y.toInt(), this.z.toInt())

  override fun compareTo(other: Vec3d): Int =
      sign(this.lengthSquared - other.lengthSquared).toInt()

  override fun toString(): String = "($x, $y, $z)"

  companion object {

    val One = Vec3d(1.0, 1.0, 1.0)
    val Zero = Vec3d(0.0, 0.0, 0.0)
  }
}
