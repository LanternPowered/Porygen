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

import org.lanternpowered.porygen.value.ConstantDouble
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GenericTypeTest {

  @Test
  fun test() {
    assertTrue(genericTypeOf<Any>().isSupertypeOf<Any>())
    assertTrue(genericTypeOf<Any?>().isSupertypeOf<Any>())
    assertFalse(genericTypeOf<Any>().isSupertypeOf<Any?>())

    assertTrue(genericTypeOf<Vec2dToDouble>().isSupertypeOf<ConstantDouble>())
    assertTrue(genericTypeOf<Vec2dToDouble?>().isSupertypeOf<ConstantDouble>())
    assertFalse(genericTypeOf<Vec2dToDouble>().isSupertypeOf<ConstantDouble?>())
    assertTrue(genericTypeOf<Vec2dToDouble?>().isSupertypeOf<ConstantDouble?>())
  }
}
