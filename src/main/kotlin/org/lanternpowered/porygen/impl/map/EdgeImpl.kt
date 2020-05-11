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
import java.util.Collections

class EdgeImpl(
    override val line: Line2i,
    override val map: MapImpl,
    val viewImpl: MapViewImpl
) : SimpleDataHolder(), Edge {

  override val id = run {
    val center = line.start.add(line.end.sub(line.start).div(2))
    packIntPair(center.x, center.y)
  }

  internal val theCells = mutableSetOf<CellImpl>()
  internal val theCorners = mutableSetOf<CornerImpl>()

  override val cells: Collection<Cell> = Collections.unmodifiableCollection(this.theCells)
  override val corners: Collection<Corner> = Collections.unmodifiableCollection(this.theCorners)
}
