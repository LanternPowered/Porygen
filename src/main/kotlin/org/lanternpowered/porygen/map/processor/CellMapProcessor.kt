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
import org.lanternpowered.porygen.math.vector.Vector2d

/**
 * A processor which will handle the given section [CellMapView].
 *
 * Data which was provided by previous processors may be accessed
 * in this processor.
 */
interface CellMapProcessor {

  /**
   * This offset is used in the case that the polygon generator
   * needs more map data from surrounding areas.
   *
   * Surrounding areas will only be processed up to the previous
   * processor state.
   *
   * This offset is represented as a additional percentage of
   * the processed area. For example, using (0.2, 0.2) will add
   * 20% more map data on each side.
   */
  val areaOffset: Vector2d
    get() = Vector2d.ZERO

  /**
   * Processes the given [CellMapView].
   *
   * @param view The cell map view to process
   */
  fun process(view: CellMapView)
}
