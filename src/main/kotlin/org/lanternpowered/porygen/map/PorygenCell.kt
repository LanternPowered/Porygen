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

import org.lanternpowered.porygen.api.data.SimpleDataHolder
import org.lanternpowered.porygen.api.map.Cell
import org.lanternpowered.porygen.api.map.Corner
import org.lanternpowered.porygen.api.map.Edge
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import java.util.*
import kotlin.collections.ArrayList

class PorygenCell internal constructor(override val map: PorygenMap, data: CellData) : SimpleDataHolder(), Cell {

    override val centerPoint = data.center
    override val polygon = data.polygon
    override val id = data.id

    // A set with all the chunk coordinates this cell is located in
    val chunks = data.chunks

    // A set with all the referenced views
    internal val referencedViews: MutableSet<Rectanglei> = HashSet()

    internal val theNeighbors = ArrayList<Cell>()
    internal val theEdges = ArrayList<Edge>()
    internal val theCorners = ArrayList<Corner>()

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
        return this.chunks.contains(chunkPos) && this.polygon.contains(x + 0.5, z + 0.5) // Use block center as check pos
    }
}
