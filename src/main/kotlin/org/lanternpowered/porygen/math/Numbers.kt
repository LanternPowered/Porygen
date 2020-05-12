/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("NOTHING_TO_INLINE")

package org.lanternpowered.porygen.math

import org.spongepowered.math.GenericMath

inline fun floorToInt(value: Float): Int = GenericMath.floor(value)
inline fun floorToInt(value: Double): Int = GenericMath.floor(value)

inline fun floorToLong(value: Float): Long = GenericMath.floorl(value)
inline fun floorToLong(value: Double): Long = GenericMath.floorl(value)

fun ceilToInt(value: Float): Int = -GenericMath.floor(-value)
fun ceilToInt(value: Double): Int = -GenericMath.floor(-value)

fun ceilToLong(value: Float): Long = -GenericMath.floorl(-value)
fun ceilToLong(value: Double): Long = -GenericMath.floorl(-value)
