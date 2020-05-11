/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map

import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator

interface CellMapGenerator {

  /**
   * The generator that is used to generate the cell map polygons.
   */
  var polygonGenerator: CellPolygonGenerator
}
