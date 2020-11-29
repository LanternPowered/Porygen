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

import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.type.GenericType

@Suppress("RedundantVisibilityModifier")
internal class DynamicNodeImpl(
  id: NodeId,
  title: String,
  position: Vec2d,
  graph: NodeGraph,
  spec: NodeSpec?
) : NodeImpl(id, title, position, graph), DynamicNode {

  init {
    if (spec != null)
      initSpec(spec)
  }

  override fun <T> addOutput(id: PortId, type: GenericType<T>): OutputPort<T> =
    createOutput(id, type)

  override fun <T> addInput(id: PortId, type: GenericType<T>, default: () -> T?): InputPort<T> =
    createInput(id, type, default)

  override fun <T> addProperty(id: PropertyId, type: GenericType<T>, value: T): Property<T> =
    createProperty(id, type, value)

  public override fun removeProperty(id: PropertyId): Boolean = super.removeProperty(id)
  public override fun removePort(id: PortId): Boolean = super.removePort(id)
}
