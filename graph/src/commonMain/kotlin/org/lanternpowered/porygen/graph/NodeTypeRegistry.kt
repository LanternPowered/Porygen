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

import org.lanternpowered.porygen.graph.type.Add2Type
import org.lanternpowered.porygen.graph.type.Add3Type
import org.lanternpowered.porygen.graph.type.AddType
import org.lanternpowered.porygen.graph.type.Multiply2Type
import org.lanternpowered.porygen.graph.type.Multiply3Type
import org.lanternpowered.porygen.graph.type.MultiplyType
import org.lanternpowered.porygen.graph.type.PerlinType
import org.lanternpowered.porygen.graph.type.Subtract2Type
import org.lanternpowered.porygen.graph.type.Subtract3Type
import org.lanternpowered.porygen.graph.type.SubtractType

object NodeTypeRegistry {

  init {
    register(AddType)
    register(Add2Type)
    register(Add3Type)

    register(MultiplyType)
    register(Multiply2Type)
    register(Multiply3Type)

    register(SubtractType)
    register(Subtract2Type)
    register(Subtract3Type)

    register(PerlinType)
  }

  fun register(type: NodeType) {

  }

  fun get(id: String): NodeType? {
    TODO()
  }
}
