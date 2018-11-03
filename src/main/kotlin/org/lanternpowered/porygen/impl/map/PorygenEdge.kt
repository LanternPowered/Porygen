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
import org.lanternpowered.porygen.util.tuple.packIntPair
import java.util.Collections
import java.util.HashSet

class PorygenEdge(
    override val line: Line2i,
    override val map: PorygenMap
) : SimpleDataHolder(), Edge {

  override val id = packIntPair(this.line.end.x - this.line.start.x, this.line.end.y - this.line.start.y)

  internal val theCells = HashSet<PorygenCell>()
  internal val theCorners = HashSet<PorygenCorner>()

  override val cells: Collection<Cell> = Collections.unmodifiableCollection(this.theCells)
  override val corners: Collection<Corner> = Collections.unmodifiableCollection(this.theCorners)
}
