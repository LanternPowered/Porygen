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
import org.lanternpowered.porygen.map.Corner
import org.spongepowered.math.vector.Vector2i

class CornerImpl(
    override val id: Long,
    override val point: Vector2i,
    override val map: MapImpl
) : SimpleDataHolder(), Corner {

  internal val mutableEdges = mutableSetOf<EdgeImpl>()
  internal val mutableCells = mutableSetOf<CellImpl>()
  internal val mutableNeighbors = mutableSetOf<CornerImpl>()

  override val edges: Collection<EdgeImpl> get() = mutableEdges
  override val cells: Collection<CellImpl> get() = mutableCells
  override val neighbors: Collection<CornerImpl> get() = mutableNeighbors
}
