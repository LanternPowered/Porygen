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

import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddVec3ToDoubleSpec
import org.lanternpowered.porygen.value.ConstantDouble
import org.lanternpowered.porygen.value.ConstantInt
import org.lanternpowered.porygen.value.ConstantLong

val DefaultNodeGraphSpec = NodeGraphSpec {
  data {
    // Primitives
    conversion(Int::toDouble)
    conversion(Int::toFloat)
    conversion(Int::toLong)
    conversion(Long::toInt)
    conversion(Long::toDouble)
    conversion(Long::toFloat)
    conversion(Float::toInt)
    conversion(Float::toDouble)
    conversion(Float::toLong)
    conversion(Double::toInt)
    conversion(Double::toFloat)
    conversion(Double::toLong)

    // Constant suppliers
    conversion(::ConstantInt)
    conversion(::ConstantDouble)
    conversion(::ConstantLong)
  }

  // Specs
  nodeSpec(AddDoubleSpec)
  nodeSpec(AddVec2ToDoubleSpec)
  nodeSpec(AddVec3ToDoubleSpec)
}
