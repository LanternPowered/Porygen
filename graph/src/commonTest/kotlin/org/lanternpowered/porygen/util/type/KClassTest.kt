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
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KClassTest {

  @Test
  fun testSuperclass() {
    assertTrue(DoubleSupplier::class.isSuperclassOf(DoubleSupplier::class))
    assertTrue(DoubleSupplier::class.isSubclassOf(DoubleSupplier::class))
    assertFalse(DoubleSupplier::class.isSuperclassOf(Vec2dToDouble::class))
    assertTrue(DoubleSupplier::class.isSubclassOf(Vec2dToDouble::class))
  }
}
