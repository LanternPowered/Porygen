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
import org.lanternpowered.porygen.graph.node.spec.NodeSpec

interface NodeEditorSpec<S : NodeSpec> {

  val preview: (@Composable S.(node: Node) -> Unit)?
}
