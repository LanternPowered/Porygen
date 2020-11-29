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
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.genericTypeOf
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Represents the specification of a node.
 */
abstract class NodeSpec(
  val id: String,
  val title: String = id
) {

  internal val impl = NodeSpecImpl()

  /**
   * Adds a new input port with the given id and data type.
   */
  fun <T> input(id: String, type: GenericType<T>): InputPortSpec<T?> =
    input(id, type, null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  fun <T> input(id: String, type: GenericType<T>, default: T): InputPortSpec<T> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("input_nullableDefault")
  @JvmName("input_nullableDefault")
  fun <T> input(id: String, type: GenericType<T>, default: T?): InputPortSpec<T?> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id and data type.
   */
  fun <T : Any> input(id: String, type: KClass<T>): InputPortSpec<T?> =
    input(id, type, null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("input_nullableDefault_kClass")
  @JvmName("input_nullableDefault_kClass")
  fun <T : Any> input(id: String, type: KClass<T>, default: T?): InputPortSpec<T?> =
    input(id, GenericType(type), default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  fun <T : Any> input(id: String, type: KClass<T>, default: T): InputPortSpec<T> =
    input(id, GenericType(type), default)

  /**
   * Adds a new input port with the given id and data type.
   */
  inline fun <reified T : Any> input(id: String): InputPortSpec<T?> =
    input(id, genericTypeOf(), null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  inline fun <reified T : Any> input(id: String, default: T): InputPortSpec<T> =
    input(id, genericTypeOf(), default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("input_nullableDefault_reified")
  @JvmName("input_nullableDefault_reified")
  inline fun <reified T : Any> input(id: String, default: T?): InputPortSpec<T?> =
    input(id, genericTypeOf(), default)

  fun <T> output(
    id: String,
    type: GenericType<T>,
    factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> {
    TODO()
  }

  fun <T : Any> output(
    id: String,
    type: KClass<T>,
    factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> =
    output(id, GenericType(type), factory)

  inline fun <reified T : Any> output(
    id: String,
    noinline factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> =
    output(id, genericTypeOf(), factory)

  inline fun <reified T> property(id: String, default: T): PropertySpec<T> =
    property(id, genericTypeOf(), default)

  inline fun <reified T> property(id: String, noinline default: () -> T): PropertySpec<T> =
    property<T>(id, genericTypeOf(), default)

  fun <T : Any> property(id: String, type: KClass<T>, default: T): PropertySpec<T> =
    property(id, GenericType(type), default)

  fun <T : Any> property(id: String, type: KClass<T>, default: () -> T): PropertySpec<T> =
    property(id, GenericType(type), default)

  @Suppress("USELESS_CAST")
  fun <T> property(id: String, type: GenericType<T>, default: T): PropertySpec<T> =
    property(id, type, { default } as () -> T)

  fun <T> property(id: String, type: GenericType<T>, default: () -> T): PropertySpec<T> =
    impl.property(id, type, default)
}

interface OutputBuilderScope {
  operator fun <T> Node.get(spec: InputPortSpec<T>): T
  operator fun <T> Node.get(spec: PropertySpec<T>): T
}

/**
 * Represents the specification of a property.
 */
interface PropertySpec<T> {

  /**
   * The id of the property created from this spec.
   */
  val id: PropertyId

  /**
   * The data type of the property created from this spec.
   */
  val dataType: GenericType<T>
}

/**
 * Represents the specification of a port.
 */
interface PortSpec<T> {

  /**
   * The id of the port created from this spec.
   */
  val id: PortId

  /**
   * The data type of the port created from this spec.
   */
  val dataType: GenericType<out T>
}

/**
 * Represents the specification of an input port.
 */
interface InputPortSpec<T> : PortSpec<T>

/**
 * Represents the specification of an output port.
 */
interface OutputPortSpec<T> : PortSpec<T>
