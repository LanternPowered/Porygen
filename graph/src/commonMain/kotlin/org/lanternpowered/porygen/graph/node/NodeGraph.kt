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

import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpec
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpecImpl
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.math.vector.Vec2d

/**
 * Instantiates a new [NodeGraph].
 */
fun NodeGraph(spec: NodeGraphSpec = NodeGraphSpec.Default): NodeGraph =
  NodeGraphImpl(spec as NodeGraphSpecImpl)

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
  fun createDynamic(spec: NodeSpec? = null, title: String? = null, position: Vec2d = Vec2d.Zero): DynamicNode

  /**
   * Creates a node with the given node spec.
   */
  fun create(spec: NodeSpec, position: Vec2d = Vec2d.Zero): Node

  /**
   * Removes the node with the given id.
   */
  fun remove(id: NodeId): Boolean

  /**
   * Attempts to get the node with the given id.
   */
  operator fun get(id: NodeId): Node?

  /**
   * Attempts to get the node with the given id.
   */
  fun require(id: NodeId): Node =
    get(id) ?: error("There's no node with the given id: $id")
}
