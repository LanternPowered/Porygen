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

import kotlinx.serialization.KSerializer
import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.Colors
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.isSubtypeOf
import org.lanternpowered.porygen.util.type.isSupertypeOf
import org.lanternpowered.porygen.util.type.nullable
import org.lanternpowered.porygen.util.unsafeCast
import kotlin.reflect.KClass

internal data class DataConversion<I, O>(
    val input: GenericType<I>,
    val output: GenericType<O>,
    val convertor: (I) -> O
)

private data class DataConversionKey(
    val input: GenericType<*>,
    val output: GenericType<*>
)

internal class NodeGraphSpecImpl : NodeGraphSpec, NodeGraphSpecBuilder {

  private val dataConversions = ArrayList<DataConversion<*,*>>()
  private val dataColors = LinkedHashMap<KClass<*>, Color>()
  private val nodeSpecs = LinkedHashMap<String, NodeSpec>()
  private val data = DataScopeImpl()

  /**
   * A map with all the resolved conversion functions.
   */
  private val conversionFunctions = HashMap<DataConversionKey, ((Any?) -> Any?)?>()

  private inner class DataScopeImpl : NodeGraphSpecBuilder.DataScope {

    override fun <I, O> conversion(
        input: GenericType<I>, output: GenericType<O>, function: (I) -> O
    ) {
      dataConversions += DataConversion(input, output, function)
    }

    override fun color(type: GenericType<*>, color: Color) {
      val kClass = type.classifier as? KClass<*> ?: return
      dataColors[kClass] = color
    }

//    override fun <T> serializableType(type: GenericType<T>, name: String, serializer: KSerializer<T>) {
//    }
  }

  override fun include(spec: NodeGraphSpec) {
    spec as NodeGraphSpecImpl
    dataConversions += spec.dataConversions
    dataColors += spec.dataColors
  }

  override fun nodeSpec(spec: NodeSpec) {
    check(spec.id !in nodeSpecs) {
      "There's already a node spec registered with the id ${spec.id}" }
    nodeSpecs[spec.id] = spec
  }

  fun nodeSpec(id: String): NodeSpec? = nodeSpecs[id]

  override fun data(block: NodeGraphSpecBuilder.DataScope.() -> Unit) {
    data.apply(block)
  }

  /**
   * Gets the [Color] that should be shown for the given type.
   */
  fun getColor(type: GenericType<*>): Color {
    val kClass = type.classifier as KClass<*>
    return dataColors.getOrPut(kClass) { getDerivedColor(kClass) }
  }

  private fun getDerivedColor(kClass: KClass<*>): Color {
    // TODO
    return Colors.Green
  }

  /**
   * Gets the conversion function that can be used to convert from the [from] type to the [to] type.
   *
   * Returns `null` if the conversion is not possible.
   */
  fun <I, O> getConversionFunction(from: GenericType<I>, to: GenericType<O>): ((I) -> O?)? {
    // No need to convert
    if (from.isSubtypeOf(to.nullable()))
      return { it.unsafeCast() }
    return conversionFunctions.getOrPut(DataConversionKey(from, to)) {
      buildConversionFunction(from, to).unsafeCast()
    }.unsafeCast()
  }

  private fun <I, O> buildConversionFunction(
    from: GenericType<I>, to: GenericType<O>, visitedTypes: List<GenericType<*>> = listOf()
  ): ((I) -> O?)? {
    val fromAccepting = ArrayList<DataConversion<*,*>>()
    for (conversion in dataConversions) {
      // Whether the conversion input would accept the 'from' type
      // Use nullable here to allow nullable inputs, will be handled manually
      val acceptsFrom = from.isSubtypeOf(conversion.input.nullable())
      // Check whether the conversion output would output the 'to' type and
      // the input type
      if (acceptsFrom && to.isSupertypeOf(conversion.output)) {
        val function = conversion.convertor.unsafeCast<((I) -> O?)>()
        // Nulls are accepted by the function
        if (conversion.input.isNullable)
          return function
        return { if (it == null) null else function(it) }
      }
      if (acceptsFrom && !visitedTypes.any { conversion.output.isSubtypeOf(it) })
        fromAccepting += conversion
    }
    for (fromConversion in fromAccepting) {
      val second = buildConversionFunction<Any?, O>(
        fromConversion.output.unsafeCast(), to, visitedTypes + from
      ) ?: continue
      val first = fromConversion.convertor.unsafeCast<(I) -> Any?>()
      return { second(first(it)) }
    }
    return null
  }
}
