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
import org.spongepowered.math.vector.Vector2d

/**
 * This generator provides [CellPolygon]s which will be used to construct [Cell]s.
 */
interface CellPolygonGenerator {

  /**
   * Generates [CellPolygon]s for the given input point [Vector2d]s.
   *
   * @param points The points
   * @return The output centered polygons
   */
  fun generate(points: Collection<Vector2d>): Collection<CellPolygon>
}
