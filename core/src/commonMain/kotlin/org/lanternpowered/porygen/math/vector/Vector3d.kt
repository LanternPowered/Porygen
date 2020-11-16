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
data class Vector3d(
    val x: Double,
    val y: Double,
    val z: Double
) : Comparable<Vector3d> {

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

  operator fun div(value: Double): Vector3d =
      Vector3d(this.x / value, this.y / value, this.z / value)

  operator fun div(value: Vector3d): Vector3d =
      Vector3d(this.x / value.x, this.y / value.y, this.z / value.z)

  operator fun div(value: Vector3i): Vector3d =
      Vector3d(this.x / value.x, this.y / value.y, this.z / value.z)

  operator fun times(value: Double): Vector3d =
      Vector3d(this.x * value, this.y * value, this.z * value)

  operator fun times(value: Vector3d): Vector3d =
      Vector3d(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun times(value: Vector3i): Vector3d =
      Vector3d(this.x * value.x, this.y * value.y, this.z * value.z)

  operator fun plus(value: Vector3d): Vector3d =
      Vector3d(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Double, y: Double, z: Double): Vector3d =
      Vector3d(this.x + x, this.y + y, this.z + z)

  operator fun plus(value: Vector3i): Vector3d =
      Vector3d(this.x + value.x, this.y + value.y, this.z + value.z)

  fun plus(x: Int, y: Int, z: Int): Vector3d =
      Vector3d(this.x + x, this.y + y, this.z + z)

  operator fun minus(value: Vector3d): Vector3d =
      Vector3d(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Double, y: Double, z: Double): Vector3d =
      Vector3d(this.x - x, this.y - y, this.z - z)

  operator fun minus(value: Vector3i): Vector3d =
      Vector3d(this.x - value.x, this.y - value.y, this.z - value.z)

  fun minus(x: Int, y: Int, z: Int): Vector3d =
      Vector3d(this.x - x, this.y - y, this.z - z)

  operator fun unaryMinus(): Vector3d =
      Vector3d(-this.x, -this.y, -this.z)

  operator fun unaryPlus(): Vector3d = this

  fun toInt(): Vector3i =
      Vector3i(this.x.toInt(), this.y.toInt(), this.z.toInt())

  override fun compareTo(other: Vector3d): Int =
      sign(this.lengthSquared - other.lengthSquared).toInt()

  override fun toString(): String = "($x, $y, $z)"

  companion object {

    val ONE = Vector3d(1.0, 1.0, 1.0)
    val ZERO = Vector3d(0.0, 0.0, 0.0)
  }
}
