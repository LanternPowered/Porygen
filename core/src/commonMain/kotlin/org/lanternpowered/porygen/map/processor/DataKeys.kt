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
   * A key to represent whether an edge is marked or corner is marked as a river.
   */
  val IS_RIVER = DataKey<Boolean>("is_river")

  /**
   * The distance from the start of the river, if the corner or edge is a river. A river starts
   * at the ocean and goes inwards the land.
   */
  val DISTANCE_TO_RIVER_START = DataKey<Int>("distance_to_river_start")

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

  /**
   * A key to represent the moisture. From 0 to 1.
   */
  val MOISTURE = DataKey<Double>("moisture")
}
