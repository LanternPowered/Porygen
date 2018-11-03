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
import org.spongepowered.math.vector.Vector2i
import java.awt.Polygon

/**
 * Represents a rectangle.
 */
class Rectanglei(min: Vector2i, max: Vector2i) : AbstractRectangle<Vector2i>(min(min, max), max(min, max)) {

  override fun contains(x: Double, y: Double): Boolean =
      x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt() = this

  override fun toDouble(): Rectangled =
      Rectangled(min.x.toDouble(), min.y.toDouble(), max.x.toDouble(), max.y.toDouble())

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
