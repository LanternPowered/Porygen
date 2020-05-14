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
import org.lanternpowered.porygen.map.CellMapChunk
import org.lanternpowered.porygen.math.vector.Vector2i

/**
 * The implementation of [CellMapChunk].
 *
 * @param map The map this chunk is located in
 * @param chunkPosition The position of the chunk
 */
class MapChunkImpl(
    override val map: MapImpl,
    override val chunkPosition: Vector2i,
    override val chunkSize: Vector2i,
    override val id: Long,
    private val cellBlockData: CellBlockData
) : SimpleDataHolder(), CellMapChunk {

  override val cells get() = this.cellBlockData.cells
  override fun getCell(localX: Int, localY: Int) = this.cellBlockData.getCell(localX, localY)

  override fun release() {
    TODO("Not yet implemented")
  }
}
