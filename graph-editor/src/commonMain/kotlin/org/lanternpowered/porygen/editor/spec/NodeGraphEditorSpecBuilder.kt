/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.editor.spec

import androidx.compose.runtime.Composable
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.util.type.GenericType

internal class NodeGraphEditorSpecImpl : NodeGraphEditorSpec, NodeGraphEditorSpecBuilder {

  private val constants = HashMap<GenericType<*>, NodeSpec.(port: InputPort<*>) -> Unit>()
  private val specs = HashMap<NodeSpec, NodeEditorSpecBuilderImpl<*>>()

  override fun <S : NodeSpec> type(spec: S, block: NodeEditorSpecBuilder<S>.() -> Unit) {
    spec(spec).also(block)
  }

  override fun <S : NodeSpec> spec(spec: S): NodeEditorSpecBuilderImpl<S> =
    specs.getOrPut(spec) { NodeEditorSpecBuilderImpl<NodeSpec>() }.unsafeCast<NodeEditorSpecBuilderImpl<S>>()

  override fun <T> constant(type: GenericType<T>, content: @Composable NodeSpec.(port: InputPort<T>) -> Unit) {
    constants[type] = content.unsafeCast<NodeSpec.(port: InputPort<*>) -> Unit>()
  }

  override fun <T> constantContent(type: GenericType<T>): (NodeSpec.(port: InputPort<T>) -> Unit)? =
    constants[type]
}
