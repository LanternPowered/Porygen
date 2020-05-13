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

import org.spongepowered.math.vector.Vector2i

/**
 * Represents a [CellMapView] which can be grown.
 */
interface GrowableCellMapView : CellMapView {

  /**
   * Grows the [GrowableCellMapView] and returns a new [GrowableCellMapView] with new size.
   *
   * The [size] will be applied in every direction, +x, -x, +y, -y
   *
   * This doesn't release the original [GrowableCellMapView].
   */
  fun grow(size: Vector2i): GrowableCellMapView
}
