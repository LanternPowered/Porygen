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
import org.lanternpowered.porygen.graph.node.port.InputPortImpl
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.OutputPortImpl
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.property.PropertyImpl
import org.lanternpowered.porygen.graph.node.spec.InputPortSpec
import org.lanternpowered.porygen.graph.node.spec.InputPortSpecImpl
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpec
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpecImpl
import org.lanternpowered.porygen.graph.node.spec.PortSpec
import org.lanternpowered.porygen.graph.node.spec.PropertySpec
import org.lanternpowered.porygen.graph.node.spec.PropertySpecImpl
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.unsafeCast

internal abstract class NodeImpl(
  override val id: NodeId,
  override var title: String,
  override var position: Vec2d,
  override val graph: NodeGraphImpl
) : Node {

  override var expanded: Boolean = true

  private val mutableInputs = HashMap<String, InputPortImpl<*>>()
  private val mutableOutputs = HashMap<String, OutputPortImpl<*>>()
  private val mutableProperties = HashMap<String, PropertyImpl<*>>()

  override var isValid: Boolean = true

  override val inputs: Collection<InputPort<*>> = mutableInputs.values.asUnmodifiableCollection()
  override val outputs: Collection<OutputPort<*>> = mutableOutputs.values.asUnmodifiableCollection()
  override val properties: Collection<Property<*>> = mutableProperties.values.asUnmodifiableCollection()

  private fun checkFreePort(id: String) {
    check(id !in mutableInputs && id !in mutableOutputs) {
      "There's already a port registered with the id: $id" }
  }

  @Suppress("UNCHECKED_CAST")
  protected fun initSpec(spec: NodeSpec) {
    for (input in spec.impl.inputs.values) {
      input as InputPortSpecImpl<Any>
      createInput(input.id, input.dataType as GenericType<Any>, input.default)
    }
    for (output in spec.impl.outputs.values) {
      output as OutputPortSpecImpl<Any>
      createOutput(output.id, output.dataType as GenericType<Any>, output.outputBuilder)
    }
    for (property in spec.impl.properties.values) {
      property as PropertySpecImpl<Any>
      createProperty(property.id, property.dataType, property.default())
    }
  }

  // TODO: Add port and property names

  protected fun <T> createInput(id: PortId, type: GenericType<T>, default: () -> T?): InputPortImpl<T> {
    checkFreePort(id.value)
    val input = InputPortImpl(id, id.value, type, this, default)
    mutableInputs[id.value] = input
    return input
  }

  protected fun <T> createOutput(id: PortId, type: GenericType<T>, outputBuilder: (Node) -> T?): OutputPortImpl<T> {
    checkFreePort(id.value)
    val output = OutputPortImpl(id, id.value, type, this, outputBuilder)
    mutableOutputs[id.value] = output
    return output
  }

  protected open fun removePort(id: PortId): Boolean {
    val input = mutableInputs.remove(id.value)
    if (input != null) {
      input.disconnect()
      return true
    } else {
      val output = mutableOutputs.remove(id.value)
      if (output != null) {
        output.disconnectFromAll()
        return true
      }
    }
    return false
  }

  protected fun <T> createProperty(id: PropertyId, type: GenericType<T>, value: T): PropertyImpl<T> {
    check(id.value !in mutableProperties) {
      "There's already a port registered with the id: $id" }
    val property = PropertyImpl(id, id.value, type, value, this)
    mutableProperties[id.value] = property
    return property
  }

  protected open fun removeProperty(id: PropertyId): Boolean {
    return mutableProperties.remove(id.value) != null
  }

  override fun input(id: PortId): InputPort<*>? =
    mutableInputs[id.value]

  override fun output(id: PortId): OutputPort<*>? =
    mutableOutputs[id.value]

  override fun port(id: PortId): Port<*>? =
    mutableInputs[id.value] ?: mutableOutputs[id.value]

  @Suppress("UNCHECKED_CAST")
  override fun <T> port(spec: InputPortSpec<T>): InputPort<T>? =
    port(spec.id) as? InputPort<T>

  @Suppress("UNCHECKED_CAST")
  override fun <T> port(spec: OutputPortSpec<T>): OutputPort<T>? =
    port(spec.id) as? OutputPort<T>

  override fun <T> port(spec: PortSpec<T>): Port<T>? =
    port(spec.id).unsafeCast()

  override fun property(id: PropertyId): Property<*>? =
    mutableProperties[id.value]

  override fun <T> property(spec: PropertySpec<T>): Property<T>? =
    property(spec.id).unsafeCast()
}
