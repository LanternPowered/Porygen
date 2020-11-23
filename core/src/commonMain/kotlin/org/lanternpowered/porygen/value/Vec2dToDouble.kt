/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.value

import org.lanternpowered.porygen.math.vector.Vec2d

/**
 * A function which produces a double value based on a 2D vector.
 */
interface Vec2dToDouble {

  /**
   * Gets the produced value for the given vector.
   */
  operator fun get(vec: Vec2d): Double = this[vec.x, vec.y]

  /**
   * Gets the produced value for the given vector.
   */
  operator fun get(x: Double, y: Double): Double
}
