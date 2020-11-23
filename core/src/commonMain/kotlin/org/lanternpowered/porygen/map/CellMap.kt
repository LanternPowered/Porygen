/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map

import org.lanternpowered.porygen.data.DataHolder
import org.lanternpowered.porygen.impl.map.CellMapBuilderImpl
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.processor.CellMapProcessor
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.math.vector.Vec2i

/**
 * Constructs a new [CellMap].
 */
fun cellMap(fn: CellMapBuilder.() -> Unit): CellMap =
    CellMapBuilderImpl().apply(fn).build()

/**
 * Represents the complete map.
 */
interface CellMap : CellMapPart, DataHolder {

  /**
   * The seed of the map.
   */
  val seed: Long

  /**
   * Gets the [CellMapChunk] at the given chunk coordinates.
   *
   * @param chunkX The chunk x coordinate
   * @param chunkY The chunk y coordinate
   */
  fun getChunk(chunkX: Int, chunkY: Int): CellMapChunk
}

/**
 * A builder for [CellMap]s.
 */
interface CellMapBuilder {

  /**
   * Sets the seed.
   *
   * @param seed The seed
   */
  fun seed(seed: Long)

  /**
   * Sets the cell polygon generator.
   *
   * @param polygonGenerator The polygon generator
   */
  fun polygonGenerator(polygonGenerator: CellPolygonGenerator)

  /**
   * Sets the points generator.
   *
   * @param pointsGenerator The points generator
   */
  fun pointsGenerator(pointsGenerator: PointsGenerator)

  /**
   * Sets the chunk size.
   *
   * Defaults to (16, 16).
   */
  fun chunkSize(size: Vec2i)

  /**
   * Sets the section size (in chunks).
   *
   * Defaults to (512, 512), so
   * (512, 512) * (16, 16) tiles.
   */
  fun sectionSize(size: Vec2i)

  /**
   * Adds a [CellMapProcessor].
   *
   * The order processors are added matters, they
   * will be executed in the same order they were
   * added.
   */
  fun addProcessor(processor: CellMapProcessor)
}
