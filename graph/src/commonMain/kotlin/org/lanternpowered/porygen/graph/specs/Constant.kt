/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs

import org.lanternpowered.porygen.graph.node.spec.NodeSpec

object ConstantDoubleSpec : NodeSpec("constant", "Constant") {

  val value = property("value", 0.0)

  val output = output("out") { node ->
    node[value]
  }
}
