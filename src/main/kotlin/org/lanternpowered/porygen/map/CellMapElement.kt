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

interface CellMapElement {

  /**
   * The id of this identifiable. This id is unique
   * within a specific [CellMap].
   */
  val id: Long

  /**
   * The [CellMap] this element is located in.
   */
  val map: CellMap
}