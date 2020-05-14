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
import org.lanternpowered.porygen.math.geom.Polygond
import org.spongepowered.math.vector.Vector2i

val Cell.delegate: Cell
  get() = if (this is CellView) delegate else this

class CellView private constructor(
    override val delegate: Cell,
    override val view: MapViewImpl
) : MapElementView<Cell>(), Cell {

  override val centerPoint: Vector2i
    get() = delegate.centerPoint

  override val polygon: Polygond
    get() = delegate.polygon

  override val neighbors: Collection<Cell> by lazy {
    delegate.neighbors.asSequence()
        .map { neighbor -> view.viewOf(neighbor) }
        .filterNotNull()
        .toList()
  }

  override val edges: Collection<Edge> by lazy {
    delegate.edges.asSequence()
        .map { edge -> view.viewOf(edge) }
        .filterNotNull()
        .toList()
  }

  override val corners: Collection<Corner> by lazy {
    delegate.corners.asSequence()
        .map { corner -> view.viewOf(corner) }
        .filterNotNull()
        .toList()
  }

  override fun contains(x: Int, y: Int): Boolean =
      delegate.contains(x, y)

  override fun equals(other: Any?): Boolean {
    if (other !is Cell)
      return false
    return delegate == other.delegate
  }

  override fun hashCode(): Int =
      delegate.hashCode()

  override fun toString(): String =
      delegate.toString()

  companion object {

    fun of(delegate: Cell, view: MapViewImpl): CellView =
        if (delegate is CellView) CellView(delegate.delegate, view) else CellView(delegate, view)
  }
}
