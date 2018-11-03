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
import kotlin.random.Random

/**
 * A processor which will handle the given [CellMapView].
 *
 * Processing will always be done for a map section, to
 * get consistent results. Data may be stored in various
 * map elements.
 */
interface CellMapViewProcessor {

  /**
   * Processes the given [CellMapView]s.
   *
   * @param context The context
   * @param view The cell map view to process
   */
  fun process(context: GeneratorContext, view: CellMapView, random: Random)
}
