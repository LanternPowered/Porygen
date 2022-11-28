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

import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpec
import org.lanternpowered.porygen.util.Named
import org.lanternpowered.porygen.util.type.GenericType
import kotlin.jvm.JvmInline

/**
 * Represents the identifier of a port.
 */
@JvmInline
value class PortId(val value: String)

/**
 * Represents a port of a node.
 */
interface Port<T> : Named {

  /**
   * The identifier of this port.
   */
  val id: PortId

  /**
   * The data type that is expected for this port.
   */
  val dataType: GenericType<T>

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
   * Returns whether the given data type is accepted by this input port. This can be directly or
   * through conversion functions that are defined in the [NodeGraphSpec].
   */
  fun isDataTypeAccepted(type: GenericType<*>): Boolean

  /**
   * The output port that is connected to this port, if any.
   */
  val connection: OutputPort<*>?

  /**
   * The default value that's assigned to this input port, if applicable.
   */
  val default: T?

  /**
   * The constant value that is assigned to this input port, if applicable. Is ignored if the port
   * is connected.
   */
  var value: T?

  /**
   * Disconnects the input port from the currently connected output port, if any.
   */
  fun disconnect(): Boolean

  /**
   * Attempts to build the node tree up to this output port and return the built object that will
   * be used as input value.
   *
   * This method does not take the [default] value into account.
   */
  fun buildTree(): T?

  /**
   * Attempts to build the node tree up to this output port and return the built object that will be
   * used as input value.
   *
   * Defaults to [default] if no output is connected to this input node or if the output value
   * couldn't be built.
   */
  fun buildTreeOrDefault(): T? = buildTree() ?: default
}

/**
 * Represents an output port.
 */
interface OutputPort<T> : Port<T> {

  /**
   * Attempts to connect this output port to the given input port. Returns whether it was
   * successful.
   */
  fun connectTo(port: InputPort<*>): Boolean

  /**
   * Attempts to disconnect this output port from the given input port.
   */
  fun disconnectFrom(port: InputPort<*>): Boolean

  /**
   * Disconnects this output port from all the inputs it's connected to.
   */
  fun disconnectFromAll()

  /**
   * All the input ports this output port is connected to.
   */
  val connections: Collection<InputPort<*>>

  /**
   * Attempts to build the node tree up to this output port and return the built object.
   */
  fun buildTree(): T?
}
