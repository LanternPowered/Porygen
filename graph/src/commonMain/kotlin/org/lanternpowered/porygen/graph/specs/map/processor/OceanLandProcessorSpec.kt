/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs.map.processor

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.map.processor.OceanLandProcessor
import org.lanternpowered.porygen.value.Vec2dToDouble

object OceanLandProcessorSpec : NodeSpec("map/processor/ocean land", "Ocean Land Processor") {

  val height = input<Vec2dToDouble>("height")

  val output = output("out") { node ->
    val height = node[height]
    if (height == null) null else OceanLandProcessor(height)
  }
}
