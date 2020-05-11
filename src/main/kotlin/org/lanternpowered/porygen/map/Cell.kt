/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map

import org.lanternpowered.porygen.math.geom.Polygond
import org.spongepowered.math.vector.Vector2i

/**
 * Represents a cell within a [CellMap].
 */
interface Cell : CellMapElement {

  /**
   * Gets the center point [Vector2i] of this [Cell].
   *
   * @return The center point
   */
  val centerPoint: Vector2i

  /**
   * Gets the [Polygond] of this cell.
   *
   * @return The polygon
   */
  val polygon: Polygond

  /**
   * Gets the neighbor [Cell]s of this cell.
   *
   * @return The neighbor cells
   */
  val neighbors: Collection<Cell>

  /**
   * Gets the [Edge]s of this cell.
   *
   * @return The edges
   */
  val edges: Collection<Edge>

  /**
   * Gets the [Corner]s of this cell.
   *
   * @return The corners
   */
  val corners: Collection<Corner>

  /**
   * Gets whether the specified point [Vector2i]
   * is located inside this [Cell].
   *
   * @param point The point
   * @return Whether the point is located in this cell
   */
  operator fun contains(point: Vector2i) = contains(point.x, point.y)

  /**
   * Gets whether the specified point is located
   * inside this [Cell].
   *
   * @param x The x coordinate
   * @param z The z coordinate
   * @return Whether the point is located in this cell
   */
  fun contains(x: Int, z: Int): Boolean
}
