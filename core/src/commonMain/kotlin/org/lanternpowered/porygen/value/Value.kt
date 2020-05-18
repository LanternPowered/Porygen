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
 * Every [Value] is also a [Value2], it will just
 * produce the same value for each coordinate.
 */
interface Value : Value2, Value3 {

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
