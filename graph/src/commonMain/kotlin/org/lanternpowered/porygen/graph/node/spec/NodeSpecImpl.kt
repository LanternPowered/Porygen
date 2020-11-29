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

import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.util.type.GenericType
import kotlin.js.JsName
import kotlin.jvm.JvmName

@Suppress("UNCHECKED_CAST")
internal class NodeSpecImpl {

  val inputs = LinkedHashMap<String, InputPortSpecImpl<*>>()
  val outputs = LinkedHashMap<String, OutputPortSpecImpl<*>>()
  val properties = LinkedHashMap<String, PropertySpecImpl<*>>()

  private fun checkFreePort(id: String) {
    check(id !in inputs && id !in outputs) {
      "There's already a port registered with the id: $id" }
  }

  @JsName("inputWithNullableDefault_DataType")
  @JvmName("inputWithNullableDefault")
  fun <T> input(id: String, type: GenericType<T>, default: T?): InputPortSpec<T?> {
    checkFreePort(id)
    val spec = InputPortSpecImpl(PortId(id), type) { default }
    inputs[id] = spec
    return spec
  }

  fun <T> input(id: String, type: GenericType<T>, default: T): InputPortSpec<T> =
    input(id, type, default as T?) as InputPortSpec<T>

  fun <T> property(id: String, type: GenericType<T>, default: () -> T): PropertySpec<T> {
    check(id !in properties) {
      "There's already a property registered with the id: $id" }
    val spec = PropertySpecImpl(PropertyId(id), type, default)
    properties[id] = spec
    return spec
  }
}

internal data class PropertySpecImpl<T>(
  override val id: PropertyId,
  override val dataType: GenericType<T>,
  val default: () -> T
) : PropertySpec<T>

internal data class OutputPortSpecImpl<T>(
  override val id: PortId,
  override val dataType: GenericType<out T>,
) : OutputPortSpec<T>

internal data class InputPortSpecImpl<T>(
  override val id: PortId,
  override val dataType: GenericType<out T>,
  val default: () -> T
) : InputPortSpec<T>
