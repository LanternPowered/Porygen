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
import org.lanternpowered.porygen.graph.data.DataType
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.graph.node.property.PropertyId

/**
 * Represents a node that can be dynamically
 * modified.
 */
interface DynamicNode : Node {

  /**
   * Registers a new output port.
   */
  fun <T> addOutput(id: PortId, type: DataType<T>): OutputPort<T>

  /**
   * Registers a new input port.
   */
  fun <T> addInput(id: PortId, type: DataType<T>, default: () -> T? = { null }): InputPort<T>

  /**
   * Removes the port with the given id and removes
   * all the connections to it. Returns whether it
   * was successful.
   */
  fun removePort(id: PortId): Boolean

  /**
   * Registers a new property.
   */
  fun <T> addProperty(id: PropertyId, type: DataType<T>, value: T): Property<T>

  /**
   * Removes the property with the given id. Returns
   * whether it was successful.
   */
  fun removeProperty(id: PropertyId): Boolean
}
