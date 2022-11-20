/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node.spec

import org.lanternpowered.porygen.util.type.genericTypeOf
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NodeGraphSpecTest {

  @Test
  fun testConversion() {
    val spec = NodeGraphSpec {
      include(NodeGraphSpec.Default)
    }
    spec as NodeGraphSpecImpl

    val intToDouble = spec.getConversionFunction(genericTypeOf<Int>(), genericTypeOf<Double>())
    assertNotNull(intToDouble)
    assertEquals(1.0, intToDouble(1))

    val intToVec2dToDouble = spec.getConversionFunction(genericTypeOf<Int>(), genericTypeOf<Vec2dToDouble>())
    assertNotNull(intToVec2dToDouble)
    assertEquals(1.0, intToVec2dToDouble(1)!![0.0,0.0])
  }
}
