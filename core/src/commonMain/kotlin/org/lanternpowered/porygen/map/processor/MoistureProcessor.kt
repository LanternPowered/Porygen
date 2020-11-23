/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.processor

import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.value.ConstantDouble
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.math.min

/**
 * @property modifier The moisture modifier
 */
class MoistureProcessor(
  private val base: Vec2dToDouble = ConstantDouble(0.0),
  private val modifier: Vec2dToDouble = ConstantDouble(1.0),
  private val maximum: Double = 2.0,
  private val oceanNeighborFactor: Double = 0.3,
  private val oceanMaxNeighbors: Int = 1,
  private val riverNeighborFactor: Double = oceanNeighborFactor / 1.5,
  private val riverMaxNeighbors: Int = 2
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    for (cell in view.cells)
      process(cell)
  }

  private fun process(cell: Cell) {
    var moisture = 0.0
    if (cell[DataKeys.IS_OCEAN] == true) {
      moisture = maximum
    } else {
      val oceanNeighbors = cell.neighbors.count { neighbor -> neighbor[DataKeys.IS_OCEAN] == true }
      moisture += min(oceanNeighbors, oceanMaxNeighbors) * oceanNeighborFactor

      val riverNeighbors = cell.edges.count { edge -> edge[DataKeys.IS_RIVER] == true }
      moisture += min(riverNeighbors, riverMaxNeighbors) * riverNeighborFactor
    }
    val center = cell.centerPoint
    val x = center.x.toDouble()
    val y = center.y.toDouble()
    val base = base[x, y]
    val modifier = modifier[x, y]
    moisture = (base + moisture) * modifier
    moisture = moisture.coerceIn(0.0, maximum)
    moisture /= maximum
    cell[DataKeys.MOISTURE] = moisture
  }
}
