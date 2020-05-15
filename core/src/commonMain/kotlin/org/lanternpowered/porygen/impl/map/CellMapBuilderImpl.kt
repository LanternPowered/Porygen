/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapBuilder
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.processor.CellMapProcessor
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.math.vector.Vector2i
import kotlin.random.Random

class CellMapBuilderImpl : CellMapBuilder {

  private var seed = Random.nextLong()
  private lateinit var polygonGenerator: CellPolygonGenerator
  private lateinit var pointsGenerator: PointsGenerator
  private var chunkSize = Vector2i(512, 512)
  private var sectionSize = Vector2i(512, 512)
  private val processors = mutableListOf<CellMapProcessor>()

  override fun seed(seed: Long) {
    this.seed = seed
  }

  override fun polygonGenerator(polygonGenerator: CellPolygonGenerator) {
    this.polygonGenerator = polygonGenerator
  }

  override fun pointsGenerator(pointsGenerator: PointsGenerator) {
    this.pointsGenerator = pointsGenerator
  }

  override fun chunkSize(size: Vector2i) {
    this.chunkSize = size
  }

  override fun sectionSize(size: Vector2i) {
    this.sectionSize = size
  }

  override fun addProcessor(processor: CellMapProcessor) {
    this.processors += processor
  }

  fun build(): CellMap = MapImpl(seed, sectionSize, polygonGenerator, pointsGenerator, processors.toList())
}
