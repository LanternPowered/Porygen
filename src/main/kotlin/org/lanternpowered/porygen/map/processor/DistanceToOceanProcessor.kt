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

import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMapElement
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.Corner
import org.spongepowered.math.GenericMath
import kotlin.math.abs

/**
 * Calculates the distance of cells to the ocean. This also
 * determines where the coastline is.
 */
class DistanceToOceanProcessor(
    private val maxOceanCellDistance: Int = 5,
    private val maxOceanCornerDistance: Int = maxOceanCellDistance + 1
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    fun Cell.isOcean(): Boolean =
        this[DataKeys.IS_OCEAN] == true

    for (cell in view.cells)
      println(cell.neighbors.count())

    for (cell in view.cells)
      updateOceanDistance(cell, view, Cell::neighbors, Cell::isOcean, maxOceanCellDistance)

    // Corners at the coastline are considered "ocean",
    // so they get a 0 distance in output
    fun Corner.isOcean(): Boolean =
        cells.any { cell -> cell.isOcean() }

    for (corner in view.corners)
      updateOceanDistance(corner, view, Corner::neighbors, Corner::isOcean, maxOceanCornerDistance)
  }

  private fun <T : CellMapElement> updateOceanDistance(
      element: T,
      view: CellMapView,
      neighbors: T.() -> Collection<T>,
      isOcean: T.() -> Boolean,
      allowedDistance: Int,
      processStack: Set<T> = setOf()
  ): Int? {
    // Already processed before
    var distance = element[DataKeys.DISTANCE_TO_OCEAN]
    if (distance != null && distance <= processStack.size)
      return distance
    // We reached the maximum allowed distance (complexity)
    if (processStack.size == allowedDistance)
      return null

    fun findNeighborDistance(): Int? {
      // Try to scan for neighbor cells
      val newProcessed = processStack + element
      // Find the shortest distance
      val value = element.neighbors().asSequence()
          .filter { neighbor -> neighbor !in processStack } // Prevent infinite loops
          .map { neighbor -> updateOceanDistance(neighbor, view, neighbors, isOcean, allowedDistance, newProcessed) }
          .filterNotNull()
          .sortedBy { abs(it) } // Find the closest distance to 0
          .firstOrNull()
      return if (value != null) GenericMath.clamp(value, -allowedDistance, allowedDistance) else null
    }

    // Check if the cell is at the coastline
    distance = if (element.isOcean()) {
      // If any neighbor is land, this is next to the coastline,
      // cells next to the coastline get a distance of 0
      if (element.neighbors().any { neighbor -> !neighbor.isOcean() }) {
        0
      } else {
        // Find the shortest distance
        val min = findNeighborDistance()
        if (min == null) null else min - 1
      }
    } else {
      // If any neighbor is ocean, this is the coastline, the
      // coastline cells get a distance of 1
      if (element.neighbors().any { neighbor -> neighbor.isOcean() }) {
        1
      } else {
        // Find the shortest distance
        val min = findNeighborDistance()
        if (min == null) null else min + 1
      }
    }

    if (distance != null)
      element[DataKeys.DISTANCE_TO_OCEAN] = distance

    return distance
  }
}
