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
import org.lanternpowered.porygen.graph.node.NodeImpl

internal abstract class PortImpl<T>(
  override val id: PortId,
  override val dataType: DataType<T>,
  override val node: NodeImpl,
) : Port<T>

internal class InputPortImpl<T>(
  id: PortId,
  dataType: DataType<T>,
  node: NodeImpl,
  private val defaultSupplier: () -> T?
) : PortImpl<T>(id, dataType, node), InputPort<T> {
  override var connection: OutputPort<out T>? = null
  override val default: T?
    get() = defaultSupplier()
}

internal class OutputPortImpl<T>(
  id: PortId,
  dataType: DataType<T>,
  node: NodeImpl,
) : PortImpl<T>(id, dataType, node), OutputPort<T> {

  override fun connectTo(port: InputPort<in T>): Boolean {
    TODO("Not yet implemented")
  }

  override val connections: Collection<InputPort<in T>>
    get() = TODO("Not yet implemented")

  override fun buildTree(): T? {
    TODO("Not yet implemented")
  }
}
