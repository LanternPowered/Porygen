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

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.util.collections.Int2ObjectOpenHashMap
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection

internal class NodeGraphImpl : NodeGraph {

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

  override fun createDynamic(spec: NodeSpec?): DynamicNode {
    TODO("Not yet implemented")
  }

  override fun create(spec: NodeSpec): Node {
    TODO("Not yet implemented")
  }

  override fun remove(id: NodeId): Boolean {
    val node = byId.remove(id.value)
      ?: return false
    // TODO: Remove connections
    return true
  }

  override fun get(id: NodeId): Node? = byId[id.value]
}
