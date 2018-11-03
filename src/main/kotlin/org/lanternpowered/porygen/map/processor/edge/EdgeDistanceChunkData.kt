/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.processor.edge

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.lanternpowered.porygen.map.Edge

class EdgeDistanceChunkData {

  internal val distanceData: Long2ObjectMap<ByteArray> = Long2ObjectOpenHashMap()

  /**
   * Attempts to get the distance to the given [Edge] or -1 if too far away.
   *
   * @param edge The edge to get the distance to
   * @param localX The local x coordinate
   * @param localZ The local z coordinate
   */
  fun getDistanceToEdge(edge: Edge, localX: Int, localZ: Int): Int {
    val distanceData = this.distanceData[edge.id] ?: return -1
    return distanceData[localZ * 16 + localX].toInt()
  }

  internal fun copy(): EdgeDistanceChunkData {
    TODO()
  }

  companion object {

    internal const val NoDistance: Byte = -1
  }
}
