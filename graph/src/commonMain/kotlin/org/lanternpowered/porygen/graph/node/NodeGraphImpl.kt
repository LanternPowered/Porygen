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

import org.lanternpowered.porygen.graph.node.port.InputPortImpl
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpecImpl
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.collections.Int2ObjectOpenHashMap
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection

internal class NodeGraphImpl(
  val spec: NodeGraphSpecImpl
) : NodeGraph {

  private val byId = Int2ObjectOpenHashMap<Node>()
  private var idCounter = 0

  override val nodes: Collection<Node> =
    byId.values.asUnmodifiableCollection()
  
  override fun iterator(): Iterator<Node> =
    nodes.iterator()

  private fun allocId(): NodeId =
    NodeId(idCounter++)

  private fun add(node: Node) {
    byId[node.id.value] = node
  }

  override fun createDynamic(spec: NodeSpec?, title: String?, position: Vec2d): DynamicNode {
    val id = allocId()
    val titleOrDefault = title ?: spec?.title ?: "Node $id"
    val node = DynamicNodeImpl(id, titleOrDefault, position, this, spec)
    add(node)
    return node
  }

  override fun create(spec: NodeSpec, position: Vec2d): Node =
    create(allocId(), spec, position)

  fun create(id: NodeId, spec: NodeSpec, position: Vec2d): Node {
    val node = SpecNodeImpl(id, spec.title, position, this, spec)
    add(node)
    return node
  }

  override fun remove(id: NodeId): Boolean {
    val node = byId.remove(id.value)
      ?: return false
    for (output in node.outputs)
      output.disconnectFromAll()
    for (input in node.inputs)
      (input as InputPortImpl).disconnect()
    return true
  }

  override fun get(id: NodeId): Node? = byId[id.value]
}
