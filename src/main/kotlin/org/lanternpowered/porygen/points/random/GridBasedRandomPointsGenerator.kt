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
import kotlin.random.Random

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

  override fun generatePoints(context: GeneratorContext, rectangle: Rectangled, random: Random): List<Vector2d> {
    val points = mutableListOf<Vector2d>()

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
}
