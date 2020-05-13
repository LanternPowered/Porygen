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
import org.lanternpowered.porygen.map.GrowableCellMapView

/**
 * A processor which will handle the given section [CellMapView].
 *
 * Data which was provided by previous processors may be accessed
 * in this processor.
 */
interface CellMapProcessor {

  /**
   * Processes the given [GrowableCellMapView].
   *
   * @param view The cell map view to process
   */
  fun process(view: GrowableCellMapView)
}
