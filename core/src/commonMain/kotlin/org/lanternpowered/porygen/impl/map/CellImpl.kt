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

import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.math.geom.Polygond
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i

class CellImpl internal constructor(
    override val id: Long,
    override val map: MapImpl,
    override val centerPoint: Vector2i,
    override val polygon: Polygond
) : MapElementImpl(), Cell {

  private var hashCode: Int = 0

  // A set with all the chunk coordinates this cell is located in
  //val chunks = data.chunks

  internal val mutableNeighbors = LinkedHashSet<CellImpl>()
  internal val mutableEdges = LinkedHashSet<EdgeImpl>()
  internal val mutableCorners = LinkedHashSet<CornerImpl>()

  override val neighbors: Collection<CellImpl> get() = mutableNeighbors
  override val edges: Collection<EdgeImpl> get() = mutableEdges
  override val corners: Collection<CornerImpl> get() = mutableCorners

  // TODO
  override fun contains(x: Int, y: Int): Boolean {
    val chunkPos = packIntPair(x shr 4, y shr 4)
    val chunk = map.getChunkIfLoaded(chunkPos)
    // If there is a chunk, it's already populated with information
    // about every block in which cells they are located, so use
    // this information to speed up the lookup.
    if (chunk != null)
      return chunk.getCell(x and 0xf, y and 0xf) == this
    // Don't even perform any calculations if the chunk pos isn't in this cell
    return /*this.chunks.contains(chunkPos) && */ this.polygon.contains(Vector2d(x + 0.5, y + 0.5)) // Use block center as check pos
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Cell)
      return false
    val delegate = other.delegate
    return delegate.id == id && delegate.map == map
  }

  override fun hashCode(): Int {
    if (hashCode == 0)
      hashCode = arrayOf(id, map, Cell::class).contentHashCode()
    return hashCode
  }
}
