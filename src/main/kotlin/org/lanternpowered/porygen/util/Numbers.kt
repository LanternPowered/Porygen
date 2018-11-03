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

package org.lanternpowered.porygen.util

import org.spongepowered.math.GenericMath

inline fun Float.floorToInt(): Int = GenericMath.floor(this)
inline fun Double.floorToInt(): Int = GenericMath.floor(this)

inline fun Float.floorToLong(): Long = GenericMath.floorl(this)
inline fun Double.floorToLong(): Long = GenericMath.floorl(this)

fun Float.ceilToInt(): Int = -GenericMath.floor(-this)
fun Double.ceilToInt(): Int = -GenericMath.floor(-this)

fun Float.ceilToLong(): Long = -GenericMath.floorl(-this)
fun Double.ceilToLong(): Long = -GenericMath.floorl(-this)
