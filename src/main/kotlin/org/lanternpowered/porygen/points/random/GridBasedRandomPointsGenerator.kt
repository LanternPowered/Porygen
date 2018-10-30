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
package org.lanternpowered.porygen.points.random

import com.flowpowered.math.vector.Vector2d
import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.util.geom.Rectangled

import java.util.ArrayList
import java.util.Arrays
import java.util.Random

/**
 * @property columns The amount of columns in the grid
 * @property rows The amount of rows in the grid
 */
class GridBasedRandomPointsGenerator(
        var columns: Int = 10,
        var rows: Int = 10
) : AbstractRandomPointsGenerator() {

    // A array that is reused
    private val usedPoints = ThreadLocal<BooleanArray>()

    fun setGrid(columns: Int, rows: Int) = apply {
        this.columns = columns
        this.rows = rows
    }

    override fun generatePoints(context: GeneratorContext, random: Random, rectangle: Rectangled): List<Vector2d> {
        val points = ArrayList<Vector2d>()

        val min = this.points.start
        val max = this.points.endInclusive

        // Randomize the amount of points that will be generated
        var amount = min + random.nextInt(max - min + 1)

        val minX = rectangle.min.x
        val minY = rectangle.min.y
        val maxX = rectangle.max.x
        val maxY = rectangle.max.y

        val dX = maxX - minX
        val dY = maxY - minY

        // Calculate the distance between each grid line
        val xLineDiff = dX / this.columns
        val yLineDiff = dY / this.rows

        val gridPointsAmount = this.columns * this.rows
        amount = Math.min(amount, gridPointsAmount) // There cannot be more points than the amount of points in the grid

        var usedPoints: BooleanArray? = this.usedPoints.get()
        // Check if the used points are initialized, or if the grid size changed
        if (usedPoints == null || usedPoints.size != gridPointsAmount) {
            usedPoints = BooleanArray(gridPointsAmount)
            this.usedPoints.set(usedPoints)
        } else {
            // Reset the used cells
            Arrays.fill(usedPoints, false)
        }

        for (i in 0 until amount) {
            var gridPoint: Int
            // Keep searching until a empty cell is found
            while (true) {
                gridPoint = random.nextInt(gridPointsAmount)
                if (!usedPoints[gridPoint]) {
                    break
                }
            }
            usedPoints[gridPoint] = true
            // Get the coordinates from the point index
            val px = (gridPoint % this.columns).toDouble()
            val py = (gridPoint / this.rows).toDouble()
            // Calculate point coordinates within the cell
            val x = minX + xLineDiff * px + xLineDiff / 2.0
            val y = minY + yLineDiff * py + yLineDiff / 2.0
            // Add the point
            points.add(Vector2d(x, y))
        }

        return points
    }
}
