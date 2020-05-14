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

import org.lanternpowered.porygen.math.vector.Vector2i

interface Corner : CellMapElement {

  /**
   * The corner point [Vector2i].
   */
  val point: Vector2i

  /**
   * All the [Cell]s that connect to this [Corner].
   */
  val cells: Collection<Cell>

  /**
   * All the neighbor [Corner]s.
   */
  val neighbors: Collection<Corner>

  /**
   * All the [Edge]s that connect to this [Corner].
   */
  val edges: Collection<Edge>
}
