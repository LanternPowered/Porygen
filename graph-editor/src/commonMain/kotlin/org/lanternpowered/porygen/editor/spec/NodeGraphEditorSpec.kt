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
import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.spec.InputPortSpec
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpec
import org.lanternpowered.porygen.util.type.GenericType
import org.lanternpowered.porygen.util.type.genericTypeOf
import org.lanternpowered.porygen.value.Vec2dToDouble
import org.lanternpowered.porygen.value.Vec3dToDouble

interface NodeGraphEditorSpec {

  fun <S : NodeSpec> spec(spec: S): NodeEditorSpec<S>

  fun <T> constantContent(type: GenericType<T>): (@Composable NodeSpec.(port: InputPort<T>) -> Unit)?
}

interface NodeGraphEditorSpecBuilder {

  fun <S : NodeSpec> type(spec: S, block: NodeEditorSpecBuilder<S>.() -> Unit)

  fun <T> constant(type: GenericType<T>, content: @Composable NodeSpec.(port: InputPort<T>) -> Unit)
}

inline fun <reified T> NodeGraphEditorSpecBuilder.constant(
  noinline content: @Composable NodeSpec.(port: InputPort<T>) -> Unit
) = constant(genericTypeOf(), content)

interface NodeEditorSpecBuilder<S : NodeSpec> {

  /**
   * Overrides the default rendering of the targeted input port.
   */
  fun <T> input(input: InputPortSpec<T>, content: @Composable S.(node: Node) -> Unit)

  /**
   * Overrides the default rendering of the targeted output port.
   */
  fun <T> output(output: OutputPortSpec<T>, content: @Composable S.(node: Node) -> Unit)

  /**
   * Generates a preview of the outputs of the node.
   */
  fun preview(preview: @Composable S.(node: Node) -> Unit)
}

fun NodeGraphEditorSpec(block: NodeGraphEditorSpecBuilder.() -> Unit): NodeGraphEditorSpec =
  NodeGraphEditorSpecImpl().also(block)

fun <S : NodeSpec> NodeEditorSpecBuilder<S>.vec2dPreview(output: S.(node: Node) -> Vec2dToDouble) {
  TODO()
}

fun <S : NodeSpec> NodeEditorSpecBuilder<S>.vec3dPreview(output: S.(node: Node) -> Vec3dToDouble) {
  TODO()
}
