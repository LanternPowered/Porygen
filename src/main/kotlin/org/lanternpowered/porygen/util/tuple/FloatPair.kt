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

package org.lanternpowered.porygen.util.tuple

import java.lang.Float.floatToIntBits
import java.lang.Float.intBitsToFloat

/**
 * Converts the [Pair] into a [FloatPair].
 */
fun Pair<Float, Float>.toFloatPair() = FloatPair(this.first, this.second)

/**
 * Constructs a [FloatPair] with the given first and second values.
 */
inline fun FloatPair(first: Float, second: Float): FloatPair = FloatPair(packFloatPair(first, second))

/**
 * Packs the first and second float values into one long value.
 */
fun packFloatPair(first: Float, second: Float): Long =
    (floatToIntBits(first).toLong() shl 32) or floatToIntBits(second).toLong()

/**
 * Unpacks the first value from the packed float pair (long) value.
 */
fun unpackFloatPairFirst(packed: Long): Float = intBitsToFloat((packed shr 32).toInt())

/**
 * Unpacks the second value from the packed float pair (long) value.
 */
fun unpackFloatPairSecond(packed: Long): Float = intBitsToFloat(((packed shl 32) shr 32).toInt())

/**
 * Represents tuple with two float values.
 *
 * @property packed The packed long value
 * @property first The first value
 * @property second The second value
 */
inline class FloatPair(val packed: Long) {

  inline val first get() = unpackFloatPairFirst(this.packed)
  inline val second get() = unpackFloatPairSecond(this.packed)

  inline operator fun component1() = this.first
  inline operator fun component2() = this.second

  fun toPair() = Pair(this.first, this.second)

  override fun toString() = "($first, $second)"
}
