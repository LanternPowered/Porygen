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
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import org.lanternpowered.porygen.api.map.gen.polygon.CellPolygon
import org.lanternpowered.porygen.api.util.ceilToInt
import org.lanternpowered.porygen.api.util.floorToInt
import org.lanternpowered.porygen.api.util.geom.Polygond
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import kotlin.math.max
import kotlin.math.min

/**
 * Temporary cell data.
 */
data class CellData(
        val center: Vector2i,
        val polygon: Polygond,
        val id: Long,
        val chunks: LongSet
)

internal fun buildCellData(cellPolygon: CellPolygon): CellData {
    val center = cellPolygon.center.toInt()
    val polygon = cellPolygon.polygon

    val id = packIntPair(center.x, center.y)
    val chunks = LongOpenHashSet()

    // Generate the outer bounds of the cell, to know in
    // which chunks this cell is located.

    var minX = Double.MAX_VALUE
    var minZ = Double.MAX_VALUE
    var maxX = -Double.MAX_VALUE
    var maxZ = -Double.MAX_VALUE

    for (vertex in polygon.vertices) {
        minX = min(minX, vertex.x)
        maxX = max(maxX, vertex.x)
        minZ = min(minZ, vertex.y)
        maxZ = max(maxZ, vertex.y)
    }

    // Collect all the chunks the cell is actually located in

    val chunkStartX = minX.floorToInt() shr 4
    val chunkStartZ = minZ.floorToInt() shr 4
    val chunkEndX = maxX.ceilToInt() shr 4
    val chunkEndZ = maxZ.ceilToInt() shr 4

    for (chunkX in chunkStartX..chunkEndX) {
        for (chunkZ in chunkStartZ..chunkEndZ) {
            val chunkArea = Rectanglei(chunkX shl 4, chunkZ shl 4, (chunkX + 1) shl 4, (chunkZ + 1) shl 4)
            if (polygon.contains(chunkArea) || polygon.intersects(chunkArea)) {
                chunks.add(packIntPair(chunkX, chunkZ))
            }
        }
    }

    // Make the set as small as possible
    chunks.trim()
    return CellData(center, polygon, id, chunks)
}
