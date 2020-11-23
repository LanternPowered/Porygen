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

import org.lanternpowered.porygen.math.vector.Vec3d

/**
 * A function which produces a double value based on a 3D vector.
 */
interface Vec3dToDouble {

  /**
   * Gets the produced value for the given vector.
   */
  operator fun get(vec: Vec3d): Double = this[vec.x, vec.y, vec.z]

  /**
   * Gets the produced value for the given vector.
   */
  operator fun get(x: Double, y: Double, z: Double): Double
}
