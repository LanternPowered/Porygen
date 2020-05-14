/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.noise

interface NoiseModule {

  /**
   * Gets the value for the given x, y and z coordinates.
   */
  fun get(x: Double, y: Double, z: Double): Double
}
