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
import org.lanternpowered.porygen.util.ToStringHelper
import org.lanternpowered.porygen.math.vector.Vec2i

class CornerImpl(
    override val id: Long,
    override val point: Vec2i,
    override val map: MapImpl
) : SimpleDataHolder(), Corner {

  private var hashCode: Int = 0

  internal val mutableCells = LinkedHashSet<CellImpl>()
  internal val mutableEdges = LinkedHashSet<EdgeImpl>()
  internal val mutableNeighbors = LinkedHashSet<CornerImpl>()

  override val edges: Collection<EdgeImpl> get() = mutableEdges
  override val cells: Collection<CellImpl> get() = mutableCells
  override val neighbors: Collection<CornerImpl> get() = mutableNeighbors

  override fun toString(): String = ToStringHelper(this)
      .add("id", id)
      .add("point", point)
      .toString()

  override fun equals(other: Any?): Boolean {
    if (other !is Corner)
      return false
    val delegate = other.delegate
    return delegate.id == id && delegate.map == map
  }

  override fun hashCode(): Int {
    if (hashCode == 0)
      hashCode = arrayOf(id, map, Corner::class).contentHashCode()
    return hashCode
  }
}
