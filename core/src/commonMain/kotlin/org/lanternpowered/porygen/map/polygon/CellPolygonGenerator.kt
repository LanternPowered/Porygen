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
import org.lanternpowered.porygen.math.vector.Vec2d

/**
 * This generator provides [CellPolygon]s which will be used to construct [Cell]s.
 */
interface CellPolygonGenerator {

  /**
   * This offset is used in the case that the polygon generator needs more points than the usual
   * (0, 1) range.
   */
  val areaOffset: Vec2d
    get() = Vec2d.Zero

  /**
   * Generates [CellPolygon]s for the given input points.
   *
   * @param points The points
   * @return The output cell polygons
   */
  fun generate(points: Collection<Vec2d>): Collection<CellPolygon>
}
