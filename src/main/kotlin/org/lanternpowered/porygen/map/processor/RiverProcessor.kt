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
import org.spongepowered.math.vector.Vector2d
import kotlin.random.Random
import kotlin.random.nextInt

class RiverProcessor(
    private val riverChance: Double = 0.15,
    private val riverLength: IntRange = 3..5,
    override val areaOffset: Vector2d = Vector2d(0.3, 0.3)
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    // Find all the corners which are next to the ocean
    val beachCorners = view.corners
        .filter { corner -> corner[DataKeys.DISTANCE_TO_OCEAN] == 0 }

    // The chance per corner that a river may occur
    for (corner in beachCorners) {
      // Every river corner gets it own random
      val random = Xor128Random(corner.id xor view.map.seed)
      if (random.nextDouble() > riverChance)
        continue

      val expectedLength = random.nextInt(riverLength)
      val riverData = traverse(corner, random,
          RiverData(minLength = riverLength.first, expectedLength = expectedLength))

      // Was successful, apply the data to the elements
      if (riverData != null) {
        for (riverCorner in riverData.corners)
          riverCorner[DataKeys.IS_RIVER] = true
        for (riverEdge in riverData.edges)
          riverEdge[DataKeys.IS_RIVER] = true
      }
    }
  }

  private fun traverse(corner: Corner, random: Random, data: RiverData): RiverData? {
    if (corner in data.corners || corner[DataKeys.IS_OCEAN] == true)
      return null
    val lastCorner = data.corners.lastOrNull()
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
    val neighborData = corner.neighbors.asSequence()
        .map { neighbor -> traverse(neighbor, random, data) }
        .filterNotNull()
        // The longest river comes first
        .sortedBy { neighborData -> -neighborData.edges.size }
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
