/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.points

import org.lanternpowered.porygen.util.IntArrays
import org.lanternpowered.porygen.math.ceilToInt
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Generates points which always have a minimal spread.
 *
 * The algorithm is inspired by
 * [Math for Game Developers - Procedural Generation (White and Blue Noise)](https://www.youtube.com/watch?v=lRfdN4L2SUg)
 *
 * @property amount The amount of points that will be generated
 * @property cells The amount of cells the grid will be divided by
 * @property cellCoverage The coverage percentage (0 - 1) of each cell, the coverage is
 *   the area of a cell where a point can be generated. So, 2 * (1 - coverage) is the
 *   minimal spread between each cell.
 */
class BlueNoisePointsGenerator(
    private val amount: IntRange,
    private val cells: Vector2i = getDefaultCells(amount),
    private val cellCoverage: Vector2d = Vector2d(0.8, 0.8)
) : PointsGenerator {

  override fun generate(random: Random): List<Vector2d> {
    // Randomize the amount of points that will be generated
    val amount = random.nextInt(this.amount)

    // Calculate the size of each gap
    val xGapSize = (1.0 - this.cellCoverage.x) / this.cells.x
    val yGapSize = (1.0 - this.cellCoverage.y) / this.cells.y

    val xHalfGapSize = xGapSize / 2.0
    val yHalfGapSize = yGapSize / 2.0

    // Calculate the size of each cell
    val xCellSize = this.cellCoverage.x / this.cells.x
    val yCellSize = this.cellCoverage.y / this.cells.y

    val points = mutableListOf<Vector2d>()
    fun addCellPoint(cell: Int) {
      // Get the coordinates from the cell index
      val cx = (cell % this.cells.y).toDouble()
      val cy = (cell / this.cells.y).toDouble()
      // Calculate point coordinates within the cell
      val x = cx * xCellSize + cx * xGapSize + xHalfGapSize + random.nextDouble() * xCellSize
      val y = cy * yCellSize + cy * yGapSize + yHalfGapSize + random.nextDouble() * yCellSize
      // Add the point
      points.add(Vector2d(x, y))
    }

    val cellAmount = this.cells.x * this.cells.y
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

  companion object {

    fun getDefaultCells(amount: IntRange): Vector2i {
      val size = ceilToInt(sqrt(amount.last.toDouble())) + 1
      return Vector2i(size, size)
    }
  }
}
