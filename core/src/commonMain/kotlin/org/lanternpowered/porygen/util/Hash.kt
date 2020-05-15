/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util

fun murmurHash3(value: Long): Long {
  var x = value xor (value ushr 33)
  x *= -0xae502812aa7333L
  x = x xor (x ushr 33)
  x *= -0x3b314601e57a13adL
  x = x xor (x ushr 33)
  return x
}

fun murmurHash3(value: Int): Int {
  var x = value
  x = x xor (x ushr 16)
  x *= -0x7a143595
  x = x xor (x ushr 13)
  x *= -0x3d4d51cb
  x = x xor (x ushr 16)
  return x
}
