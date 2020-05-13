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
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapElement
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.Corner
import org.lanternpowered.porygen.map.Edge
import org.lanternpowered.porygen.math.geom.Rectanglei

/**
 * @property sections All the sections this view (partially) contains
 */
open class MapViewImpl(
    val sections: Set<MapSectionReference>,
    override val viewRectangle: Rectanglei,
    override val cells: Collection<Cell>,
    override val corners: Collection<Corner>,
    override val edges: Collection<Edge>
) : SimpleDataHolder(), CellMapView {

  override val map: CellMap = this.sections.first().get().map

  override fun getCorner(id: Long) = this.map.getCorner(id)
  override fun getCell(id: Long) = this.map.getCell(id)
  override fun getEdge(id: Long) = this.map.getEdge(id)

  override fun contains(element: CellMapElement): Boolean {
    return when(element) {
      is Cell -> element in this.cells
      is Corner -> element in this.corners
      is Edge -> element in this.edges
      else -> false
    }
  }

  override fun getSubView(rectangle: Rectanglei): CellMapView {
    check(this.viewRectangle.contains(rectangle)) {
      "The target rectangle $rectangle must be inside this map view rectangle $viewRectangle"
    }
    return this.map.getSubView(rectangle)
  }

  override fun getCell(x: Int, z: Int): Cell {
    check(this.viewRectangle.contains(x, z)) {
      "The target block coordinates ($x, $z) must be inside this map view rectangle $viewRectangle"
    }
    return this.map.getCell(x, z)
  }

  override fun release() {
    for (section in this.sections)
      section.release()
  }
}
