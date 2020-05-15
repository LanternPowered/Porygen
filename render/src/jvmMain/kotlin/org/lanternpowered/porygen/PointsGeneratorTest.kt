/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen

import org.lanternpowered.porygen.impl.map.SectionPolygonGenerator
import org.lanternpowered.porygen.impl.map.SectionPosition
import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.polygon.TriangleCenterProvider
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i
import org.lanternpowered.porygen.noise.module.scalePoint
import org.lanternpowered.porygen.noise.module.source.Perlin
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import kotlin.jvm.JvmStatic

object PointsGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L

    // The random points generator
    var generator: PointsGenerator = BlueNoisePointsGenerator(amount = 200..250)
    // generator = GridBasedPointsGenerator(amount = 200..250)
    generator = ZoomPointsGenerator(generator, Vector2d(1.1, 1.1))

    val perlin = Perlin(
        frequency = 0.05,
        persistence = 0.5,
        seed = seed.hashCode(),
        octaves = 10
    ).scalePoint(x = 0.07, z = 0.07)

    var polygonGenerator: CellPolygonGenerator
    polygonGenerator = VoronoiPolygonGenerator(TriangleCenterProvider.Circumcenter)
    // polygonGenerator = DelaunayTrianglePolygonGenerator()

    val sectionPolygonGenerator = SectionPolygonGenerator(seed, Vector2i(300, 300), generator, polygonGenerator)
    val centeredPolygons = mutableListOf<CellPolygon>()
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 0)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 1)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(1, 1)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 2)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 0)))

    /*
    showGraphics { _, graphics ->
      for (polygon in centeredPolygons) {
        val center = polygon.center
        val value = perlin[center.x, 1.0, center.y]
        if (value < 1.0) {
          graphics.color = Color(0, (255.0 - 255.0 * (1.4 - value * 1.3)).toInt(), (255.0 - 255.0 * (1.4 - value * 1.3)).toInt())
        } else {
          graphics.color = Color((255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), (255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), 0)
        }
        graphics.fillPolygon(polygon.polygon.toDrawable())
        graphics.color = Color.BLACK
        graphics.fillRect(polygon.center.floorX, polygon.center.floorY, 3, 3)
        graphics.drawPolygon(polygon.polygon.toDrawable())
      }
    }*/
  }
}
