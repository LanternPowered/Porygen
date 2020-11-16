/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.type.map.processor

import org.lanternpowered.porygen.graph.NodeType
import org.lanternpowered.porygen.map.processor.OceanLandProcessor
import org.lanternpowered.porygen.value.Value2

object OceanLandProcessorType : NodeType("map/processor/ocean land", "Ocean Land Processor") {

  val height = input<Value2>("height")

  val output = output("out") { node ->
    val height = node[height]
    if (height == null) null else OceanLandProcessor(height)
  }
}
