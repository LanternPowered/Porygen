/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "FunctionName", "NOTHING_TO_INLINE")

package org.lanternpowered.porygen.util.pair

import kotlin.jvm.JvmInline

/**
 * Converts the [Pair] into a [FloatPair].
 */
fun Pair<Float, Float>.toFloatPair() = FloatPair(this.first, this.second)

/**
 * Packs the first and second float values into one long value.
 */
fun packFloatPair(first: Float, second: Float): Long = packIntPair(first.toRawBits(), second.toRawBits())

/**
 * Unpacks the first value from the packed float pair (long) value.
 */
fun unpackFloatPairFirst(packed: Long): Float = Float.fromBits(unpackIntPairFirst(packed))

/**
 * Unpacks the second value from the packed float pair (long) value.
 */
fun unpackFloatPairSecond(packed: Long): Float = Float.fromBits(unpackIntPairSecond(packed))

/**
 * Represents tuple with two float values.
 *
 * @property packed The packed long value
 * @property first The first value
 * @property second The second value
 */
@JvmInline
value class FloatPair(val packed: Long) {

  /**
   * Constructs a [FloatPair] with the given first and second values.
   */
  constructor(first: Float, second: Float) : this(packFloatPair(first, second))

  inline val first: Float
    get() = unpackFloatPairFirst(this.packed)

  inline val second: Float
    get() = unpackFloatPairSecond(this.packed)

  inline operator fun component1() = this.first
  inline operator fun component2() = this.second

  fun toPair(): Pair<Float, Float> =
      Pair(this.first, this.second)

  override fun toString() = "($first, $second)"
}
