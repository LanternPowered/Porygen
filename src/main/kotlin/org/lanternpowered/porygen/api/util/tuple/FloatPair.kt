/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "FunctionName", "NOTHING_TO_INLINE")

package org.lanternpowered.porygen.api.util.tuple

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
