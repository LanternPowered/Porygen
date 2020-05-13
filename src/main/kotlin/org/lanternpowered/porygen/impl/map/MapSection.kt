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

import org.lanternpowered.porygen.math.geom.Rectanglei

/**
 * Represents a "section" of a map. A section is a bigger area
 * that contains multiple chunks. The map is also generated per
 * section.
 *
 * So if you request a single chunk, the whole section will be
 * loaded into memory.
 */
class MapSection(
    val position: SectionPosition,
    val map: MapImpl,
    val viewRectangle: Rectanglei,
    val cells: Collection<CellImpl>,
    val corners: Collection<CornerImpl>,
    val edges: Collection<EdgeImpl>
) {

  /**
   * The current state of the processing of the [MapSection].
   */
  var processorState: Int = -1

  /**
   * All the [MapViewImpl]s that hold a reference to this [MapSection].
   */
  val references = mutableSetOf<MapSectionReference>()

  fun addReference(reference: MapSectionReference) {
    this.references.add(reference)
  }

  fun removeReference(reference: MapSectionReference) {
    if (!this.references.remove(reference))
      return
    // No more references, the section is
    // no longer needed
    if (this.references.isEmpty()) {
      // TODO: Remove section from map
    }
  }
}
