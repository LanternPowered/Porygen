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

import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.math.geom.Line2i

val Edge.delegate: Edge
  get() = if (this is EdgeView) delegate else this

class EdgeView private constructor(
    override val delegate: Edge,
    override val view: MapViewImpl
) : MapElementView<Edge>(), Edge {

  override val line: Line2i
    get() = delegate.line

  override val cells: Collection<CellView> by lazy {
    delegate.cells.asSequence()
        .map { cell -> view.viewOf(cell) }
        .filterNotNull()
        .sortToSet()
  }

  override val corners: Collection<Corner> by lazy {
    delegate.corners.asSequence()
        .map { corner -> view.viewOf(corner) }
        .filterNotNull()
        .sortToSet()
  }

  override fun other(cell: Cell): Cell {
    val delegateCell = cell.delegate
    check(delegateCell in delegate.cells)
    return cells.first { it.delegate != delegateCell }
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Edge)
      return false
    return delegate == other.delegate
  }

  override fun hashCode(): Int =
      delegate.hashCode()

  override fun toString(): String =
      delegate.toString()

  companion object {

    fun of(delegate: Edge, view: MapViewImpl): EdgeView =
        if (delegate is EdgeView) EdgeView(delegate.delegate, view) else EdgeView(delegate, view)
  }
}
