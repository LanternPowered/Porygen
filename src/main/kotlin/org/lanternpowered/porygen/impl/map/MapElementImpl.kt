/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.CellMapElement

abstract class MapElementImpl : SimpleDataHolder(), CellMapElement {

  /**
   * All the sections this [MapElementImpl] is located in.
   */
  var sections = mutableSetOf<MapSection>()
}
