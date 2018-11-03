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

/**
 * Represents the complete map.
 */
interface CellMap : CellMapPart, DataHolder {

  /**
   * Gets the [CellMapChunk] at the given chunk coordinates.
   *
   * @param sectionX The section x coordinate
   * @param sectionZ The section z coordinate
   */
  fun getChunk(sectionX: Int, sectionZ: Int): CellMapChunk
}
