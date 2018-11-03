/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.points.random

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.util.IntArrays
import org.spongepowered.math.vector.Vector2d
import java.awt.Color
import kotlin.random.Random

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

  fun setCells(xCells: Int, yCells: Int) = apply {
    this.xCells = xCells
    this.yCells = yCells
  }

  fun setCellCoverage(xCellCoverage: Double, yCellCoverage: Double) = apply {
    this.xCellCoverage = xCellCoverage
    this.yCellCoverage = yCellCoverage
  }

  override fun generatePoints(context: GeneratorContext, rectangle: Rectangled, random: Random): List<Vector2d> {
    val points = ArrayList<Vector2d>()

    val min = this.points.first
    val max = this.points.last

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

    fun addCellPoint(cell: Int) {
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
      for (i in 0 until cellAmount)
        addCellPoint(i)
    } else {
      val cells = IntArray(cellAmount) { it }
      IntArrays.shuffle(cells, random)

      for (i in 0 until amount)
        addCellPoint(cells[i])
    }

    return points
  }
}
