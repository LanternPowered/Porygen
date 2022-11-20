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

import org.lanternpowered.porygen.graph.specs.AddDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.AddVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.ConstantDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.MultiplyVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractVec2ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.SubtractVec3ToDoubleSpec
import org.lanternpowered.porygen.graph.specs.noise.PerlinSpec
import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.genericTypeOf
import org.lanternpowered.porygen.value.ConstantDouble
import org.lanternpowered.porygen.value.ConstantInt
import org.lanternpowered.porygen.value.ConstantLong
import kotlin.reflect.KClass

/**
 * Instantiates a new node graph spec.
 */
fun NodeGraphSpec(block: NodeGraphSpecBuilder.() -> Unit = {}): NodeGraphSpec =
  NodeGraphSpecImpl().also(block)

/**
 * Assigns the color to the given data type. Colors of derived classes will automatically be mixed
 * if they are needed and not specified explicitly.
 */
inline fun <reified T> NodeGraphSpecBuilder.DataScope.color(color: Color) =
  color(genericTypeOf<T>(), color)

/**
 * Registers a data conversion that accepts the given input type and outputs the output type.
 *
 * Data conversions allow different data types to be connected in the graph.
 */
inline fun <reified I, reified O> NodeGraphSpecBuilder.DataScope.conversion(
  noinline function: (I) -> O
) = conversion(genericTypeOf(), genericTypeOf(), function)

//inline fun <reified T> NodeGraphSpecBuilder.DataScope.serializableType(
//  id: String,
//  serializer: KSerializer<T> = serializer()
//) = serializableType(genericTypeOf(), id, serializer)

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
     * Registers a data conversion that accepts the given input type and outputs the output type.
     *
     * Data conversions allow different data types to be connected in the graph.
     */
    fun <I : Any, O : Any> conversion(
      input: KClass<I>, output: KClass<O>, function: (I) -> O
    ) = conversion(GenericType(input), GenericType(output), function)

    /**
     * Registers a data conversion that accepts the given input type and outputs the output type.
     *
     * Data conversions allow different data types to be connected in the graph.
     */
    fun <I, O> conversion(
      input: GenericType<I>, output: GenericType<O>, function: (I) -> O
    )

    /**
     * Assigns the color to the given data type. Colors of derived classes will automatically be
     * mixed if they are needed and not specified explicitly.
     */
    fun color(type: KClass<*>, color: Color) = color(GenericType(type), color)

    /**
     * Assigns the color to the given data type. Colors of derived classes will automatically be
     * mixed if they are needed and not specified explicitly.
     */
    fun color(type: GenericType<*>, color: Color)
//
//    fun <T> serializableType(type: GenericType<T>, name: String, serializer: KSerializer<T>)
  }
}

/**
 * Represents the specification of a node graph.
 */
interface NodeGraphSpec {

  companion object {

    val Default = NodeGraphSpec {
      data {
        // Primitives
        conversion(Int::toDouble)
        conversion(Int::toFloat)
        conversion(Int::toLong)
        conversion(Long::toInt)
        conversion(Long::toDouble)
        conversion(Long::toFloat)
        conversion(Float::toInt)
        conversion(Float::toDouble)
        conversion(Float::toLong)
        conversion(Double::toInt)
        conversion(Double::toFloat)
        conversion(Double::toLong)

        // Constant suppliers
        conversion(::ConstantInt)
        conversion(::ConstantDouble)
        conversion(::ConstantLong)

        // Serializable types for dynamic nodes
//        serializableType<Boolean>("boolean")
//        serializableType<Byte>("byte")
//        serializableType<Short>("short")
//        serializableType<Int>("int")
//        serializableType<Long>("long")
//        serializableType<Float>("float")
//        serializableType<Double>("double")
//        serializableType<String>("string")
//        serializableType<Vec2d>("vec2d")
//        serializableType<Vec3d>("vec3d")
//        serializableType<Rectangled>("rectangled")
//        serializableType<Rectanglei>("rectanglei")
//        serializableType<Triangled>("triangled")
      }

      // Specs
      nodeSpec(ConstantDoubleSpec)
      nodeSpec(AddDoubleSpec)
      nodeSpec(AddVec2ToDoubleSpec)
      nodeSpec(AddVec3ToDoubleSpec)
      nodeSpec(MultiplyDoubleSpec)
      nodeSpec(MultiplyVec2ToDoubleSpec)
      nodeSpec(MultiplyVec3ToDoubleSpec)
      nodeSpec(SubtractDoubleSpec)
      nodeSpec(SubtractVec2ToDoubleSpec)
      nodeSpec(SubtractVec3ToDoubleSpec)

      nodeSpec(PerlinSpec)
    }
  }
}
