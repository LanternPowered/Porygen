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
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.math.geom.Line2i

class EdgeImpl(
    override val id: Long,
    override val line: Line2i,
    override val map: MapImpl
) : SimpleDataHolder(), Edge {

  internal val mutableCells = mutableSetOf<CellImpl>()
  internal val mutableCorners = mutableSetOf<CornerImpl>()

  override fun other(cell: Cell): CellImpl {
    check(cell in mutableCells)
    return mutableCells.first { it != cell }
  }

  override val cells: Collection<CellImpl> = mutableCells
  override val corners: Collection<CornerImpl> = mutableCorners
}
