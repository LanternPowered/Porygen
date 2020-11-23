/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.terrain

import org.lanternpowered.porygen.math.vector.Vec3i

/**
 * Represents a terrain of blocks.
 */
interface Terrain<Block> {

  /**
   * Gets the [Block] at the given position.
   */
  fun getBlock(position: Vec3i): Block
}
