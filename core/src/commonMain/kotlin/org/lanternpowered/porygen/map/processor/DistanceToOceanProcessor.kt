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
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.math.vector.Vec2d
import kotlin.math.abs

/**
 * Calculates the distance of cells to the ocean. This also
 * determines where the coastline is.
 */
class DistanceToOceanProcessor(
  private val maxOceanCellDistance: Int = 5,
  private val maxOceanCornerDistance: Int = maxOceanCellDistance + 3,
  override val areaOffset: Vec2d = Vec2d(0.3, 0.3)
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    for (cell in view.cells)
      updateCellOceanDistance(cell, maxOceanCellDistance)

    for (corner in view.corners)
      updateCornerOceanDistance(corner, maxOceanCornerDistance)
  }

  private fun Cell.isOcean(): Boolean =
    this[DataKeys.IS_OCEAN] == true

  private fun Corner.getType(): CornerType {
    val count = cells.count { cell -> cell.isOcean() }
    return when {
      count == 0 -> CornerType.Land
      count < cells.size -> CornerType.Coast
      else -> CornerType.Ocean
    }
  }

  private enum class CornerType {
    Land,
    Coast,
    Ocean
  }

  private fun updateCornerOceanDistance(
    element: Corner, allowedDistance: Int, processStack: Set<Corner> = setOf()
  ): Int? {
    val stored = element[DataKeys.DISTANCE_TO_OCEAN]
    if (stored == 0 || stored == 1 || stored == -1)
      return stored

    // We reached the maximum allowed distance (complexity)
    if (processStack.size == allowedDistance)
      return null

    fun findNeighborDistance(): Int? {
      // Try to scan for neighbor corners
      val newProcessed = processStack + element
      // Find the shortest distance
      val value = element.neighbors.asSequence()
        .filter { neighbor -> neighbor !in processStack } // Prevent infinite loops
        .map { neighbor -> updateCornerOceanDistance(neighbor, allowedDistance, newProcessed) }
        .filterNotNull()
        .sortedBy { abs(it) } // Find the closest distance to 0
        .firstOrNull()
      return value?.coerceIn(-allowedDistance, allowedDistance)
    }

    val type = element.getType()
    val distance = when (type) {
      CornerType.Land -> {
        val value = findNeighborDistance()
        if (value == null) null else value + 1
      }
      CornerType.Ocean -> {
        val value = findNeighborDistance()
        if (value == null) null else value - 1
      }
      CornerType.Coast -> 0
    }

    if (stored != null && (distance == null || abs(stored) <= abs(distance)))
      return stored

    if (distance != null) {
      element[DataKeys.DISTANCE_TO_OCEAN] = distance
    } else if (stored == null) {
      // Insert a fallback value, can be overwritten later
      element[DataKeys.DISTANCE_TO_OCEAN] = when (type) {
        CornerType.Land -> allowedDistance
        CornerType.Ocean -> -allowedDistance
        CornerType.Coast -> throw IllegalStateException()
      }
    }

    return distance
  }

  private fun updateCellOceanDistance(element: Cell, allowedDistance: Int, processStack: Set<Cell> = setOf()): Int? {
    // We reached the maximum allowed distance (complexity)
    if (processStack.size == allowedDistance)
      return null

    fun findNeighborDistance(): Int? {
      // Try to scan for neighbor cells
      val newProcessed = processStack + element
      // Find the shortest distance
      val value = element.neighbors.asSequence()
        .filter { neighbor -> neighbor !in processStack } // Prevent infinite loops
        .map { neighbor -> updateCellOceanDistance(neighbor, allowedDistance, newProcessed) }
        .filterNotNull()
        .sortedBy { abs(it) } // Find the closest distance to 0
        .firstOrNull()
      return value?.coerceIn(-allowedDistance, allowedDistance)
    }

    // Check if the cell is at the coastline
    val distance = if (element.isOcean()) {
      // If any neighbor is land, this is next to the coastline,
      // cells next to the coastline get a distance of 0
      if (element.neighbors.any { neighbor -> !neighbor.isOcean() }) {
        0
      } else {
        // Find the shortest distance
        val min = findNeighborDistance()
        if (min == null) null else min - 1
      }
    } else {
      // If any neighbor is ocean, this is the coastline, the
      // coastline cells get a distance of 1
      if (element.neighbors.any { neighbor -> neighbor.isOcean() }) {
        1
      } else {
        // Find the shortest distance
        val min = findNeighborDistance()
        if (min == null) null else min + 1
      }
    }

    if (distance != null) {
      element[DataKeys.DISTANCE_TO_OCEAN] = distance
    } else if (element[DataKeys.DISTANCE_TO_OCEAN] == null) {
      // Insert a fallback value, can be overwritten later
      element[DataKeys.DISTANCE_TO_OCEAN] = if (element.isOcean()) -allowedDistance else allowedDistance
    }

    return distance
  }
}
