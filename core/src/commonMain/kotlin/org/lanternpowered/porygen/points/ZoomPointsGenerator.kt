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

import org.lanternpowered.porygen.math.vector.Vec2d
import kotlin.random.Random

/**
 * Zooms in on the points of the underlying [PointsGenerator].
 */
class ZoomPointsGenerator(
    private val original: PointsGenerator,
    private val scale: Vec2d
) : PointsGenerator {

  override fun generate(random: Random): List<Vec2d> =
      this.original.generate(random)
          .map { point -> point * this.scale }
          .filter { point -> point.x < 1.0 && point.y < 1.0 }
}
