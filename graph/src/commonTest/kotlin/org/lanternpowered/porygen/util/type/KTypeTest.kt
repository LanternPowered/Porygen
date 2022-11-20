/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.type

import org.lanternpowered.porygen.value.DoubleSupplier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KTypeTest {

  @Test
  fun testCreateKType() {
    val kType = DoubleSupplier::class.createType(nullable = true)
    println(kType)
    assertEquals(DoubleSupplier::class, kType.classifier)
    assertEquals(emptyList(), kType.arguments)
    assertTrue(kType.isMarkedNullable)
  }
}
