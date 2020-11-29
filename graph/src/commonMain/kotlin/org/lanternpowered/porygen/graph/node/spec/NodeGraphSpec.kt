/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.porygen.graph.node.spec

import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.genericTypeOf
import kotlin.reflect.KClass

/**
 * Instantiates a new node graph spec.
 */
fun NodeGraphSpec(block: NodeGraphSpecBuilder.() -> Unit = {}): NodeGraphSpec =
  NodeGraphSpecImpl().also(block)

/**
 * Assigns the color to the given data type. Colors of derived
 * classes will automatically be mixed if they are needed and
 * not specified explicitly.
 */
inline fun <reified T> NodeGraphSpecBuilder.DataScope.color(color: Color) =
  color(genericTypeOf<T>(), color)

/**
 * Registers a data conversion that accepts the given
 * input type and outputs the output type.
 *
 * Data conversions allow different data types to be
 * connected in the graph.
 */
inline fun <reified I, reified O> NodeGraphSpecBuilder.DataScope.conversion(
  noinline function: (I) -> O
) = conversion(genericTypeOf(), genericTypeOf(), function)

/**
 * Represents the builder of the node graph spec.
 */
interface NodeGraphSpecBuilder {

  /**
   * Includes another [NodeGraphSpec] in this spec.
   */
  fun include(spec: NodeGraphSpec)

  /**
   * Registers a node spec.
   */
  fun nodeSpec(spec: NodeSpec)

  /**
   * Configures the data.
   */
  fun data(block: DataScope.() -> Unit)

  /**
   * The data scope.
   */
  interface DataScope {

    /**
     * Registers a data conversion that accepts the given
     * input type and outputs the output type.
     *
     * Data conversions allow different data types to be
     * connected in the graph.
     */
    fun <I : Any, O : Any> conversion(
      input: KClass<I>, output: KClass<O>, function: (I) -> O
    ) = conversion(GenericType(input), GenericType(output), function)

    /**
     * Registers a data conversion that accepts the given
     * input type and outputs the output type.
     *
     * Data conversions allow different data types to be
     * connected in the graph.
     */
    fun <I, O> conversion(
      input: GenericType<I>, output: GenericType<O>, function: (I) -> O
    )

    /**
     * Assigns the color to the given data type. Colors of derived
     * classes will automatically be mixed if they are needed and
     * not specified explicitly.
     */
    fun color(type: KClass<*>, color: Color) = color(GenericType(type), color)

    /**
     * Assigns the color to the given data type. Colors of derived
     * classes will automatically be mixed if they are needed and
     * not specified explicitly.
     */
    fun color(type: GenericType<*>, color: Color)
  }
}

/**
 * Represents the specification of a node graph.
 */
interface NodeGraphSpec
