/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.points

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.util.random.Xor128Random
import org.spongepowered.math.vector.Vector2d
import kotlin.random.Random

/**
 * A generator that will generate points within a [Rectangled].
 */
interface PointsGenerator {

  /**
   * Generates a [Collection] with point coordinates that will be
   * used to generate [Cell]s.
   *
   * @param context The context that contains extra information related to the generation
   * @param rectangle The rectangle that represents the area that the points should be located in
   * @param random The random used to generate the points
   * @return A collection with the generated points
   */
  fun generatePoints(
      context: GeneratorContext, rectangle: Rectangled, random: Random = Xor128Random(context.seed)
  ): Collection<Vector2d>
}
