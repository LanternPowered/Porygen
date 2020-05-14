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
import org.lanternpowered.porygen.math.vector.Vector2d
import kotlin.random.Random

/**
 * This generator provides [CellPolygon]s which will be used to construct [Cell]s.
 */
interface CellPolygonGenerator {

  /**
   * This offset is used in the case that the polygon generator
   * needs more points than the usual (0, 1) range.
   */
  val areaOffset: Vector2d
    get() = Vector2d.ZERO

  /**
   * Generates [CellPolygon]s for the given input points.
   *
   * @param points The points
   * @return The output cell polygons
   */
  fun generate(points: Collection<Vector2d>): Collection<CellPolygon>
}
