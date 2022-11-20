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
import kotlin.reflect.KClass

abstract class I1Transform<S : Any>(
  id: String, title: String, val type: KClass<S>
) : NodeSpec(id, title) {

  val input = input("in", type)

  val output = output("out", type) { node ->
    val in1 = node[input]
    if (in1 != null) apply(in1) else null
  }

  abstract fun apply(in1: S): S
}

abstract class I2Transform<S : Any>(
  id: String, title: String, val type: KClass<S>
) : NodeSpec(id, title) {

  val input1 = input("in1", type)
  val input2 = input("in2", type)

  val output = output("out", type) { node ->
    val in1 = node[input1]
    val in2 = node[input2]
    if (in1 != null && in2 != null) combine(in1, in2) else null
  }

  abstract fun combine(in1: S, in2: S): S
}
