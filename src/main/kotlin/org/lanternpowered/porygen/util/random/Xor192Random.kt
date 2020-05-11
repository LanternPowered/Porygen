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
 * A random number generator implementing the "xor128" algorithm.
 *
 * Has a period of 2^192 - 1.
 */
class Xor192Random private constructor(
    private var x: Int,
    private var y: Int,
    private var z: Int,
    private var w: Int,
    private var v: Int,
    private var u: Int
) : Random() {

  private constructor(seed1: Int, seed2: Int) : this(seed1, seed2, 0, seed1.inv(), 0, 0)

  constructor(seed: Long) : this((seed and 0xffffffffL).toInt(), (seed ushr 32).toInt())

  override fun nextInt(): Int {
    val x0 = x
    val t = x0 xor (x0 shl 11)
    x = y
    y = z
    z = w
    w = v
    val u0 = u
    v = u0
    val u1 = (u0 xor (u0 ushr 19)) xor (t xor (t ushr 8))
    u = u1
    return u1
  }

  override fun nextBits(bitCount: Int): Int =
      nextInt().takeUpperBits(bitCount)
}
