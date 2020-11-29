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

import org.lanternpowered.porygen.noise.NoiseQuality
import org.lanternpowered.porygen.value.DoubleSupplier
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object KClassTest {

  @Test
  fun testSuperclass() {
    assertTrue(DoubleSupplier::class.isSuperclassOf(DoubleSupplier::class))
    assertTrue(DoubleSupplier::class.isSubclassOf(DoubleSupplier::class))
    assertFalse(DoubleSupplier::class.isSuperclassOf(Vec2dToDouble::class))
    assertTrue(DoubleSupplier::class.isSubclassOf(Vec2dToDouble::class))
  }

  @Test
  fun testEnumValues() {
    val enumValues = NoiseQuality::class.enumValues
    assertNotNull(enumValues)
    assertTrue(NoiseQuality.Best in enumValues)
    assertTrue(NoiseQuality.Fast in enumValues)
    assertTrue(NoiseQuality.Standard in enumValues)
  }
}
