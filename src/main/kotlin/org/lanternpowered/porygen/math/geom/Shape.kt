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

import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i

interface Shape {

  /**
   * Gets whether the given [Shape] is located inside this [Shape].
   *
   * @param shape The shape
   * @return Whether the shape is located inside this 2d shape
   */
  operator fun contains(shape: Shape): Boolean {
    return when (shape) {
      is Rectangled -> contains(shape)
      is Rectanglei -> contains(shape)
      is Polygond -> contains(shape)
      else -> throw IllegalStateException()
    }
  }

  /**
   * Gets whether the given [Rectanglei] is located inside this [Shape].
   *
   * @param rectangle The rectangle
   * @return Whether the rectangle is located inside this 2d shape
   */
  operator fun contains(rectangle: Rectanglei): Boolean

  /**
   * Gets whether the given [Rectangled] is located inside this [Shape].
   *
   * @param rectangle The rectangle
   * @return Whether the rectangle is located inside this 2d shape
   */
  operator fun contains(rectangle: Rectangled): Boolean

  /**
   * Gets whether the given [Polygond] is located inside this [Shape].
   *
   * @param polygon The polygon
   * @return Whether the polygon is located inside this 2d shape
   */
  operator fun contains(polygon: Polygond): Boolean

  /**
   * Gets whether the given [Shape] intersects with this [Shape].
   *
   * @param shape The shape
   * @return Whether the shape is intersecting with this shape
   */
  fun intersects(shape: Shape): Boolean {
    return when (shape) {
      is Rectangled -> intersects(shape)
      is Rectanglei -> intersects(shape)
      is Polygond -> intersects(shape)
      else -> throw IllegalStateException()
    }
  }

  /**
   * Gets whether the given [Rectanglei] intersects with this [Shape].
   *
   * @param rectangle The rectangle
   * @return Whether the rectangle is intersecting with this shape
   */
  fun intersects(rectangle: Rectanglei): Boolean

  /**
   * Gets whether the given [Rectangled] intersects with this [Shape].
   *
   * @param rectangle The rectangle
   * @return Whether the rectangle is intersecting with this shape
   */
  fun intersects(rectangle: Rectangled): Boolean

  /**
   * Gets whether the given [Polygond] intersects with this [Shape].
   *
   * @param polygon The polygon
   * @return Whether the polygon is intersecting with this shape
   */
  fun intersects(polygon: Polygond): Boolean

  /**
   * Gets whether the given point [Vector2d] is located within this [Shape].
   *
   * @param point The point
   * @return Whether this 2d shape contains the point
   */
  operator fun contains(point: Vector2i): Boolean =
      contains(point.x, point.y)

  /**
   * Gets whether the given is are located within this [Shape].
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return Whether this 2d shape contains the point
   */
  fun contains(x: Int, y: Int): Boolean

  /**
   * Gets whether the given point [Vector2d] is located within this [Shape].
   *
   * @param point The point
   * @return Whether this 2d shape contains the point
   */
  operator fun contains(point: Vector2d): Boolean =
      contains(point.x, point.y)

  /**
   * Gets whether the given is are located within this [Shape].
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return Whether this 2d shape contains the point
   */
  fun contains(x: Double, y: Double): Boolean
}
