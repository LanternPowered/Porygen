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
package org.lanternpowered.porygen.map.impl

import com.flowpowered.math.GenericMath
import com.flowpowered.math.vector.Vector2d
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.DataKey
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.map.gen.CenteredPolygon
import org.lanternpowered.porygen.util.ChunkPos
import org.lanternpowered.porygen.util.geom.Polygond
import org.lanternpowered.porygen.util.geom.Rectangled
import org.lanternpowered.porygen.util.uncheckedCast
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

class PorygenCell : Cell {

    override val centerPoint: Vector2d
    override val polygon: Polygond

    // A set with all the referenced views
    internal val referencedViews: MutableSet<Rectangled> = HashSet()

    // A set with all the chunk coordinates this cell is located in
    val chunks = LongOpenHashSet()

    // Data values stored within this cell
    private val dataValues = ConcurrentHashMap<DataKey<*>, Any?>()

    internal val theNeighbors = ArrayList<Cell>()
    internal val theEdges = ArrayList<Edge>()
    internal val theCorners = ArrayList<Corner>()

    private val unmodifiableNeighbors = Collections.unmodifiableList(this.theNeighbors)
    private val unmodifiableEdges = Collections.unmodifiableList(this.theEdges)
    private val unmodifiableCorners = Collections.unmodifiableList(this.theCorners)

    override val neighbors: List<Cell> = this.unmodifiableNeighbors
    override val edges: List<Edge> = this.unmodifiableEdges
    override val corners: List<Corner> = this.unmodifiableCorners

    internal constructor(centeredPolygon: CenteredPolygon) {
        this.centerPoint = centeredPolygon.center
        this.polygon = centeredPolygon.polygon

        // Generate the outer bounds of the cell, to know in
        // which chunks this cell is located.

        var minX = Double.MAX_VALUE
        var minY = Double.MAX_VALUE
        var maxX = -Double.MAX_VALUE
        var maxY = -Double.MAX_VALUE

        for (vertex in this.polygon.vertices) {
            minX = Math.min(minX, vertex.x)
            maxX = Math.max(maxX, vertex.x)
            minY = Math.min(minY, vertex.y)
            maxY = Math.max(maxY, vertex.y)
        }

        // Collect all the chunks the cell is actually located in

        val chunkStartX = GenericMath.floor(minX) shr 4
        val chunkStartY = GenericMath.floor(minY) shr 4
        val chunkEndX = GenericMath.floor(maxX) shr 4
        val chunkEndY = GenericMath.floor(maxY) shr 4

        for (chunkX in chunkStartX..chunkEndX) {
            chunkLoop@ for (chunkY in chunkStartY..chunkEndY) {
                for (localX in 0 until 16) {
                    val x = chunkX + localX
                    for (localY in 0 until 16) {
                        val y = chunkY + localY
                        if (this.polygon.contains(x, y)) {
                            val chunkPos = ChunkPos(x, y)
                            this.chunks.add(chunkPos.packed)
                            continue@chunkLoop
                        }
                    }
                }
            }
        }

        // Make the set as small as possible
        this.chunks.trim()
    }

    override fun <T> get(key: DataKey<T>): T? = this.dataValues[key].uncheckedCast()

    override fun <T> set(key: DataKey<T>, value: T) {
        this.dataValues[key] = value as Any?
    }

    override fun contains(x: Double, y: Double) = this.polygon.contains(x, y)
}
