/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.math.vector.Vec2d

@Suppress("UNCHECKED_CAST")
internal class SpecNodeImpl(
  id: NodeId,
  title: String,
  position: Vec2d,
  graph: NodeGraphImpl,
  val spec: NodeSpec
) : NodeImpl(id, title, position, graph) {

  init {
    initSpec(spec)
  }
}
