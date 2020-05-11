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

import org.spongepowered.math.vector.Vector2d
import kotlin.random.Random

/**
 * Represents a set of points which can be generated,
 * [generate] call will produce new points.
 *
 * The generator should always output the same points
 * for a specifically seeded [Random].
 */
interface PointsGenerator {

  /**
   * Gets a list of points for the given [Random].
   *
   * Points should range between 0 (inclusive) and
   * 1 (exclusive).
   */
  fun generate(random: Random): List<Vector2d>
}
