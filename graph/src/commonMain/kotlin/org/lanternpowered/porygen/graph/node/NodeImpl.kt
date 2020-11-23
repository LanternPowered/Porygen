/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node

import kotlinx.serialization.Serializable
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.spec.PortSpec
import org.lanternpowered.porygen.math.vector.Vec2d

@Serializable(with = NodeSerializer::class)
class NodeImpl(
  override val id: NodeId,
  override var title: String,
  override var position: Vec2d
) : Node {

  override val inputs: List<InputPort<*>>
    get() = TODO("Not yet implemented")

  override val outputs: List<OutputPort<*>>
    get() = TODO("Not yet implemented")

  override fun port(id: PortId): Port<*>? {
    TODO("Not yet implemented")
  }

  override fun <T> port(spec: PortSpec<T>): Port<T>? {
    TODO("Not yet implemented")
  }
}
