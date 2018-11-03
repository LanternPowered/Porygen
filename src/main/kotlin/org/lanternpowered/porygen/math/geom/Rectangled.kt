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
import org.lanternpowered.porygen.util.floorToInt
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
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

  constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) :
      super(Vector2d(min(minX, maxX), min(minY, maxY)), Vector2d(max(minX, maxX), max(minY, maxY)))

  constructor(min: Vector2d, max: Vector2d) :
      super(Vector2d(min(min.x, max.x), min(min.y, max.y)), Vector2d(max(min.x, max.x), max(min.y, max.y)))

  override fun contains(x: Double, y: Double): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt(): Rectanglei {
    val min = min
    val max = max
    return Rectanglei(
        Vector2i(GenericMath.floor(min.x), GenericMath.floor(min.y)),
        Vector2i(GenericMath.floor(max.x), GenericMath.floor(max.y)))
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
    pointsX[0] = min.x.floorToInt()
    pointsY[0] = min.y.floorToInt()
    pointsX[1] = max.x.floorToInt()
    pointsY[1] = min.y.floorToInt()
    pointsX[2] = max.x.floorToInt()
    pointsY[2] = max.y.floorToInt()
    pointsX[3] = min.x.floorToInt()
    pointsY[3] = max.y.floorToInt()
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
