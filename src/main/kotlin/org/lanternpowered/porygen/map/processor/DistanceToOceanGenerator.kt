package org.lanternpowered.porygen.map.processor

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMapView
import kotlin.random.Random

/**
 * Calculates the distance of cells to the ocean.
 */
class DistanceToOceanGenerator(
    private val maxOceanDistance: Int = 5
) : CellMapViewProcessor {

  override fun process(context: GeneratorContext, view: CellMapView, random: Random) {
    val unresolvedDistance = -1

    fun findOcean(cell: Cell, allowedDistance: Int = this.maxOceanDistance, parentStack: Set<Cell> = setOf()): Int {
      var distance = cell[DataKeys.DISTANCE_TO_OCEAN]
      if (distance != null)
        return distance
      if (allowedDistance == 0)
        return unresolvedDistance
      // Find the minimum distance to the ocean
      distance = cell.neighbors.asSequence()
          .filter { neighbor -> neighbor !in parentStack }
          .map { neighbor -> findOcean(neighbor, allowedDistance - 1, parentStack + cell) }
          .filter { it != unresolvedDistance }
          .min()
      // Reached the max depth
      if (distance == null)
        return unresolvedDistance
      if (view.contains(cell))
        cell[DataKeys.DISTANCE_TO_OCEAN] = distance
      return distance
    }

    for (cell in view.cells)
      findOcean(cell)
  }
}
