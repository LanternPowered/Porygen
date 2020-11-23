/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.graph.specs.AddVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.ConstantDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractDoubleSpec

object NodeSpecRegistry {

  init {
    register(ConstantDoubleSpec)

    register(AddDoubleSpec)
    register(AddVec2ToDoubleSpec)
    register(AddVec3ToDoubleSpec)

    register(MultiplyDoubleSpec)
    register(MultiplyVec2ToDoubleSpec)
    register(MultiplyVec3ToDoubleSpec)

    register(SubtractDoubleSpec)
    register(SubtractVec2ToDoubleSpec)
    register(SubtractVec3ToDoubleSpec)

    //register(PerlinType)
  }

  fun register(type: NodeSpec) {

  }

  fun get(id: String): NodeSpec? {
    TODO()
  }
}
