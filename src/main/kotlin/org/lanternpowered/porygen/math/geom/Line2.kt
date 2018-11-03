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

interface Line2<P> {

  /**
   * Gets the start point of this [Line2].
   *
   * @return The start point
   */
  val start: P

  /**
   * Gets the end point of this [Line2].
   *
   * @return The end point
   */
  val end: P

  /**
   * Gets whether this [Line2] intersects with
   * the given [Line2d].
   *
   * @param line The line
   * @return Whether the lines intersect
   */
  fun intersects(line: Line2d): Boolean =
      intersects(line.start, line.end)

  /**
   * Gets whether this [Line2] intersects with
   * the given coordinate based line.
   *
   * @param start The start point
   * @param end The end point
   * @return Whether the lines intersect
   */
  fun intersects(start: Vector2i, end: Vector2i): Boolean =
      intersects(start.x, start.y, end.x, end.y)

  /**
   * Gets whether this [Line2] intersects with
   * the given coordinate based line.
   *
   * @param start The start point
   * @param end The end point
   * @return Whether the lines intersect
   */
  fun intersects(start: Vector2d, end: Vector2d): Boolean =
      intersects(start.x, start.y, end.x, end.y)

  /**
   * Gets whether this [Line2] intersects with
   * the given coordinate based line.
   *
   * @param startX The start x coordinate
   * @param startY The start y coordinate
   * @param endX The end x coordinate
   * @param endY The end y coordinate
   * @return Whether the lines intersect
   */
  fun intersects(startX: Double, startY: Double, endX: Double, endY: Double): Boolean

  /**
   * Gets whether this [Line2] intersects with
   * the given coordinate based line.
   *
   * @param startX The start x coordinate
   * @param startY The start y coordinate
   * @param endX The end x coordinate
   * @param endY The end y coordinate
   * @return Whether the lines intersect
   */
  fun intersects(startX: Int, startY: Int, endX: Int, endY: Int): Boolean =
      intersects(startX.toDouble(), startY.toDouble(), endX.toDouble(), endY.toDouble())

  /**
   * Converts this [Line2]
   * into a [Line2i].
   *
   * @return The int line
   */
  fun toInt(): Line2i

  /**
   * Converts this [Line2]
   * into a [Line2d].
   *
   * @return The double line
   */
  fun toDouble(): Line2d
}
