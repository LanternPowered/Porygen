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

import org.lanternpowered.porygen.math.geom.Line2i

/**
 * Represents the edge of one
 * or multiple [Cell]s.
 */
interface Edge : CellMapElement {

  /**
   * Gets the [Line2i].
   *
   * @return The line
   */
  val line: Line2i

  /**
   * A collection with all the [Cell]s that have this edge.
  */
  val cells: Collection<Cell>

  /**
   * Gets the other cell this edge is next to.
   *
   * @throws IllegalArgumentException If the given [cell] doesn't have this edge
   */
  fun other(cell: Cell): Cell

  /**
   * A collection with all the [Corner]s that connect to this edge.
   */
  val corners: Collection<Corner>
}
