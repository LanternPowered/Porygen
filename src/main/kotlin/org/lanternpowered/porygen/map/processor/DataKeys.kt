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
  val IS_OCEAN = DataKey<Boolean>("is_ocean")

  /**
   * A key to represent whether a edge is marked
   * or corner is marked as a river.
   */
  val IS_RIVER = DataKey<Boolean>("is_river")

  /**
   * A key to represent the distance to the ocean.
   *
   * Corners at the coastline will have a distance
   * of 0. Corners further in land start at 1. Corners
   * further into the ocean start at -1.
   *
   * Cells that are the coastline will have a distance
   * of 1. Cells further in land start at 2. Cells further
   * in the ocean start at -1.
   */
  val DISTANCE_TO_OCEAN = DataKey<Int>("distance_to_ocean")
}
