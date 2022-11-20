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
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.genericTypeOf

/**
 * Registers a new output port.
 */
inline fun <reified T> DynamicNode.addOutput(
  id: PortId, noinline output: (Node) -> T?
): OutputPort<T> = addOutput(id, genericTypeOf(), output)

/**
 * Registers a new input port.
 */
inline fun <reified T> DynamicNode.addInput(
  id: PortId, noinline default: () -> T? = { null }
): InputPort<T> = addInput(id, genericTypeOf(), default)

/**
 * Registers a new property.
 */
inline fun <reified T> DynamicNode.addProperty(id: PropertyId, value: T): Property<T> =
  addProperty(id, genericTypeOf(), value)

/**
 * Represents a node that can be modified dynamically. All input, output and property types must
 * have a valid serializer registered in the graph spec.
 */
interface DynamicNode : Node {

  /**
   * Registers a new output port.
   */
  fun <T> addOutput(id: PortId, type: GenericType<T>, output: (Node) -> T?): OutputPort<T>

  /**
   * Registers a new input port.
   */
  fun <T> addInput(id: PortId, type: GenericType<T>, default: () -> T? = { null }): InputPort<T>

  /**
   * Removes the port with the given id and removes all the connections to it. Returns whether it
   * was successful.
   */
  fun removePort(id: PortId): Boolean

  /**
   * Registers a new property.
   */
  fun <T> addProperty(id: PropertyId, type: GenericType<T>, value: T): Property<T>

  /**
   * Removes the property with the given id. Returns whether it was successful.
   */
  fun removeProperty(id: PropertyId): Boolean
}
