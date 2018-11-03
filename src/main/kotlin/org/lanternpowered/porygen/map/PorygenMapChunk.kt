/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen.map

import com.flowpowered.math.vector.Vector2i
import org.lanternpowered.porygen.api.data.SimpleDataHolder
import org.lanternpowered.porygen.api.map.CellMap
import org.lanternpowered.porygen.api.map.CellMapChunk
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import org.spongepowered.api.world.Chunk

/**
 * Wraps around a minecraft [Chunk] to cache information
 * about the cells which each block is located in.
 *
 * @param map The map this chunk is located in
 * @param chunkPos The position of the chunk
 */
class PorygenMapChunk(
        override val map: PorygenMap,
        override val chunkPos: Vector2i
) : SimpleDataHolder(), CellMapChunk {

    internal var cellBlockData: CellBlockData = NullCellBlockData

    override val id: Long = packIntPair(this.chunkPos.x, this.chunkPos.y)
    override val chunk: Chunk? get() = this.map.world.getChunk(this.chunkPos.x, 0, this.chunkPos.y).orElse(null)

    override val cells get() = this.cellBlockData.cells
    override fun getCell(localX: Int, localZ: Int) = this.cellBlockData.getCell(localX, localZ)
}
