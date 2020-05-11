/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.processor

import org.lanternpowered.porygen.data.DataKey

object DataKeys {

  /**
   * A key to represent whether a cell is marked
   * as an ocean. Can be used later to determine
   * biomes.
   */
  val IS_OCEAN = DataKey<Boolean>()

  /**
   * A key to represent the distance to the ocean.
   *
   * This can be applied to cells and corners, for
   * cells it represents the number of cells to reach
   * the ocean. For corners, it's the number of edges.
   */
  val DISTANCE_TO_OCEAN = DataKey<Int>()
}
