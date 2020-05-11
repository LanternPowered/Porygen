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
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.processor.CellMapProcessor
import org.spongepowered.math.vector.Vector2i

/**
 * Constructs a new [CellMap].
 */
fun cellMap(fn: CellMapBuilder.() -> Unit): CellMap {
  TODO()
}

/**
 * Represents the complete map.
 */
interface CellMap : CellMapPart, DataHolder {

  /**
   * Gets the [CellMapChunk] at the given chunk coordinates.
   *
   * @param sectionX The section x coordinate
   * @param sectionZ The section z coordinate
   */
  fun getChunk(sectionX: Int, sectionZ: Int): CellMapChunk
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
   * Sets the chunk size.
   *
   * Defaults to (16, 16).
   */
  fun chunkSize(size: Vector2i)

  /**
   * Sets the section size (in chunks).
   *
   * Defaults to (16, 16), so
   * (16, 16) * (16, 16) tiles.
   */
  fun sectionSize(size: Vector2i)

  /**
   * Adds a [CellMapProcessor].
   *
   * The order processors are added matters, they
   * will be executed in the same order they were
   * added.
   *
   * Each processor will be executed in a "state",
   * during this state, it is possible to retrieve
   * information from elements that are outside the
   * current view. Only data up to the previous state
   * is guaranteed to be available. This is done to
   * prevent circular dependencies.
   */
  fun addProcessor(processor: CellMapProcessor)
}
