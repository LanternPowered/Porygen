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
import org.lanternpowered.porygen.util.random.Xor128Random
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i

/**
 * This generator generates points for a specified world, the points are generated
 * based of the section coordinates and the world seed resulting in always the same
 * results when the points generator is re-run.
 *
 * Even if the input rectangle is smaller than the section, the algorithm must be
 * executed for the complete section, this ensures that the points are always the
 * same. Points that aren't within the requested rectangle, will be filtered out
 * afterwards.
 *
 * @property backing The backing points generator
 * @property sectionSize The size of a single section, should
 *   be a multiple of the chunk size
 */
class SectionBasedPointsGenerator(
    private val backing: PointsGenerator,
    private val sectionSize: Vector2i
) {

  fun generate(context: GeneratorContext, rectangle: Rectangled): List<Vector2d> {
    val points = mutableListOf<Vector2d>()

    val minX = rectangle.min.floorX / sectionSize.x
    val minY = rectangle.min.floorY / sectionSize.y
    val maxX = rectangle.max.floorX / sectionSize.x
    val maxY = rectangle.max.floorY / sectionSize.y

    val seed = context.seed
    val dSectionSize = sectionSize.toDouble()

    for (x in minX..maxX) {
      val xStart = sectionSize.x * x
      for (y in minY..maxY) {
        val yStart = sectionSize.y * y

        val sectionStart = Vector2d(xStart.toDouble(), yStart.toDouble())
        val sectionRandom = Xor128Random(x.toLong() * 341873128712L + y.toLong() * 132897987541L xor seed)

        val generated = backing.generate(sectionRandom)
            .map { it.mul(dSectionSize).add(sectionStart) }
        if (x == minX || y == minY || x == maxX || y == maxY) {
          points.addAll(generated.filter { rectangle.contains(it) })
        } else {
          points.addAll(generated)
        }
      }
    }

    return points
  }
}
