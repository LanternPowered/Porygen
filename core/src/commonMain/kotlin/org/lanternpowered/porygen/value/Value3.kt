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

/**
 * A module which produces a value based on 3D coordinates.
 */
interface Value3 {

  /**
   * Gets the produced value at the given coordinates.
   */
  operator fun get(x: Double, y: Double, z: Double): Double
}
