/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName", "NOTHING_TO_INLINE")

package org.lanternpowered.porygen.util.pair

import kotlin.jvm.JvmInline

/**
 * Converts the [Pair] into a [IntPair].
 */
fun Pair<Int, Int>.toIntPair() = IntPair(this.first, this.second)

/**
 * Packs the first and second int values into one long value.
 */
fun packIntPair(first: Int, second: Int): Long = ((first.toLong() and 0xffffffffL) shl 32) or (second.toLong() and 0xffffffffL)

/**
 * Unpacks the first value from the packed int pair (long) value.
 */
fun unpackIntPairFirst(packed: Long): Int = (packed ushr 32).toInt()

/**
 * Unpacks the second value from the packed int pair (long) value.
 */
fun unpackIntPairSecond(packed: Long): Int = (packed and 0xffffffffL).toInt()

/**
 * Represents tuple with two int values.
 *
 * @property packed The packed long value
 * @property first The first value
 * @property second The second value
 */
@JvmInline
value class IntPair(val packed: Long) {

  /**
   * Constructs a [IntPair] with the given first and second values.
   */
  constructor(first: Int, second: Int) : this(packIntPair(first, second))

  inline val first get() = unpackIntPairFirst(this.packed)
  inline val second get() = unpackIntPairSecond(this.packed)

  inline operator fun component1() = this.first
  inline operator fun component2() = this.second

  fun toPair() = Pair(this.first, this.second)

  override fun toString() = "($first, $second)"
}
