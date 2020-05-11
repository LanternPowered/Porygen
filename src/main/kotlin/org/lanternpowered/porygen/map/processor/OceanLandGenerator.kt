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

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.CellMapView
import org.spongepowered.noise.module.Module
import kotlin.random.Random

class OceanLandGenerator(
    private val terrainHeightModule: Module
) : CellMapViewProcessor {

  override fun process(context: GeneratorContext, view: CellMapView, random: Random) {
    for (cell in view.cells) {
      val point = cell.centerPoint

      val x = point.x.toDouble()
      val z = point.y.toDouble()

      val terrainHeight = this.terrainHeightModule.getValue(x, 1.0, z)
      // Everything below 0 is an ocean, the depth or
      // height for hills will be determined later
      cell[DataKeys.IS_OCEAN] = terrainHeight < 0.0
    }
  }
}
