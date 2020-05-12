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

import org.spongepowered.math.vector.Vector2i

/**
 * Represents a chunk on a map. A chunk consists of
 * (chunkSize.x * chunkSize.y) tiles.
 *
 * After the chunk is no longer needed, it should be released through
 * [release]. Otherwise it's kept into memory until the map itself is
 * destroyed.
 */
interface CellMapChunk : CellMapElement, Releasable {

  /**
   * The chunk size. This should be the same
   * for every section in a map.
   */
  val chunkSize: Vector2i

  /**
   * The chunk position.
   */
  val chunkPosition: Vector2i

  /**
   * All the different [Cell]s within this chunk.
   */
  val cells: Collection<Cell>

  /**
   * Gets the [Cell] for the given local tile coordinates.
   */
  fun getCell(localX: Int, localY: Int): Cell
}
