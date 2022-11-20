/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.biome

import org.lanternpowered.porygen.map.Cell
import kotlin.random.Random

/**
 * A selector which can be used to select a [Biome] on [Cell] level.
 */
interface CellBiomeSelector<Biome> {

  /**
   * Selects a biome for the given [Cell].
   */
  fun select(cell: Cell, random: Random): Biome
}
