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

/**
 * The manager for all the [CellMap] of each active [World].
 */
interface CellMapManager {

  /**
   * Attempts to create a new [CellMap].
   */
  fun createMap(): CellMap // TODO: Customize the map
}
