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
package org.lanternpowered.porygen.api.points.random

import com.flowpowered.math.vector.Vector2d
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.util.IntArrays
import org.lanternpowered.porygen.api.util.geom.Rectangled
import java.util.*

/**
 * @property columns The amount of columns in the grid
 * @property rows The amount of rows in the grid
 */
class GridBasedRandomPointsGenerator(
        var columns: Int = 10,
        var rows: Int = 10
) : AbstractRandomPointsGenerator() {

    fun setGrid(columns: Int, rows: Int) = apply {
        this.columns = columns
        this.rows = rows
    }

    override fun generatePoints(context: GeneratorContext, rectangle: Rectangled): List<Vector2d> {
        val random = context.random
        val points = ArrayList<Vector2d>()

        val min = this.points.start
        val max = this.points.endInclusive

        // Randomize the amount of points that will be generated
        val amount = min + random.nextInt(max - min + 1)

        val minX = rectangle.min.x
        val minY = rectangle.min.y
        val maxX = rectangle.max.x
        val maxY = rectangle.max.y

        val dX = maxX - minX
        val dY = maxY - minY

        // Calculate the distance between each grid line
        val xLineDiff = dX / this.columns
        val yLineDiff = dY / this.rows

        fun addCellPoint(cell: Int) {
            // Get the coordinates from the point index
            val px = (cell % this.columns).toDouble()
            val py = (cell / this.rows).toDouble()
            // Calculate point coordinates within the cell
            val x = minX + xLineDiff * px + xLineDiff / 2.0
            val y = minY + yLineDiff * py + yLineDiff / 2.0
            points.add(Vector2d(x, y))
        }

        val gridPointsAmount = this.columns * this.rows
        if (amount >= gridPointsAmount) {
            // All cells have to be populated, so don't
            // go random through all the cells
            for (i in 0 until gridPointsAmount) {
                addCellPoint(i)
            }
        } else {
            val cells = IntArray(gridPointsAmount) { it }
            IntArrays.shuffle(cells, random)

            for (i in 0 until amount) {
                addCellPoint(cells[i])
            }
        }

        return points
    }
}
