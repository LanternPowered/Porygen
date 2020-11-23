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

import org.lanternpowered.porygen.util.ToStringHelper
import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.math.vector.Vec2i
import kotlin.math.max
import kotlin.math.min

class Rectangled : AbstractRectangle<Vec2d> {

  private val polygon by lazy {
    Polygond.newConvexPolygon(
        Vec2d(min.x, min.y),
        Vec2d(max.x, min.y),
        Vec2d(max.x, max.y),
        Vec2d(min.x, max.y))
  }

  override val size: Vec2d by lazy { max - min }

  constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) :
      super(Vec2d(min(minX, maxX), min(minY, maxY)), Vec2d(max(minX, maxX), max(minY, maxY)))

  constructor(min: Vec2d, max: Vec2d) :
      super(Vec2d(min(min.x, max.x), min(min.y, max.y)), Vec2d(max(min.x, max.x), max(min.y, max.y)))

  override fun contains(x: Double, y: Double): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun contains(x: Int, y: Int): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt(): Rectanglei {
    val min = min
    val max = max
    return Rectanglei(
        Vec2i(floorToInt(min.x), floorToInt(min.y)),
        Vec2i(floorToInt(max.x), floorToInt(max.y)))
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
