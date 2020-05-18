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

import kotlinx.serialization.Serializable
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.graph.serializer.NodeSerializer

class InputLink<T : Any>(val node: Node, val output: NodeOutput<T>)

class OutputLink<T : Any>(val node: Node, val input: NodeInput<T>)

/**
 * Represents a node.
 */
@Serializable(with = NodeSerializer::class)
class Node(val id: Int, val type: NodeType) {

  /**
   * The title of the node.
   */
  var title: String = type.title

  /**
   * The position of the node.
   */
  var position: Vector2d = Vector2d.ZERO

  /**
   * Gets the link that is made to the target input.
   */
  fun <T : Any> getLink(input: NodeInput<T>): InputLink<T>? {
    TODO()
  }

  /**
   * Gets the links that are made from the target output.
   */
  fun <T : Any> getLinks(input: NodeOutput<T>): Collection<OutputLink<T>> {
    TODO()
  }

  /**
   * Gets the current value for the given [NodeInput].
   */
  operator fun <T : Any> get(input: NodeInput<T>): T? {
    TODO()
  }

  /**
   * Gets the current value for the given [NodeInput].
   */
  operator fun <T : Any> get(input: Property<T>): T {
    TODO()
  }

  /**
   * Gets the current value for the given [NodeInput].
   */
  operator fun <T : Any> get(input: DefaultedNodeInput<T>): T {
    TODO()
  }
}
