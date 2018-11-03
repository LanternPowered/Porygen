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
import org.lanternpowered.porygen.math.geom.Rectangled
import org.spongepowered.math.vector.Vector2d
import kotlin.random.Random
import kotlin.streams.toList

/**
 * Zooms to a specific area on points from the
 * underlying [PointsGenerator].
 */
class ZoomPointsGenerator(
    private val backing: PointsGenerator,
    private val zoomFactor: Vector2d
) : PointsGenerator {

  override fun generatePoints(context: GeneratorContext, rectangle: Rectangled, random: Random): List<Vector2d> {
    val min = rectangle.min
    val max = rectangle.max
    val zoomOutRectangle = Rectangled(min, max.sub(min).mul(this.zoomFactor).add(min))
    return this.backing.generatePoints(context, zoomOutRectangle, random).stream()
        .filter { rectangle.contains(it) }
        .toList()
  }
}
