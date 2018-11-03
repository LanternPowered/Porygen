/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.points.random

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.points.PointsGenerator
import kotlin.random.Random

/**
 * A base for a [PointsGenerator] which will generate random points
 * using a [Random] provided by the [GeneratorContext].
 *
 * For a specific seeded [Random] should always
 * the same result be outputted.
 */
abstract class AbstractRandomPointsGenerator : PointsGenerator {

  /**
   * The range which will be used to select the
   * amount of points that will be generated.
   */
  var points: IntRange = 1..10
}
