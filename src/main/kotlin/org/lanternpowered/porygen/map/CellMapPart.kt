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

import org.lanternpowered.porygen.math.geom.Rectanglei
import org.spongepowered.math.vector.Vector2i

/**
 * Represents a view of a [CellMap].
 */
interface CellMapPart {

  /**
   * Gets a sub [CellMapView] of this map view. The minimum and maximum
   * coordinates will be clamped to the bounds of this view.
   *
   * @param min The area minimum coordinate
   * @param max The area maximum coordinate
   * @return The new map view
   */
  fun getSubView(min: Vector2i, max: Vector2i) = getSubView(Rectanglei(min, max))

  /**
   * Gets a sub [CellMapView] of this map view. The minimum and maximum
   * coordinates will be clamped to the bounds of this view.
   *
   * @param rectangle The area
   * @return The new map view
   */
  fun getSubView(rectangle: Rectanglei): CellMapView

  /**
   * Gets the [Cell] the specified point is located in.
   *
   * @param point The point
   * @return The cell
   */
  fun getCell(point: Vector2i) = getCell(point.x, point.y)

  /**
   * Gets the [Cell] the specified point is located in.
   *
   * @param x The x coordinate
   * @param z The z coordinate
   * @return The cell
   */
  fun getCell(x: Int, z: Int): Cell

  /**
   * Gets the [Corner] for the specified id. Or null if it doesn't exist.
   *
   * @param id The corner id
   * @return The corner
   */
  fun getCorner(id: Long): Corner?

  /**
   * Gets the [Edge] for the specified id. Or null if it doesn't exist.
   *
   * @param id The edge id
   * @return The edge
   */
  fun getEdge(id: Long): Edge?

  /**
   * Gets the [Cell] for the specified id. Or null if it doesn't exist.
   *
   * @param id The cell id
   * @return The cell
   */
  fun getCell(id: Long): Cell?

  operator fun contains(element: CellMapElement): Boolean
}
