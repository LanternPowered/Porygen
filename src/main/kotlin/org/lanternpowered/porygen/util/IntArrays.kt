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

import kotlin.random.Random

object IntArrays {

  fun shuffle(array: IntArray, random: Random = Random.Default) {
    var index: Int
    for (i in array.size - 1 downTo 1) {
      index = random.nextInt(i + 1)
      if (index != i) {
        array[index] = array[index] xor array[i]
        array[i] = array[i] xor array[index]
        array[index] = array[index] xor array[i]
      }
    }
  }
}
