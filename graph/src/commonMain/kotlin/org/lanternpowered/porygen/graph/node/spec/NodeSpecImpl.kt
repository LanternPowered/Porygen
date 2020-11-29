/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node.spec

import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.unsafeCast
import kotlin.js.JsName
import kotlin.jvm.JvmName

// TODO: Introduce some caching mechanism so outputs can be reused for inputs?

@Suppress("UNCHECKED_CAST")
internal class NodeSpecImpl {

  val inputs = LinkedHashMap<String, InputPortSpecImpl<*>>()
  val outputs = LinkedHashMap<String, OutputPortSpecImpl<*>>()
  val properties = LinkedHashMap<String, PropertySpecImpl<*>>()

  private fun checkFreePort(id: String) {
    check(id !in inputs && id !in outputs) {
      "There's already a port registered with the id: $id" }
  }

  fun <T> output(
    id: String,
    type: GenericType<T>,
    factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> {
    checkFreePort(id)
    val builder: (Node) -> T? = { OutputBuilderScopeImpl.factory(it) }
    val spec = OutputPortSpecImpl(PortId(id), type, builder)
    outputs[id] = spec
    return spec
  }

  private fun <T> input0(id: String, type: GenericType<T>, default: T?): InputPortSpec<T?> {
    checkFreePort(id)
    val spec = InputPortSpecImpl(PortId(id), type) { default }
    inputs[id] = spec
    return spec
  }

  @JsName("input_nullableDefault")
  @JvmName("input_nullableDefault")
  fun <T> input(id: String, type: GenericType<T>, default: T?): InputPortSpec<T?> =
    input0(id, type, default)

  fun <T> input(id: String, type: GenericType<T>, default: T): InputPortSpec<T> =
    input0(id, type, default as T?) as InputPortSpec<T>

  fun <T> property(id: String, type: GenericType<T>, default: () -> T): PropertySpec<T> {
    check(id !in properties) {
      "There's already a property registered with the id: $id" }
    val spec = PropertySpecImpl(PropertyId(id), type, default)
    properties[id] = spec
    return spec
  }
}

internal object OutputBuilderScopeImpl : OutputBuilderScope {

  override fun <T> Node.get(spec: InputPortSpec<T>): T =
    requirePort(spec).buildTreeOrDefault().unsafeCast()

  override fun <T> Node.get(spec: PropertySpec<T>): T =
    requireProperty(spec).value
}

internal data class PropertySpecImpl<T>(
  override val id: PropertyId,
  override val dataType: GenericType<T>,
  val default: () -> T
) : PropertySpec<T>

internal data class OutputPortSpecImpl<T>(
  override val id: PortId,
  override val dataType: GenericType<out T>,
  val outputBuilder: (Node) -> T?
) : OutputPortSpec<T>

internal data class InputPortSpecImpl<T>(
  override val id: PortId,
  override val dataType: GenericType<out T>,
  val default: () -> T
) : InputPortSpec<T>
