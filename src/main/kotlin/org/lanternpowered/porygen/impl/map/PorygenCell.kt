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
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.util.tuple.packIntPair
import org.spongepowered.math.vector.Vector2d
import java.util.Collections

class PorygenCell internal constructor(override val map: PorygenMap, data: CellData) : SimpleDataHolder(), Cell {

  override val centerPoint = data.center
  override val polygon = data.polygon
  override val id = data.id

  // A set with all the chunk coordinates this cell is located in
  val chunks = data.chunks

  // A set with all the referenced views
  internal val referencedViews = mutableSetOf<Rectanglei>()

  internal val theNeighbors = mutableListOf<Cell>()
  internal val theEdges = mutableListOf<Edge>()
  internal val theCorners = mutableListOf<Corner>()

  override val neighbors: List<Cell> = Collections.unmodifiableList(this.theNeighbors)
  override val edges: List<Edge> = Collections.unmodifiableList(this.theEdges)
  override val corners: List<Corner> = Collections.unmodifiableList(this.theCorners)

  override fun contains(x: Int, z: Int): Boolean {
    val chunkPos = packIntPair(x shr 4, z shr 4)
    val chunk = this.map.getChunkIfLoaded(chunkPos)
    // If there is a chunk, it's already populated with information
    // about every block in which cells they are located, so use
    // this information to speed up the lookup.
    if (chunk != null) {
      return chunk.getCell(x and 0xf, z and 0xf) == this
    }
    // Don't even perform any calculations if the chunk pos isn't in this cell
    return this.chunks.contains(chunkPos) && this.polygon.contains(Vector2d(x + 0.5, z + 0.5)) // Use block center as check pos
  }
}
