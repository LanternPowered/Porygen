/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.collections

import kotlin.test.Test
import kotlin.test.assertEquals

class Long2ObjectMapTest {

  @Test fun testGetOrPut() {
    val map = Long2ObjectOpenHashMap<String>()
    assertEquals("TEST", map.getOrPut(1L) { "TEST" })
  }
}
