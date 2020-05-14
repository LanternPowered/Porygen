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
import org.lanternpowered.porygen.util.ToStringHelper

class EdgeImpl(
    override val id: Long,
    override val line: Line2i,
    override val map: MapImpl
) : SimpleDataHolder(), Edge {

  private var hashCode: Int = 0

  internal val mutableCells = mapElementSetOf<CellImpl>()
  internal val mutableCorners = mapElementSetOf<CornerImpl>()

  override fun other(cell: Cell): CellImpl {
    check(cell in mutableCells)
    return mutableCells.first { it != cell }
  }

  override val cells: Collection<CellImpl> = mutableCells
  override val corners: Collection<CornerImpl> = mutableCorners

  override fun equals(other: Any?): Boolean {
    if (other !is Edge)
      return false
    val delegate = other.delegate
    return delegate.id == this.id && delegate.map == this.map
  }

  override fun hashCode(): Int {
    if (hashCode == 0)
      hashCode = arrayOf(id, map, Edge::class).contentHashCode()
    return hashCode
  }

  override fun toString(): String = ToStringHelper(this)
      .add("id", id)
      .add("line", line)
      .add("center", line.center)
      .toString()
}
