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
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

class GridBasedPointsGenerator(
    private val amount: IntRange,
    private val gridSize: Vector2i = getDefaultCells(amount)
) : PointsGenerator {

  override fun generate(random: Random): List<Vector2d> {
    // Randomize the amount of points that will be generated
    val amount = random.nextInt(this.amount)

    // Calculate the distance between each grid line
    val xLineDiff = 1.0 / this.gridSize.x
    val yLineDiff = 1.0 / this.gridSize.y

    val xHalfLineDiff = xLineDiff / 2.0
    val yHalfLineDiff = yLineDiff / 2.0

    val points = mutableListOf<Vector2d>()
    fun addCellPoint(cell: Int) {
      // Get the coordinates from the point index
      val px = (cell % this.gridSize.y).toDouble()
      val py = (cell / this.gridSize.y).toDouble()
      // Calculate point coordinates within the cell
      val x = xLineDiff * px + xHalfLineDiff
      val y = yLineDiff * py + yHalfLineDiff
      points.add(Vector2d(x, y))
    }

    val gridPointsAmount = this.gridSize.x * this.gridSize.y
    if (amount >= gridPointsAmount) {
      // All cells have to be populated, so don't
      // go random through all the cells
      for (i in 0 until gridPointsAmount)
        addCellPoint(i)
    } else {
      val cells = IntArray(gridPointsAmount) { it }
      IntArrays.shuffle(cells, random)

      for (i in 0 until amount)
        addCellPoint(cells[i])
    }

    return points
  }

  companion object {

    fun getDefaultCells(amount: IntRange): Vector2i {
      val size = ceilToInt(sqrt(amount.last.toDouble())) + 4
      return Vector2i(size, size)
    }
  }
}
