/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.gen.polygon

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.math.geom.Rectangled

/**
 * This generator provides [CellPolygon]s which will be used to construct [Cell]s.
 */
interface CellPolygonGenerator {

  /**
   * Generates [CellPolygon]s for the given input point [Vector2d]s.
   *
   * @param context The context
   * @return The output centered polygons
   */
  fun generate(context: GeneratorContext, rectangle: Rectangled): Collection<CellPolygon>
}
