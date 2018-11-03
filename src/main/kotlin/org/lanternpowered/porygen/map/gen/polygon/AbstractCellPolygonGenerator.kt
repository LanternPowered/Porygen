/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.gen.polygon

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.SectionBasedPointsGenerator
import org.lanternpowered.porygen.points.random.BlueNoiseRandomPointsGenerator
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i

/**
 * This generator provides [CellPolygon]s which
 * will be used to construct [Cell]s.
 */
abstract class AbstractCellPolygonGenerator : CellPolygonGenerator {

  /**
   * The points generator that will be used to generate the polygons.
   */
  var pointsGenerator: PointsGenerator = SectionBasedPointsGenerator(
      BlueNoiseRandomPointsGenerator(), Vector2i(16 * 16, 16 * 16)) // TODO: Section size

  /**
   * Generates [CellPolygon]s for the given input point [Vector2d]s.
   *
   * @param context The context
   * @param points The points
   * @return The output centered polygons
   */
  abstract fun generate(context: GeneratorContext, rectangle: Rectangled, points: Collection<Vector2d>): Collection<CellPolygon>

  override fun generate(context: GeneratorContext, rectangle: Rectangled): Collection<CellPolygon> {
    val points = this.pointsGenerator.generatePoints(context, rectangle)
    return generate(context, rectangle, points)
  }
}
