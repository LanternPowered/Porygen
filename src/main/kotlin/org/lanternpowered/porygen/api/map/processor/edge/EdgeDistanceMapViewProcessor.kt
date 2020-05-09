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
package org.lanternpowered.porygen.api.map.processor.edge

import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.data.DataKey
import org.lanternpowered.porygen.api.map.CellMapView
import org.lanternpowered.porygen.api.map.processor.CellMapViewProcessor
import kotlin.math.roundToInt

/**
 * A map view processor that computes the distance to closest
 * edge for each block coordinate. This is only done for a specific
 * distance from the edges so not every block coordinate gets processed.
 *
 * @property sideIterations The amount of iterations on each side of each edge
 */
class EdgeDistanceMapViewProcessor(
        private val sideIterations: Int = 10
) : CellMapViewProcessor {

    override fun process(context: GeneratorContext, view: CellMapView) {
        val v = view.viewRectangle

        val startChunkX = v.min.x shr 4
        val startChunkZ = v.min.y shr 4

        val endChunkX = v.max.x shr 4
        val endChunkZ = v.max.y shr 4

        val chunksX = endChunkX - startChunkX
        val chunksZ = endChunkZ - startChunkZ

        val chunkCache = Array<EdgeDistanceChunkData?>(chunksX * chunksZ) { null }
        for (relX in 0 until chunksX) {
            for (relZ in 0 until chunksZ) {
                chunkCache[relZ * chunksX + relX] = view.map.getChunk(startChunkX + relX, startChunkZ + relZ)[EdgeDistanceData]?.copy()
            }
        }

        fun processLine(edgeId: Long, startX: Double, startY: Double, stepY: Double, steps: Int, dis: Int) {
            var x = startX
            var y = startY
            var lastChunkX = Integer.MAX_VALUE
            var lastChunkZ = Integer.MAX_VALUE
            var lastDistanceData: ByteArray? = null
            for (step in 0 until steps) {
                val blockX = x.toInt()
                val blockZ = y.toInt()

                val chunkX = blockX shr 4
                val chunkZ = blockZ shr 4

                val distanceData = if (lastDistanceData != null && lastChunkX == chunkX && lastChunkZ == chunkZ) {
                    lastDistanceData
                } else {
                    val chunkIndex = (chunkZ - startChunkZ) * chunksX + (chunkX - startChunkX)
                    val chunkEdgeData = chunkCache[chunkIndex] ?: EdgeDistanceChunkData().apply { chunkCache[chunkIndex] = this }
                    chunkEdgeData.distanceData.computeIfAbsent(edgeId) { ByteArray(16 * 16) }
                }

                lastChunkX = chunkX
                lastChunkZ = chunkZ
                lastDistanceData = distanceData

                val blockIndex = (blockZ and 0xf) * 16 + (blockX and 0xf)
                val currentDis = distanceData[blockIndex]

                if (currentDis == EdgeDistanceChunkData.NoDistance || dis < (currentDis.toInt() and 0xff)) {
                    distanceData[blockIndex] = dis.toByte()
                }

                // Increment for next block
                y += stepY
                x++
            }
        }

        for (edge in view.edges) {
            val start = edge.line.start.toDouble()
            val end = edge.line.end.toDouble()

            val dx = end.x - start.x
            val dy = end.y - start.y

            val stepY = dy / dx
            val steps = (dy / stepY).roundToInt()

            processLine(edge.id, start.x, start.y, stepY, steps, 0)

            val length = Math.sqrt(dx * dx + dy * dy)

            val sin = dy / length
            val cos = dx / length

            for (left in 1..this.sideIterations) {
                val startX = start.x - sin * 1 * left
                val startY = start.y + cos * 1 * left
                processLine(edge.id, startX, startY, stepY, steps, left)
            }

            for (right in 1..this.sideIterations) {
                val startX = start.x + sin * 1 * right
                val startY = start.y - cos * 1 * right
                processLine(edge.id, startX, startY, stepY, steps, right)
            }
        }

        for (relX in 0 until chunksX) {
            for (relZ in 0 until chunksZ) {
                chunkCache[relZ * chunksX + relX]?.let { data ->
                    view.map.getChunk(startChunkX + relX, startChunkZ + relZ)[EdgeDistanceData] = data
                }
            }
        }
    }

    companion object {

        val EdgeDistanceData = DataKey<EdgeDistanceChunkData>()
    }
}
