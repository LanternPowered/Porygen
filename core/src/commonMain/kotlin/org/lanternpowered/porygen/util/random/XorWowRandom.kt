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
 * A random number generator implementing the "xorwow" algorithm.
 *
 * Has a period of 2^192 - 2^32.
 */
class XorWowRandom private constructor(
  private var x: Int,
  private var y: Int,
  private var z: Int,
  private var w: Int,
  private var v: Int,
  private var d: Int,
) : Random() {

  private constructor(seed1: Int, seed2: Int) :
    this(seed1, seed2, 0, 0, seed1.inv(), (seed1 shl 10) xor (seed2 ushr 4))

  constructor(seed: Long) : this((seed and 0xffffffffL).toInt(), (seed ushr 32).toInt())

  override fun nextInt(): Int {
    val x0 = x
    val t = x0 xor (x0 ushr 2)
    x = y
    z = w
    val v0 = v
    w = v0
    val v1 = (v0 xor (v0 shl 4)) xor (t xor (t shl 1))
    v = v1
    val d1 = d + 362437
    d = d1
    return d1 + v1
  }

  override fun nextBits(bitCount: Int): Int =
    nextInt().takeUpperBits(bitCount)
}
