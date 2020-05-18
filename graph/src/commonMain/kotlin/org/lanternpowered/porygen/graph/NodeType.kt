/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph

import org.lanternpowered.porygen.value.Constant
import org.lanternpowered.porygen.value.ConstantInt
import org.lanternpowered.porygen.value.IntValue
import org.lanternpowered.porygen.value.Value
import kotlin.reflect.KClass

abstract class NodeType(val id: String, val title: String = id) {

  fun input(name: String, default: Double): DefaultedNodeInput<Value> =
      input(name, Value::class) { Constant(default) }

  fun input(name: String, default: Int): DefaultedNodeInput<IntValue> =
      input(name, IntValue::class) { ConstantInt(default) }

  inline fun <reified E : Enum<E>> input(name: String, default: E): DefaultedNodeInput<E> =
      input(name, E::class) { default }

  inline fun <reified T : Any> input(name: String, noinline default: () -> T): DefaultedNodeInput<T> =
      input(name, T::class, default)

  inline fun <reified T : Any> input(name: String): NodeInput<T> =
      input(name, T::class)

  fun <T : Any> input(name: String, type: KClass<T>): NodeInput<T> {
    TODO()
  }

  fun <T : Any> input(name: String, type: KClass<T>, default: () -> T): DefaultedNodeInput<T> {
    TODO()
  }

  inline fun <reified T : Any> output(name: String, noinline factory: (node: Node) -> T): NodeOutput<T> =
      output(name, T::class, factory)

  fun <T : Any> output(name: String, type: KClass<T>, factory: (node: Node) -> T?): NodeOutput<T> {
    TODO()
  }

  inline fun <reified T : Any> property(name: String, default: T): Property<T> =
      property(name, T::class) { default }

  inline fun <reified T : Any> property(name: String, noinline default: () -> T): Property<T> =
      property(name, T::class, default)

  fun <T : Any> property(name: String, type: KClass<T>, default: () -> T): Property<T> {
    TODO()
  }
}

class Property<T : Any>(val type: KClass<T>)

open class NodeInput<T : Any>(val type: KClass<T>)

class DefaultedNodeInput<T : Any>(type: KClass<T>) : NodeInput<T>(type)

class NodeOutput<T : Any>(val type: KClass<T>) {

  /**
   * Constructs the output object, if possible.
   */
  fun construct(): T? {
    TODO()
  }
}
