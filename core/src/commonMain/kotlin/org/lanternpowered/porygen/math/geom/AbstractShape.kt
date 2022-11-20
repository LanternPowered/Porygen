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

abstract class AbstractShape : Shape {

  protected abstract fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean

  override fun intersects(rectangle: Rectangled): Boolean =
    intersects(rectangle.min.x, rectangle.min.y, rectangle.max.x, rectangle.max.y)

  override fun intersects(rectangle: Rectanglei): Boolean =
    intersects(
      rectangle.min.x.toDouble(), rectangle.min.y.toDouble(),
      rectangle.max.x.toDouble(), rectangle.max.y.toDouble(),
    )

  protected abstract fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean

  override fun contains(rectangle: Rectangled): Boolean =
      contains(rectangle.min.x, rectangle.min.y, rectangle.max.x, rectangle.max.y)

  override fun contains(rectangle: Rectanglei): Boolean =
      contains(
        rectangle.min.x.toDouble(), rectangle.min.y.toDouble(),
        rectangle.max.x.toDouble(), rectangle.max.y.toDouble(),
      )

  override fun contains(polygon: Polygond): Boolean {
    if (!polygon.isConvex && polygon.trueIntersection(this))
      return false
    return polygon.vertices.all { vertex -> contains(vertex) }
  }

  override fun contains(x: Int, y: Int): Boolean = contains(x.toDouble(), y.toDouble())
}
