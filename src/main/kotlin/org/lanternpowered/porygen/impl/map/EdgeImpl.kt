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
import org.lanternpowered.porygen.math.geom.Line2i
import org.lanternpowered.porygen.util.pair.packIntPair

class EdgeImpl(
    override val line: Line2i,
    override val map: MapImpl
) : SimpleDataHolder(), Edge {

  override val id = run {
    val center = line.start.add(line.end.sub(line.start).div(2))
    packIntPair(center.x, center.y)
  }

  internal val mutableCells = mutableSetOf<CellImpl>()
  internal val mutableCorners = mutableSetOf<CornerImpl>()

  override fun other(cell: Cell): Cell {
    check(cell in this.mutableCells)
    return mutableCells.first { it != cell }
  }

  override val cells: Collection<Cell> = this.mutableCells
  override val corners: Collection<Corner> = this.mutableCorners
}
