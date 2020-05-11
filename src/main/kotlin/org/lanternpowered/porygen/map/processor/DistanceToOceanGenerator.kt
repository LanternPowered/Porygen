/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.processor

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.Corner
import kotlin.random.Random

/**
 * Calculates the distance of cells to the ocean.
 */
class DistanceToOceanGenerator(
    private val maxOceanCornerDistance: Int = 5,
    private val maxOceanCellDistance: Int = 5
) : CellMapProcessor {

  override fun process(context: GeneratorContext, view: CellMapView, random: Random) {
    for (cell in view.cells)
      findOcean(cell, view)

    for (corner in view.corners)
      findOcean(corner, view)
  }

  private fun findOcean(cell: Cell, view: CellMapView, allowedDistance: Int = this.maxOceanCellDistance, parentStack: Set<Cell> = setOf()): Int {
    if (cell[DataKeys.IS_OCEAN] == true)
      return 0
    var distance = cell[DataKeys.DISTANCE_TO_OCEAN]
    if (distance != null)
      return distance
    if (allowedDistance == 0)
      return unresolvedDistance
    // Find the minimum distance to the ocean
    distance = cell.neighbors.asSequence()
        .filter { neighbor -> neighbor !in parentStack }
        .map { neighbor -> findOcean(neighbor, view, allowedDistance - 1, parentStack + cell) }
        .filter { it != unresolvedDistance }
        .min()
    // Reached the max depth
    if (distance == null)
      return unresolvedDistance
    if (view.contains(cell))
      cell[DataKeys.DISTANCE_TO_OCEAN] = distance
    return distance
  }

  private fun findOcean(
      corner: Corner, view: CellMapView, allowedDistance: Int = this.maxOceanCornerDistance, parentStack: Set<Corner> = setOf()): Int {
    if (corner.cells.any { cell -> cell[DataKeys.IS_OCEAN] == true })
      return 0
    var distance = corner[DataKeys.DISTANCE_TO_OCEAN]
    if (distance != null)
      return distance
    if (allowedDistance == 0)
      return unresolvedDistance
    // Find the minimum distance to the ocean
    distance = corner.neighbors.asSequence()
        .filter { neighbor -> neighbor !in parentStack }
        .map { neighbor -> findOcean(neighbor, view, allowedDistance - 1, parentStack + corner) }
        .filter { it != unresolvedDistance }
        .min()
    // Reached the max depth
    if (distance == null)
      return unresolvedDistance
    if (view.contains(corner))
      corner[DataKeys.DISTANCE_TO_OCEAN] = distance
    return distance
  }

  companion object {

    private const val unresolvedDistance = -1
  }
}
