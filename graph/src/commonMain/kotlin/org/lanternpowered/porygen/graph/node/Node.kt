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
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.spec.InputPortSpec
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpec
import org.lanternpowered.porygen.graph.node.spec.PortSpec
import org.lanternpowered.porygen.graph.node.spec.PropertySpec
import org.lanternpowered.porygen.math.vector.Vec2d

/**
 * Represents the identifier of a node.
 */
inline class NodeId(val value: Int)

/**
 * Represents a single node in a graph.
 */
interface Node {

  /**
   * The graph this node is or
   * was attached to.
   */
  val graph: NodeGraph

  /**
   * Whether this node is still valid. A node is
   * valid if it's still attached to its graph.
   */
  val isValid: Boolean

  /**
   * The id of the node.
   */
  val id: NodeId

  /**
   * The title of the node.
   */
  var title: String

  /**
   * The position of the node.
   */
  var position: Vec2d

  /**
   * Whether the node is currently expanded.
   */
  var expanded: Boolean

  /**
   * A list with all the inputs of this node.
   */
  val inputs: Collection<InputPort<*>>

  /**
   * A list with all the outputs of this node.
   */
  val outputs: Collection<OutputPort<*>>

  /**
   * All the properties of this node.
   */
  val properties: Collection<Property<*>>

  /**
   * Attempts to get a port with the given id.
   */
  fun port(id: PortId): Port<*>?

  /**
   * Attempts to get an input port with the given id.
   */
  fun input(id: PortId): InputPort<*>?

  /**
   * Attempts to get an output port with the given id.
   */
  fun output(id: PortId): OutputPort<*>?

  /**
   * Attempts to get the property with the given id.
   */
  fun property(id: PropertyId): Property<*>?

  /**
   * Attempts to get the port for the given spec.
   */
  fun <T> port(spec: PortSpec<T>): Port<T>?

  /**
   * Attempts to get the input port for the given spec.
   */
  fun <T> port(spec: InputPortSpec<T>): InputPort<T>?

  /**
   * Attempts to get the output port for the given spec.
   */
  fun <T> port(spec: OutputPortSpec<T>): OutputPort<T>?

  /**
   * Attempts to get the port for the given spec.
   */
  fun <T> requirePort(spec: PortSpec<T>): Port<T> =
    port(spec) ?: error("No port for given spec: $spec")

  /**
   * Attempts to get the input port for the given spec.
   */
  fun <T> requirePort(spec: InputPortSpec<T>): InputPort<T> =
    port(spec) ?: error("No port for given spec: $spec")

  /**
   * Attempts to get the output port for the given spec.
   */
  fun <T> requirePort(spec: OutputPortSpec<T>): OutputPort<T> =
    port(spec) ?: error("No port for given spec: $spec")

  /**
   * Attempts to get the property for the given spec.
   */
  fun <T> property(spec: PropertySpec<T>): Property<T>?

  /**
   * Attempts to get the property for the given spec.
   */
  fun <T> requireProperty(spec: PropertySpec<T>): Property<T> =
    property(spec) ?: error("No property for given spec: $spec")
}
