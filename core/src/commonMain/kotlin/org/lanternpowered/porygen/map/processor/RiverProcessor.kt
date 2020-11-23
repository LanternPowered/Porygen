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

import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.util.random.Xor128Random
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.murmurHash3
import kotlin.random.Random
import kotlin.random.nextInt

class RiverProcessor(
    private val riverChance: Double = 0.19,
    private val riverLength: IntRange = 3..15,
    override val areaOffset: Vec2d = Vec2d(0.3, 0.3)
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    // Find all the corners which are next to the ocean
    val beachCorners = view.corners
        .filter { corner -> corner[DataKeys.DISTANCE_TO_OCEAN] == 0 }

    // The chance per corner that a river may occur
    for (corner in beachCorners) {
      // Every river corner gets it own random
      val random = Xor128Random(murmurHash3(corner.id) xor view.map.seed)
      if (random.nextDouble() > riverChance)
        continue

      val expectedLength = random.nextInt(riverLength)
      val riverData = traverse(corner, random,
          RiverData(minLength = riverLength.first, expectedLength = expectedLength))

      // Was successful, apply the data to the elements
      if (riverData != null) {
        for ((distance, riverCorner) in riverData.corners.withIndex()) {
          riverCorner[DataKeys.IS_RIVER] = true
          riverCorner[DataKeys.DISTANCE_TO_RIVER_START] = distance
        }

        for ((distance, riverEdge) in riverData.edges.withIndex()) {
          riverEdge[DataKeys.IS_RIVER] = true
          riverEdge[DataKeys.DISTANCE_TO_RIVER_START] = distance
        }
      }
    }
  }

  private fun traverse(corner: Corner, random: Random, data: RiverData): RiverData? {
    if (corner in data.corners)
      return null
    val lastCorner = data.corners.lastOrNull()
    // The river can't go back to the ocean
    if (lastCorner != null) {
      val distance = corner[DataKeys.DISTANCE_TO_OCEAN] ?: 0
      // River can only go land inwards
      if (distance <= 0)
        return null
      val lastDistance = lastCorner.require(DataKeys.DISTANCE_TO_OCEAN)
      // Rivers can only go up hill, or stay on the same level
      if (lastDistance > distance)
        return null
    }
    val edges = if (lastCorner != null) {
      // Add the common edge between the last corner and the current one
      data.edges + lastCorner.edges.first { edge -> edge in corner.edges }
    } else data.edges
    val corners = data.corners + corner
    @Suppress("NAME_SHADOWING")
    val data = data.copy(corners = corners, edges = edges)
    if (data.edges.size == data.expectedLength)
      return data

    // Try to traverse to neighbor corners
    val neighborData = corner.neighbors.shuffled(random)
        .asSequence()
        .sortedBy { neighbor -> -(neighbor[DataKeys.DISTANCE_TO_OCEAN] ?: 0) }
        .map { neighbor -> traverse(neighbor, random, data) }
        .filterNotNull()
        .sortedBy { -(it.corners.lastOrNull()?.get(DataKeys.DISTANCE_TO_OCEAN) ?: 0) }
        .firstOrNull()
    if (neighborData != null)
      return neighborData
    // Couldn't traverse to the neighbor, so return the current
    // data, but only if the minimum river length is possible
    return if (data.edges.size >= data.minLength) data else null
  }

  private data class RiverData(
      val minLength: Int,
      val expectedLength: Int,
      val corners: List<Corner> = listOf(),
      val edges: List<Edge> = listOf()
  )
}
