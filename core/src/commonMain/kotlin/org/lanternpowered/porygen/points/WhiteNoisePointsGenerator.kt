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

import org.lanternpowered.porygen.math.vector.Vector2d
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * A point set of which the points are completely randomized.
 *
 * @property amount The amount of points that will be generated
 */
class WhiteNoisePointsGenerator(
    private val amount: IntRange
) : PointsGenerator {

  override fun generate(random: Random): List<Vector2d> {
    // Randomize the amount of points that will be generated
    val amount = random.nextInt(this.amount)

    val points = mutableListOf<Vector2d>()
    for (i in 0 until amount) {
      val x = random.nextDouble()
      val y = random.nextDouble()
      points += Vector2d(x, y)
    }
    return points
  }
}