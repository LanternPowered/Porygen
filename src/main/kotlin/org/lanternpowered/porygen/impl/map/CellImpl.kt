/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.util.pair.packIntPair
import org.spongepowered.math.vector.Vector2d

class CellImpl internal constructor(override val map: MapImpl, data: CellData) : SimpleDataHolder(), Cell {

  override val centerPoint = data.center
  override val polygon = data.polygon
  override val id = data.id

  // A set with all the chunk coordinates this cell is located in
  val chunks = data.chunks

  internal val mutableNeighbors = mutableListOf<Cell>()
  internal val mutableEdges = mutableListOf<Edge>()
  internal val mutableCorners = mutableListOf<Corner>()

  override val neighbors: List<Cell> get() = mutableNeighbors
  override val edges: List<Edge> get() = mutableEdges
  override val corners: List<Corner> get() = mutableCorners

  override fun contains(x: Int, z: Int): Boolean {
    val chunkPos = packIntPair(x shr 4, z shr 4)
    val chunk = map.getChunkIfLoaded(chunkPos)
    // If there is a chunk, it's already populated with information
    // about every block in which cells they are located, so use
    // this information to speed up the lookup.
    if (chunk != null)
      return chunk.getCell(x and 0xf, z and 0xf) == this
    // Don't even perform any calculations if the chunk pos isn't in this cell
    return this.chunks.contains(chunkPos) && this.polygon.contains(Vector2d(x + 0.5, z + 0.5)) // Use block center as check pos
  }
}
