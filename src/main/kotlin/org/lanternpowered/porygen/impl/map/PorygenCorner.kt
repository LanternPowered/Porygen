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
import org.lanternpowered.porygen.util.tuple.packIntPair
import org.spongepowered.math.vector.Vector2i
import java.util.ArrayList
import java.util.Collections

class PorygenCorner(
    override val point: Vector2i,
    override val map: PorygenMap
) : SimpleDataHolder(), Corner {

  override val id: Long = packIntPair(this.point.x, this.point.y)

  internal val theEdges = ArrayList<PorygenEdge>()
  internal val theCells = ArrayList<PorygenCell>()

  private val unmodifiableEdges = Collections.unmodifiableList(this.theEdges)
  private val unmodifiableCells = Collections.unmodifiableList(this.theCells)

  override val edges: Collection<Edge> = this.unmodifiableEdges
  override val cells: Collection<Cell> = this.unmodifiableCells
}
