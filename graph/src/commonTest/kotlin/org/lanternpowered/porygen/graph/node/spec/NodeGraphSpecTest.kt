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

import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.ConstantDoubleSpec
import org.lanternpowered.porygen.graph.specs.noise.PerlinSpec
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.noise.NoiseQuality
import org.lanternpowered.porygen.util.type.genericTypeOf
import org.lanternpowered.porygen.value.Vec2dToDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    val doubleToNoiseQuality = spec.getConversionFunction(genericTypeOf<Double>(), genericTypeOf<NoiseQuality>())
    assertNull(doubleToNoiseQuality)
  }

  @Test
  fun testConnect() {
    val spec = NodeGraphSpec {
      include(NodeGraphSpec.Default)
    }

    val graph = NodeGraph(spec)

    val const1 = graph.create(ConstantDoubleSpec)
    const1.requireProperty(ConstantDoubleSpec.value).value = 100.0
    val const1Output = const1.requirePort(ConstantDoubleSpec.output)

    val add = graph.create(AddDoubleSpec)
    val addOutput = add.requirePort(AddDoubleSpec.output)

    val perlin = graph.create(PerlinSpec, position = Vec2d(100.0, 100.0))
    assertFalse(perlin.requirePort(PerlinSpec.quality).isDataTypeAccepted(const1Output.dataType))
    assertFalse(perlin.requirePort(PerlinSpec.quality).isDataTypeAccepted(addOutput.dataType))
    assertTrue(perlin.requirePort(PerlinSpec.frequency).isDataTypeAccepted(const1Output.dataType))

    assertFalse(addOutput.connectTo(perlin.requirePort(PerlinSpec.quality)))
  }
}
