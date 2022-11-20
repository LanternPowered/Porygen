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
 * A module which produces a value.
 *
 * Every [DoubleSupplier] is also a [Vec2dToDouble] and [Vec3dToDouble], it will just produce the
 * same value for each coordinate.
 */
interface DoubleSupplier : Vec2dToDouble, Vec3dToDouble {

  /**
   * Gets the produced value.
   */
  fun get(): Double

  /**
   * Always returns the value of [get].
   */
  override fun get(x: Double, y: Double): Double = get()

  /**
   * Always returns the value of [get].
   */
  override fun get(x: Double, y: Double, z: Double): Double = get()
}
