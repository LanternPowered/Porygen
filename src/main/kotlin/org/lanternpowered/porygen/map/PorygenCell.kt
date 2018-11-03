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

import com.flowpowered.math.GenericMath
import com.flowpowered.math.vector.Vector2i
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import org.lanternpowered.porygen.api.data.SimpleDataHolder
import org.lanternpowered.porygen.api.map.Cell
import org.lanternpowered.porygen.api.map.Corner
import org.lanternpowered.porygen.api.map.Edge
import org.lanternpowered.porygen.api.map.gen.polygon.CellPolygon
import org.lanternpowered.porygen.api.util.geom.Polygond
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import java.util.*
import kotlin.collections.ArrayList

class PorygenCell : SimpleDataHolder, Cell {

    override val map: PorygenMap
    override val centerPoint: Vector2i
    override val polygon: Polygond

    override val id: Long

    // A set with all the referenced views
    internal val referencedViews: MutableSet<Rectanglei> = HashSet()

    // A set with all the chunk coordinates this cell is located in
    val chunks = LongOpenHashSet()

    internal val theNeighbors = ArrayList<Cell>()
    internal val theEdges = ArrayList<Edge>()
    internal val theCorners = ArrayList<Corner>()

    override val neighbors: List<Cell> = Collections.unmodifiableList(this.theNeighbors)
    override val edges: List<Edge> = Collections.unmodifiableList(this.theEdges)
    override val corners: List<Corner> = Collections.unmodifiableList(this.theCorners)

    internal constructor(map: PorygenMap, centeredPolygon: CellPolygon) {
        this.map = map
        this.centerPoint = centeredPolygon.center.toInt()
        this.polygon = centeredPolygon.polygon
        this.id = packIntPair(this.centerPoint.x, this.centerPoint.y)

        // Generate the outer bounds of the cell, to know in
        // which chunks this cell is located.

        var minX = Double.MAX_VALUE
        var minZ = Double.MAX_VALUE
        var maxX = -Double.MAX_VALUE
        var maxZ = -Double.MAX_VALUE

        for (vertex in this.polygon.vertices) {
            minX = Math.min(minX, vertex.x)
            maxX = Math.max(maxX, vertex.x)
            minZ = Math.min(minZ, vertex.y)
            maxZ = Math.max(maxZ, vertex.y)
        }

        // Collect all the chunks the cell is actually located in

        val chunkStartX = GenericMath.floor(minX) shr 4
        val chunkStartZ = GenericMath.floor(minZ) shr 4
        val chunkEndX = GenericMath.floor(maxX) shr 4
        val chunkEndZ = GenericMath.floor(maxZ) shr 4

        for (chunkX in chunkStartX..chunkEndX) {
            for (chunkZ in chunkStartZ..chunkEndZ) {
                val chunkArea = Rectanglei(chunkX shl 4, chunkZ shl 4, (chunkX + 1) shl 4, (chunkZ + 1) shl 4)
                if (this.polygon.contains(chunkArea) || this.polygon.intersects(chunkArea)) {
                    this.chunks.add(packIntPair(chunkX, chunkZ))
                }
            }
        }

        // Make the set as small as possible
        this.chunks.trim()
    }

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
