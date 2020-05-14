/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.math.geom.Polygond
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.math.ceilToInt
import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.math.vector.Vector2i
import kotlin.math.max
import kotlin.math.min

/**
 * Temporary cell data.
 */
data class CellData(
    val center: Vector2i,
    val polygon: Polygond,
    val id: Long,
    val chunks: LongSet
)

internal fun buildCellData(cellPolygon: CellPolygon): CellData {
  val center = cellPolygon.center.toInt()
  val polygon = cellPolygon.polygon

  val id = packIntPair(center.x, center.y)
  val chunks = LongOpenHashSet()

  // Generate the outer bounds of the cell, to know in
  // which chunks this cell is located.

  var minX = Double.MAX_VALUE
  var minZ = Double.MAX_VALUE
  var maxX = -Double.MAX_VALUE
  var maxZ = -Double.MAX_VALUE

  for (vertex in polygon.vertices) {
    minX = min(minX, vertex.x)
    maxX = max(maxX, vertex.x)
    minZ = min(minZ, vertex.y)
    maxZ = max(maxZ, vertex.y)
  }

  // Collect all the chunks the cell is actually located in

  val chunkStartX = floorToInt(minX) shr 4
  val chunkStartZ = floorToInt(minZ) shr 4
  val chunkEndX = ceilToInt(maxX) shr 4
  val chunkEndZ = ceilToInt(maxZ) shr 4

  for (chunkX in chunkStartX..chunkEndX) {
    for (chunkZ in chunkStartZ..chunkEndZ) {
      val chunkArea = Rectanglei(Vector2i(chunkX shl 4, chunkZ shl 4), Vector2i((chunkX + 1) shl 4, (chunkZ + 1) shl 4))
      if (polygon.contains(chunkArea) || polygon.intersects(chunkArea)) {
        chunks.add(packIntPair(chunkX, chunkZ))
      }
    }
  }

  // Make the set as small as possible
  chunks.trim()
  return CellData(center, polygon, id, chunks)
}
