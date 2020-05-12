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
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Represents a partial view of a [CellMap]. It allows access to all the
 * [Cell]s, etc. that are visible to this [CellMapView]. Neighbor cells,
 * etc. that aren't in the view, won't be available.
 *
 * After the view is no longer needed, it should be released through
 * [release]. Otherwise it's kept into memory until the map itself is
 * destroyed.
 */
interface CellMapView : CellMapPart, Releasable {

  /**
   * The bounds of this cell map view.
   */
  val viewRectangle: Rectanglei

  /**
   * The [CellMap] of this map view.
   */
  val map: CellMap

  /**
   * All the [Cell]s that are (partially) visible in this [CellMapView].
   */
  val cells: Collection<Cell>

  /**
   * All the [Corner]s that are visible in this [CellMapView].
   */
  val corners: Collection<Corner>

  /**
   * All the [Edge]s that are (partially) visible in this [CellMapView].
   */
  val edges: Collection<Edge>

  /**
   * Grows the [CellMapView] and returns a new [CellMapView] with new size.
   *
   * The [size] will be applied in every direction, +x, -x, +y, -y
   *
   * This doesn't release the original [CellMapView].
   */
  fun grow(size: Vector2i): CellMapView
}
