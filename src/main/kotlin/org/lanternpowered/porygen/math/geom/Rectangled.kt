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
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i
import java.awt.Polygon
import kotlin.math.max
import kotlin.math.min

class Rectangled : AbstractRectangle<Vector2d> {

  private val polygon by lazy {
    Polygond.newConvexPolygon(
        Vector2d(min.x, min.y),
        Vector2d(max.x, min.y),
        Vector2d(max.x, max.y),
        Vector2d(min.x, max.y))
  }

  override val size: Vector2d by lazy { max - min }

  constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) :
      super(Vector2d(min(minX, maxX), min(minY, maxY)), Vector2d(max(minX, maxX), max(minY, maxY)))

  constructor(min: Vector2d, max: Vector2d) :
      super(Vector2d(min(min.x, max.x), min(min.y, max.y)), Vector2d(max(min.x, max.x), max(min.y, max.y)))

  override fun contains(x: Double, y: Double): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun contains(x: Int, y: Int): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt(): Rectanglei {
    val min = min
    val max = max
    return Rectanglei(
        Vector2i(floorToInt(min.x), floorToInt(min.y)),
        Vector2i(floorToInt(max.x), floorToInt(max.y)))
  }

  override fun toDouble(): Rectangled = this

  /**
   * Converts this [Rectangled] into a [Polygond].
   *
   * @return The polygon
   */
  fun toPolygon(): Polygond = this.polygon

  /**
   * Converts this [Rectangled] into
   * a drawable [Polygon].
   *
   * @return The drawable polygon
   */
  fun toDrawable(): Polygon {
    val pointsX = IntArray(4)
    val pointsY = IntArray(4)
    pointsX[0] = floorToInt(min.x)
    pointsY[0] = floorToInt(min.y)
    pointsX[1] = floorToInt(max.x)
    pointsY[1] = floorToInt(min.y)
    pointsX[2] = floorToInt(max.x)
    pointsY[2] = floorToInt(max.y)
    pointsX[3] = floorToInt(min.x)
    pointsY[3] = floorToInt(max.y)
    return Polygon(pointsX, pointsY, 4)
  }

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
