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
import org.spongepowered.math.vector.Vector2i

class CornerView private constructor(
    override val delegate: Corner,
    override val view: MapViewImpl
) : MapElementView<Corner>(), Corner {

  override val point: Vector2i
    get() = delegate.point

  override val cells: Collection<Cell> by lazy {
    delegate.cells.asSequence()
        .filter { cell -> view.contains(cell) }
        .map { cell -> CellView.of(cell, view) }
        .toList()
  }

  override val neighbors: Collection<Corner> by lazy {
    delegate.neighbors.asSequence()
        .filter { corner -> view.contains(corner) }
        .map { corner -> of(corner, view) }
        .toList()
  }

  override val edges: Collection<Edge> by lazy {
    delegate.edges.asSequence()
        .filter { edge -> view.contains(edge) }
        .map { edge -> EdgeView.of(edge, view) }
        .toList()
  }

  private val Corner.delegate: Corner
    get() = if (this is CornerView) delegate else this

  override fun equals(other: Any?): Boolean {
    if (other !is Corner)
      return false
    return delegate == other.delegate
  }

  override fun hashCode(): Int =
      delegate.hashCode()

  override fun toString(): String =
      delegate.toString()

  companion object {

    fun of(delegate: Corner, view: MapViewImpl): CornerView =
        if (delegate is CornerView) CornerView(delegate.delegate, view) else CornerView(delegate, view)
  }
}
