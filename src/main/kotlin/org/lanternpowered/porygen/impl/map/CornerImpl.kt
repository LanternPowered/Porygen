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

import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.util.pair.packIntPair
import org.spongepowered.math.vector.Vector2i

class CornerImpl(
    override val point: Vector2i,
    override val map: MapImpl
) : SimpleDataHolder(), Corner {

  override val id: Long = packIntPair(this.point.x, this.point.y)

  internal val mutableEdges = mutableListOf<EdgeImpl>()
  internal val mutableCells = mutableListOf<CellImpl>()
  internal val mutableNeighbors = mutableListOf<CornerImpl>()

  override val edges: Collection<Edge> get() = this.mutableEdges
  override val cells: Collection<Cell> get() = this.mutableCells
  override val neighbors: Collection<Corner> get() = this.mutableNeighbors
}
