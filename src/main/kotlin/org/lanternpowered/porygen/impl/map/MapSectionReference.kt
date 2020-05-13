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

import org.lanternpowered.porygen.map.Releasable

class MapSectionReference(
    private val section: MapSection
) : Releasable {

  private var released = false

  init {
    section.addReference(this)
  }

  fun get(): MapSection {
    check(!released) { "The map section is already released." }
    return section
  }

  override fun release() {
    if (released)
      return
    released = true
    section.removeReference(this)
  }
}
