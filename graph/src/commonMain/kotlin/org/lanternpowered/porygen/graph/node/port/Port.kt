/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node.port

import org.lanternpowered.porygen.graph.data.DataType
import org.lanternpowered.porygen.graph.node.Node

/**
 * Represents the identifier of a port.
 */
inline class PortId(val value: String)

/**
 * Represents a port of a node.
 */
interface Port<T> {

  /**
   * The identifier of this port.
   */
  val id: PortId

  /**
   * The data type that is expected for this port.
   */
  val dataType: DataType<T>

  /**
   * The node this port is part of.
   */
  val node: Node
}

/**
 * Represents an input port.
 */
interface InputPort<T> : Port<T> {

  /**
   * The output port that is connected
   * to this port, if any.
   */
  val connection: OutputPort<out T>?

  /**
   * The default value that's assigned to this input
   * port, if applicable.
   */
  val default: T?
}

/**
 * Represents an output port.
 */
interface OutputPort<T> : Port<T> {

  /**
   * Attempts to connect this output port to
   * the given input port. Returns whether it was
   * successful.
   */
  fun connectTo(port: InputPort<in T>): Boolean

  /**
   * All the input ports this output port
   * is connected to.
   */
  val connections: Collection<InputPort<in T>>

  /**
   * Attempts to build the node tree up to this output
   * port and return the built object.
   */
  fun buildTree(): T?
}
