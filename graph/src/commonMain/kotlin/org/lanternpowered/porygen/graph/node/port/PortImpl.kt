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
import org.lanternpowered.porygen.graph.node.NodeImpl
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.unsafeCast

internal abstract class PortImpl<T>(
  override val id: PortId,
  override val name: String,
  override val dataType: GenericType<T>,
  override val node: NodeImpl
) : Port<T>

internal class InputPortImpl<T>(
  id: PortId,
  name: String,
  dataType: GenericType<T>,
  node: NodeImpl,
  private val defaultSupplier: () -> T?
) : PortImpl<T>(id, name, dataType, node), InputPort<T> {
  override var connection: OutputPort<*>? = null
  override val default: T?
    get() = defaultSupplier()

  fun disconnect() {
    connection?.disconnectFrom(this)
  }

  override fun isDataTypeAccepted(type: GenericType<*>): Boolean =
    node.graph.spec.getConversionFunction(type, dataType) != null

  override fun buildTree(): T? {
    val output = this.connection
      ?: return null
    val value = output.buildTree()
    if (!dataType.isNullable && value == null)
      return null
    val function = node.graph.spec.getConversionFunction(output.dataType, dataType)
      .unsafeCast<(Any?) -> T>()
    return function(value)
  }
}

internal class OutputPortImpl<T>(
  id: PortId,
  name: String,
  dataType: GenericType<T>,
  node: NodeImpl,
  private val outputBuilder: (Node) -> T?
) : PortImpl<T>(id, name, dataType, node), OutputPort<T> {

  private val mutableConnections = HashSet<InputPortImpl<*>>()

  override val connections: Collection<InputPort<*>> =
    mutableConnections.asUnmodifiableCollection()

  override fun connectTo(port: InputPort<*>): Boolean {
    port as InputPortImpl<*>
    if (!port.node.isValid || !node.isValid || port.node == node || !port.isDataTypeAccepted(dataType))
      return false
    port.connection = this
    mutableConnections.add(port)
    return true
  }

  override fun disconnectFrom(port: InputPort<*>): Boolean {
    port as InputPortImpl<*>
    if (port.connection != this)
      return false
    port.connection = null
    mutableConnections.remove(port)
    return true
  }

  override fun disconnectFromAll() {
    for (port in mutableConnections)
      port.connection = null
    mutableConnections.clear()
  }

  override fun buildTree(): T? =
    outputBuilder(node)
}
