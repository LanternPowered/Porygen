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

import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.type.GenericType

internal data class DataConversion<I, O>(
  val input: GenericType<I>,
  val output: GenericType<O>,
  val convertor: (I) -> O
)

internal class NodeGraphSpecImpl : NodeGraphSpec, NodeGraphSpecBuilder {

  private val dataConversions = ArrayList<DataConversion<*,*>>()
  private val dataColors = LinkedHashMap<GenericType<*>, Color>()
  private val nodeSpecs = LinkedHashMap<String, NodeSpec>()
  private val data = DataScopeImpl()

  private inner class DataScopeImpl : NodeGraphSpecBuilder.DataScope {

    override fun <I, O> conversion(
      input: GenericType<I>, output: GenericType<O>, convertor: (I) -> O
    ) {
      dataConversions += DataConversion(input, output, convertor)
    }

    override fun color(type: GenericType<*>, color: Color) {
      dataColors[type] = color
    }
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

  override fun data(block: NodeGraphSpecBuilder.DataScope.() -> Unit) {
    data.apply(block)
  }
}
