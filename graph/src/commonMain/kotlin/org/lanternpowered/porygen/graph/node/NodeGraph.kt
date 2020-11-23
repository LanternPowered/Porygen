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

/**
 * Represents a graph of nodes.
 */
interface NodeGraph : Iterable<Node> {

  /**
   * A collection with all the nodes in this graph.
   */
  val nodes: Collection<Node>

  /**
   * Creates a dynamic node. Optionally with the given spec.
   */
  fun createDynamic(spec: NodeSpec? = null): DynamicNode

  /**
   * Creates a node with the given node spec.
   */
  fun create(spec: NodeSpec): Node

  /**
   * Removes the node with the given id.
   */
  fun remove(id: NodeId): Boolean

  /**
   * Attempts to get the node with the given id.
   */
  operator fun get(id: NodeId): Node?
}
