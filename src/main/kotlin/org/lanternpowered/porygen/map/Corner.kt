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

import org.lanternpowered.porygen.data.DataHolder
import org.spongepowered.math.vector.Vector2i

interface Corner : CellMapElement, DataHolder {

  /**
   * Gets the corner point [Vector2i].
   *
   * @return The corner point
   */
  val point: Vector2i

  /**
   * Gets all the [Cell]s that connect
   * to this [Corner].
   *
   * @return The cells
   */
  val cells: Collection<Cell>

  /**
   * Gets all the [Edge]s that connect
   * to this [Corner].
   *
   * @return The edges
   */
  val edges: Collection<Edge>
}
