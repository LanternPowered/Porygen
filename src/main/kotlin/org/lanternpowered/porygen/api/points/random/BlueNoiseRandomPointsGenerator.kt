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
import org.lanternpowered.porygen.api.util.geom.Rectangled
import java.awt.Color
import java.util.*

/**
 * Generates points which always have a minimal spread.
 *
 * The algorithm is inspired by
 * [Math for Game Developers - Procedural Generation (White and Blue Noise)](https://www.youtube.com/watch?v=lRfdN4L2SUg)
 */
class BlueNoiseRandomPointsGenerator : AbstractRandomPointsGenerator() {

    // The amount of cells in the x direction
    var xCells = 10
    // The amount of cells in the y direction
    var yCells = 10

    // The amount of percent (0 - 1.0) the cells should
    // cover the rectangle, based on this value will the
    // gap between the cells be calculated

    // Cell coverage in the x direction
    var xCellCoverage = 0.8
    // Cell coverage in the y direction
    var yCellCoverage = 0.8

    var debug = false

    // A array that is reused
    private val usedCells = ThreadLocal<BooleanArray>()

    fun setCells(xCells: Int, yCells: Int) {
        this.xCells = xCells
        this.yCells = yCells
    }

    fun setCellCoverage(xCellCoverage: Double, yCellCoverage: Double) {
        this.xCellCoverage = xCellCoverage
        this.yCellCoverage = yCellCoverage
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

        // Calculate the size of each gap
        val xGapSize = dX * (1.0 - this.xCellCoverage) / this.xCells
        val yGapSize = dY * (1.0 - this.yCellCoverage) / this.yCells

        // Calculate the size of each cell
        val xCellSize = dX * this.xCellCoverage / this.xCells
        val yCellSize = dY * this.yCellCoverage / this.yCells

        if (this.debug) {
            context.debugGraphics?.let { graphics ->
                val color = graphics.color
                graphics.color = Color.RED
                for (x in 0 until this.xCells) {
                    for (y in 0 until this.yCells) {
                        val startX = (minX + x * xCellSize + x * xGapSize + xGapSize / 2.0).toInt()
                        val startY = (minY + y * yCellSize + y * yGapSize + yGapSize / 2.0).toInt()
                        graphics.drawRect(startX, startY, xCellSize.toInt(), yCellSize.toInt())
                    }
                }
                graphics.color = color
            }
        }

        val pointAdder = { cell: Int ->
            // Get the coordinates from the cell index
            val cx = (cell % this.xCells).toDouble()
            val cy = (cell / this.xCells).toDouble()
            // Calculate point coordinates within the cell
            val x = minX + cx * xCellSize + cx * xGapSize + xGapSize / 2.0 + random.nextDouble() * xCellSize
            val y = minY + cy * yCellSize + cy * yGapSize + yGapSize / 2.0 + random.nextDouble() * yCellSize
            // Add the point
            points.add(Vector2d(x, y))
        }

        val cellAmount = this.xCells * this.yCells
        if (amount >= cellAmount) {
            // All cells have to be populated, so don't
            // go random through all the cells
            for (i in 0 until cellAmount) {
                pointAdder(i)
            }
        } else {
            var usedCells: BooleanArray? = this.usedCells.get()
            // Check if the used cells are initialized, or if the cell amount changed
            if (usedCells == null || usedCells.size != cellAmount) {
                usedCells = BooleanArray(cellAmount)
                this.usedCells.set(usedCells)
            } else {
                // Reset the used cells
                Arrays.fill(usedCells, false)
            }

            for (i in 0 until amount) {
                var cell: Int
                // Keep searching until a empty cell is found
                do {
                    cell = random.nextInt(cellAmount)
                } while (usedCells[cell])
                usedCells[cell] = true
                pointAdder(cell)
            }
        }

        return points
    }
}
