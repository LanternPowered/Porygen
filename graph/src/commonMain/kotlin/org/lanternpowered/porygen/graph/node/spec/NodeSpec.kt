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
import org.lanternpowered.porygen.graph.data.DataType
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.value.ConstantDouble
import org.lanternpowered.porygen.value.ConstantInt
import org.lanternpowered.porygen.value.ConstantLong
import org.lanternpowered.porygen.value.DoubleSupplier
import org.lanternpowered.porygen.value.IntSupplier
import org.lanternpowered.porygen.value.LongSupplier
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
  fun <T> input(id: String, type: DataType<T>): InputPortSpec<T?> =
    input(id, type, null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  fun <T> input(id: String, type: DataType<T>, default: T): InputPortSpec<T> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("inputWithNullableDefault_DataType")
  @JvmName("inputWithNullableDefault")
  fun <T> input(id: String, type: DataType<T>, default: T?): InputPortSpec<T?> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id and data type.
   */
  fun <T : Any> input(id: String, type: KClass<T>): InputPortSpec<T?> =
    input(id, type, null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("inputWithNullableDefault")
  @JvmName("inputWithNullableDefault")
  fun <T : Any> input(id: String, type: KClass<T>, default: T?): InputPortSpec<T?> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  fun <T : Any> input(id: String, type: KClass<T>, default: T): InputPortSpec<T> =
    impl.input(id, type, default)

  /**
   * Adds a new input port with the given id and data type.
   */
  inline fun <reified T : Any> input(id: String): InputPortSpec<T?> =
    input(id, T::class, null)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  inline fun <reified T : Any> input(id: String, default: T): InputPortSpec<T> =
    input(id, T::class, default)

  /**
   * Adds a new input port with the given id, data type and default value.
   */
  @JsName("reifiedInputWithNullableDefault")
  @JvmName("inputWithNullableDefault")
  inline fun <reified T : Any> input(id: String, default: T?): InputPortSpec<T?> =
    input(id, T::class, default)

  fun input(id: String, default: Int? = null): InputPortSpec<IntSupplier?> =
    input(id, IntSupplier::class, if (default != null) ConstantInt(default) else null)

  fun input(id: String, default: Int): InputPortSpec<IntSupplier> =
    input(id, IntSupplier::class, ConstantInt(default))

  fun input(id: String, default: Double? = null): InputPortSpec<DoubleSupplier?> =
    input(id, DoubleSupplier::class, if (default != null) ConstantDouble(default) else null)

  fun input(id: String, default: Double): InputPortSpec<DoubleSupplier> =
    input(id, DoubleSupplier::class, ConstantDouble(default))

  fun input(id: String, default: Long? = null): InputPortSpec<LongSupplier?> =
    input(id, LongSupplier::class, if (default != null) ConstantLong(default) else null)

  fun input(id: String, default: Long): InputPortSpec<LongSupplier> =
    input(id, LongSupplier::class, ConstantLong(default))

  fun <T> output(
    id: String,
    type: DataType<T>,
    factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> {
    TODO()
  }

  fun <T : Any> output(
    id: String,
    type: KClass<T>,
    factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> {
    TODO()
  }

  inline fun <reified T : Any> output(
    id: String,
    noinline factory: OutputBuilderScope.(node: Node) -> T?
  ): OutputPortSpec<T> =
    output(id, T::class, factory)

  inline fun <reified T : Any> property(id: String, default: T): PropertySpec<T> =
    property(id, T::class) { default }

  inline fun <reified T : Any> property(id: String, noinline default: () -> T): PropertySpec<T> =
    property(id, T::class, default)

  fun <T : Any> property(id: String, type: KClass<T>, default: () -> T): PropertySpec<T> {
    TODO()
  }

  @Suppress("USELESS_CAST")
  fun <T : Any> property(id: String, type: DataType<T>, default: T): PropertySpec<T> =
    property(id, type, { default } as () -> T)

  fun <T : Any> property(id: String, type: DataType<T>, default: () -> T): PropertySpec<T> =
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
  val dataType: DataType<T>
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
  val dataType: DataType<out T>
}

/**
 * Represents the specification of an input port.
 */
interface InputPortSpec<T> : PortSpec<T>

/**
 * Represents the specification of an output port.
 */
interface OutputPortSpec<T> : PortSpec<T>
