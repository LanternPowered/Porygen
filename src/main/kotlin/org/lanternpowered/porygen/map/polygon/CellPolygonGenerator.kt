/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.polygon

import org.lanternpowered.porygen.map.Cell
import kotlin.random.Random

/**
 * This generator provides [CellPolygon]s which will be used to construct [Cell]s.
 *
 * Polygon points should range between 0 (inclusive) and
 * 1 (exclusive) on the x and y axes.
 */
interface CellPolygonGenerator {

  /**
   * Generates [CellPolygon]s for the given input [Random].
   *
   * @param random The random
   * @return The output cell polygons
   */
  fun generate(random: Random): Collection<CellPolygon>
}
