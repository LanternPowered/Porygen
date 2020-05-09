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
package org.lanternpowered.porygen.api.points

import com.flowpowered.math.vector.Vector2d
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.util.geom.Rectangled
import java.util.*

/**
 * This generator generates points for a specified world, the points are generated
 * based of the chunk coordinates and the world seed resulting in always the same
 * results when the points generator is re-run.
 *
 * When generating underlying points, chunks are grouped into sections
 * and for this section will the underlying algorithm be executed, if the point
 * lies outside of the original rectangle, it will be filtered out.
 */
class ChunkBasedPointsGenerator(
        private val parent: PointsGenerator
) : PointsGenerator {

    var chunksPerXSection = 16
    var chunksPerYSection = 16

    fun setChunksPerSection(chunksPerXSection: Int, chunksPerYSection: Int) = apply {
        this.chunksPerXSection = chunksPerXSection
        this.chunksPerYSection = chunksPerYSection
    }

    override fun generatePoints(context: GeneratorContext, rectangle: Rectangled): List<Vector2d> {
        val points = ArrayList<Vector2d>()

        var minX = rectangle.min.floorX shr 4
        minX -= minX % this.chunksPerXSection
        var minY = rectangle.min.floorY shr 4
        minY -= minY % this.chunksPerYSection
        var maxX = rectangle.max.floorX shr 4
        maxX -= maxX % this.chunksPerXSection
        var maxY = rectangle.max.floorY shr 4
        maxY -= maxY % this.chunksPerYSection

        val seed = context.seed
        var x = minX
        while (x <= maxX) {
            var y = minY
            while (y <= maxY) {
                val xStart = x shl 4
                val yStart = y shl 4
                val xEnd = x + this.chunksPerXSection - 1 shl 4 or 0xf
                val yEnd = y + this.chunksPerYSection - 1 shl 4 or 0xf

                val chunkArea = Rectangled(xStart.toDouble(), yStart.toDouble(), xEnd.toDouble(), yEnd.toDouble())
                context.random.setSeed(x.toLong() * 341873128712L + y.toLong() * 132897987541L xor seed)

                // When generating points for the edge chunks, points may fall
                // outside the scope of the original rectangle, filter out those
                if (x == minX || y == minY || x == maxX || y == maxY) {
                    this.parent.generatePoints(context, chunkArea).stream()
                            .filter { rectangle.contains(it) }
                            .forEach { points.add(it) }
                } else {
                    points.addAll(this.parent.generatePoints(context, chunkArea))
                }
                y += this.chunksPerYSection
            }
            x += this.chunksPerXSection
        }

        return points
    }
}
