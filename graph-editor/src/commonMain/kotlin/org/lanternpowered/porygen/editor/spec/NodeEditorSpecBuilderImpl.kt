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
import org.lanternpowered.porygen.graph.node.spec.InputPortSpec
import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpec

class NodeEditorSpecBuilderImpl<S : NodeSpec> : NodeEditorSpecBuilder<S>, NodeEditorSpec<S> {

  override var preview: (@Composable S.(node: Node) -> Unit)? = null
    private set

  override fun <T> input(input: InputPortSpec<T>, content: @Composable S.(node: Node) -> Unit) {
    TODO("Not yet implemented")
  }

  override fun <T> output(output: OutputPortSpec<T>, content: @Composable S.(node: Node) -> Unit) {
    TODO("Not yet implemented")
  }

  override fun preview(preview: @Composable S.(node: Node) -> Unit) {
    this.preview = preview
  }
}
