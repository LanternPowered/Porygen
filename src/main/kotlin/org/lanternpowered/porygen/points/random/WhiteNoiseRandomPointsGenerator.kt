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
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.points.PointsGenerator
import org.spongepowered.math.vector.Vector2d
import kotlin.random.Random

/**
 * A simple [PointsGenerator] that generates random
 * points within the [Rectangled].
 */
class WhiteNoiseRandomPointsGenerator : AbstractRandomPointsGenerator() {

  override fun generatePoints(context: GeneratorContext, rectangle: Rectangled, random: Random): List<Vector2d> {
    val points = mutableListOf<Vector2d>()

    val min = this.points.first
    val max = this.points.last

    // Randomize the amount of points that will be generated
    val amount = min + random.nextInt(max - min + 1)

    val minX = rectangle.min.x
    val minY = rectangle.min.y
    val maxX = rectangle.max.x
    val maxY = rectangle.max.y

    val dX = maxX - minX
    val dY = maxY - minY

    for (i in 0 until amount) {
      val x = minX + random.nextDouble() * dX
      val y = minY + random.nextDouble() * dY
      points.add(Vector2d(x, y))
    }

    return points
  }
}
