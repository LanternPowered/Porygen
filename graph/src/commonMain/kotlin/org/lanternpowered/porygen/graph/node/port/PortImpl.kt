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

import org.lanternpowered.porygen.graph.node.NodeImpl
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection
import org.lanternpowered.porygen.util.type.GenericType

internal abstract class PortImpl<T>(
  override val id: PortId,
  override val dataType: GenericType<T>,
  override val node: NodeImpl,
) : Port<T>

internal class InputPortImpl<T>(
  id: PortId,
  dataType: GenericType<T>,
  node: NodeImpl,
  private val defaultSupplier: () -> T?
) : PortImpl<T>(id, dataType, node), InputPort<T> {
  override var connection: OutputPort<out T>? = null
  override val default: T?
    get() = defaultSupplier()

  fun disconnect() {
    connection?.disconnectFrom(this)
  }
}

internal class OutputPortImpl<T>(
  id: PortId,
  dataType: GenericType<T>,
  node: NodeImpl,
) : PortImpl<T>(id, dataType, node), OutputPort<T> {

  private val mutableConnections = HashSet<InputPortImpl<in T>>()

  override val connections: Collection<InputPort<in T>> =
    mutableConnections.asUnmodifiableCollection()

  override fun connectTo(port: InputPort<in T>): Boolean {
    port as InputPortImpl<in T>
    if (!port.node.isValid || !node.isValid || port.node == node)
      return false
    port.connection = this
    mutableConnections.add(port)
    return true
  }

  override fun disconnectFrom(port: InputPort<in T>): Boolean {
    port as InputPortImpl<in T>
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

  override fun buildTree(): T? {
    TODO("Not yet implemented")
  }
}
