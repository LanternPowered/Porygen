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

import kotlin.math.PI

fun floorToInt(value: Float): Int {
  val v = value.toInt()
  return if (value < v) v - 1 else v
}

fun floorToInt(value: Double): Int {
  val v = value.toInt()
  return if (value < v) v - 1 else v
}

fun floorToLong(value: Float): Long {
  val v = value.toLong()
  return if (value < v) v - 1L else v
}

fun floorToLong(value: Double): Long {
  val v = value.toLong()
  return if (value < v) v - 1L else v
}

fun ceilToInt(value: Float): Int = -floorToInt(-value)
fun ceilToInt(value: Double): Int = -floorToInt(-value)

fun ceilToLong(value: Float): Long = -floorToLong(-value)
fun ceilToLong(value: Double): Long = -floorToLong(-value)

fun degToRad(degrees: Double): Double = degrees / 180.0 * PI
fun radToDeg(radians: Double): Double = radians * 180.0 / PI
