/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs.map

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.map.cellMap
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.processor.CellMapProcessor
import org.lanternpowered.porygen.math.vector.Vec2i
import org.lanternpowered.porygen.points.PointsGenerator

/*
object CellMapType : NodeSpec("map/cell map", "Cell Map") {

  val seed = input("seed", 0L)
  val polygonGenerator = input<CellPolygonGenerator>("polygons")
  val pointsGenerator = input<PointsGenerator>("points")

  val chunkSize = property("chunk size", Vec2i(16, 16))
  val sectionSize = property("section size", Vec2i(512, 512))

  val processors = input<CellMapProcessor>("processors").multipleIndexed()

  val output = output("out") { node ->
    val polygonGenerator = node[polygonGenerator] ?: return@output null
    val pointsGenerator = node[pointsGenerator] ?: return@output null
    val processors = node[processors].filterNotNull()
    cellMap {
      seed(node[seed].get())
      polygonGenerator(polygonGenerator)
      pointsGenerator(pointsGenerator)
      chunkSize(node[chunkSize])
      sectionSize(node[sectionSize])
      processors.forEach(::addProcessor)
    }
  }
}
*/
