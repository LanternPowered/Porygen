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

import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.value.Vec2dToDouble

class OceanLandProcessor(
  private val terrainHeightModule: Vec2dToDouble,
  override val areaOffset: Vec2d = Vec2d(0.3, 0.3),
) : CellMapProcessor {

  override fun process(view: CellMapView) {
    for (cell in view.cells) {
      val point = cell.centerPoint

      val x = point.x.toDouble()
      val y = point.y.toDouble()

      val terrainHeight = this.terrainHeightModule[x, y]
      // Everything below 0 is an ocean, the depth or
      // height for hills will be determined later
      cell[DataKeys.IS_OCEAN] = terrainHeight < 0.0
    }
  }
}
