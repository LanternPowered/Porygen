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

import org.lanternpowered.porygen.math.vector.max
import org.lanternpowered.porygen.math.vector.min
import org.lanternpowered.porygen.math.vector.Vector2i
import java.awt.Polygon
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a rectangle.
 */
class Rectanglei : AbstractRectangle<Vector2i> {

  override val size: Vector2i by lazy { max - min }

  constructor(min: Vector2i, max: Vector2i) :
      super(min(min, max), max(min, max))

  constructor(minX: Int, minY: Int, maxX: Int, maxY: Int) :
      super(Vector2i(min(minX, maxX), min(minY, maxY)), Vector2i(max(minX, maxX), max(minY, maxY)))

  override fun contains(x: Double, y: Double): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun contains(x: Int, y: Int): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt() = this

  override fun toDouble(): Rectangled =
      Rectangled(min.x.toDouble(), min.y.toDouble(), max.x.toDouble(), max.y.toDouble())

  fun translate(offset: Vector2i): Rectanglei =
      Rectanglei(min + offset, max + offset)

  /**
   * Converts this [Rectangled] into
   * a drawable [Polygon].
   *
   * @return The drawable polygon
   */
  fun toDrawable(): Polygon {
    val pointsX = IntArray(4)
    val pointsY = IntArray(4)
    pointsX[0] = min.x
    pointsY[0] = min.y
    pointsX[1] = max.x
    pointsY[1] = min.y
    pointsX[2] = max.x
    pointsY[2] = max.y
    pointsX[3] = min.x
    pointsY[3] = max.y
    return Polygon(pointsX, pointsY, 4)
  }
}
