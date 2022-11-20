/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.math.geom

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.lanternpowered.porygen.util.ToStringHelper
import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.math.vector.Vec2i
import kotlin.math.max
import kotlin.math.min

@Serializable
class Rectangled private constructor(
  override val min: Vec2d,
  override val max: Vec2d,
  @Transient private val ignored: Unit = Unit,
) : AbstractRectangle<Vec2d>() {

  companion object {

    val Zero = Rectangled(0.0, 0.0, 0.0, 0.0)
  }

  private val polygon by lazy {
    Polygond.newConvexPolygon(
        Vec2d(min.x, min.y),
        Vec2d(max.x, min.y),
        Vec2d(max.x, max.y),
        Vec2d(min.x, max.y))
  }

  override val size: Vec2d by lazy { max - min }

  constructor(min: Vec2d, max: Vec2d) :
    this(
      min = Vec2d(min(min.x, max.x), min(min.y, max.y)),
      max = Vec2d(max(min.x, max.x), max(min.y, max.y)),
      ignored = Unit,
    )

  constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) :
    this(
      min = Vec2d(min(minX, maxX), min(minY, maxY)),
      max = Vec2d(max(minX, maxX), max(minY, maxY)),
    )

  override fun contains(x: Double, y: Double): Boolean =
    x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun contains(x: Int, y: Int): Boolean =
    x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt(): Rectanglei {
    val min = min
    val max = max
    return Rectanglei(
      min = Vec2i(floorToInt(min.x), floorToInt(min.y)),
      max = Vec2i(floorToInt(max.x), floorToInt(max.y)),
    )
  }

  override fun toDouble(): Rectangled = this

  /**
   * Converts this [Rectangled] into a [Polygond].
   *
   * @return The polygon
   */
  fun toPolygon(): Polygond = this.polygon

  override fun toString(): String = ToStringHelper(this)
    .add("min", min)
    .add("max", max)
    .toString()

  override fun equals(other: Any?): Boolean {
    if (other !is Rectangled)
      return false
    return other.min == min && other.max == max
  }

  override fun hashCode(): Int = arrayOf<Any>(min, max).contentHashCode()
}
