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
package org.lanternpowered.porygen.api.map

import com.flowpowered.math.vector.Vector2i
import com.flowpowered.math.vector.Vector3i
import org.lanternpowered.porygen.api.data.DataHolder
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.Chunk

/**
 * Represents a minecraft [Chunk] which also provides
 * information about the [CellMap].
 */
interface CellMapChunk : CellMapElement, DataHolder {

    /**
     * The actual chunk. Can be null if the chunk isn't loaded.
     */
    val chunk: Chunk?

    /**
     * The chunk position.
     */
    val chunkPos: Vector2i

    /**
     * All the different [Cell]s within this map chunk.
     */
    val cells: Collection<Cell>

    /**
     * Gets the [Cell] for the given local block coordinates.
     */
    fun getCell(localX: Int, localZ: Int): Cell

    companion object {

        /**
         * The size of a chunk.
         */
        val CHUNK_SIZE: Vector3i by lazy { Sponge.getServer().chunkLayout.chunkSize }
    }
}
