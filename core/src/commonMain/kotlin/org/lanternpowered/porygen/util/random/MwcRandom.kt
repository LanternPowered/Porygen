/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.random

import org.lanternpowered.porygen.util.takeUpperBits
import kotlin.random.Random

/**
 * A random number generator implementing the "MWC" algorithm.
 *
 * Has a period of ([A] * (2^32)^3 - 1) / 2 ≈ 2^125.
 */
class MwcRandom private constructor(
    private var x: Int,
    private var y: Int,
    private var z: Int,
    private var c: Int
) : Random() {

  private constructor(seed1: Int, seed2: Int) : this(seed1, seed2, 0, seed1.inv())

  constructor(seed: Long) : this((seed and 0xffffffffL).toInt(), (seed ushr 32).toInt())

  override fun nextInt(): Int {
    val t = A * x.toLong() + c
    x = y
    y = z
    c = (t ushr 32).toInt()
    val z1 = (t and 0xffffffffL).toInt()
    z = z1
    return z1
  }

  override fun nextBits(bitCount: Int): Int =
      nextInt().takeUpperBits(bitCount)

  companion object {

    private const val A = 916905990
  }
}
