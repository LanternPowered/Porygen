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

import org.lanternpowered.porygen.graphics.showGraphics
import org.lanternpowered.porygen.graphics.toDrawable
import org.lanternpowered.porygen.impl.map.SectionPolygonGenerator
import org.lanternpowered.porygen.impl.map.SectionPosition
import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.polygon.TriangleCenterProvider
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.math.vector.Vec2i
import org.lanternpowered.porygen.noise.LatticeOrientation
import org.lanternpowered.porygen.noise.SimplexNoiseQuality
import org.lanternpowered.porygen.noise.module.source.Billow
import org.lanternpowered.porygen.noise.module.source.Perlin
import org.lanternpowered.porygen.noise.module.source.RidgedMulti
import org.lanternpowered.porygen.noise.module.source.Simplex
import org.lanternpowered.porygen.noise.module.source.SimplexRidgedMulti
import org.lanternpowered.porygen.noise.module.source.Voronoi
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.value.plus
import org.lanternpowered.porygen.value.pow
import org.lanternpowered.porygen.value.scalePoint
import org.lanternpowered.porygen.value.times
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

object PointsGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L

    // The random points generator
    var generator: PointsGenerator = BlueNoisePointsGenerator(amount = 200..250)
    // generator = GridBasedPointsGenerator(amount = 200..250)
    generator = ZoomPointsGenerator(generator, Vec2d(1.1, 1.1))

    var perlin = Perlin(
      frequency = 0.05,
      persistence = 0.5,
      seed = seed.hashCode(),
      octaves = 30
    ).scalePoint(xScale = 0.2, zScale = 0.2)

    perlin = Billow(seed = seed.hashCode(), frequency = 0.008, lacunarity = 4.0)
    perlin = Perlin(seed = seed.hashCode(), frequency = 0.008, lacunarity = 1.7)//.pow(3.0)
//    perlin = RidgedMulti(seed = seed.hashCode(), frequency = 0.008, lacunarity = 4.0).times(-1.0).plus(1.0)
//    perlin = Voronoi(seed = seed.hashCode(), frequency = 0.08, displacement = 2.0).times(1.5)
    perlin = SimplexRidgedMulti(
      seed = seed.hashCode(),
      latticeOrientation = LatticeOrientation.XZBeforeY,
      octaves = 5,
      frequency = 1.0 / 160,
      quality = SimplexNoiseQuality.Smooth,
      lacunarity = 2.5
    )//.times(-1.0).plus(1.0)

    showGraphics(
      width = 1000,
      height = 1000,
    ) { _, graphics ->
      for (x in 0..<1000) {
        for (y in 0..<1000) {
          val value = perlin[x.toDouble(), y.toDouble()]
          val component = (value.coerceIn(0.0, 1.0) * 255).toInt()
          graphics.color = Color(component, component, component)
          graphics.drawRect(x, y, 1, 1)
        }
      }
    }
//
//    var polygonGenerator: CellPolygonGenerator
//    polygonGenerator = VoronoiPolygonGenerator(TriangleCenterProvider.Circumcenter)
//    // polygonGenerator = DelaunayTrianglePolygonGenerator()
//
//    val sectionPolygonGenerator = SectionPolygonGenerator(seed, Vec2i(200, 200), generator,
//      polygonGenerator)
//    val centeredPolygons = mutableListOf<CellPolygon>()
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 0)))
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 1)))
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(1, 1)))
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 2)))
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 0)))
//    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(3, 0)))
//
//    showGraphics { _, graphics ->
//      for (polygon in centeredPolygons) {
//        val center = polygon.center
//        var value = perlin[center.x, 0.0, center.y]
//        if (value < 1.0) {
//          value = max(0.5, value)
//          graphics.color = Color(0, (255.0 - 255.0 * (1.4 - value * 1.3)).toInt(), (255.0 - 255.0 * (1.4 - value * 1.3)).toInt())
//        } else {
//          value = min(2.0, value)
//          graphics.color = Color((255.0 - 255.0 * (value - 1.0)).toInt(), (255.0 - 255.0 * (value - 1.0)).toInt(), 0)
//        }
//        graphics.fillPolygon(polygon.polygon.toDrawable())
//        graphics.color = Color.BLACK
//        graphics.fillRect(polygon.center.floorX, polygon.center.floorY, 3, 3)
//        graphics.drawPolygon(polygon.polygon.toDrawable())
//      }
//    }
    var min = 100.0
    var max = -100.0
    for (x in 0..<10000) {
      for (y in 0..<10000) {
        val value = perlin[x.toDouble(), 0.0, y.toDouble()]
        min = min(min, value)
        max = max(max, value)
      }
    }
    println("max: $max")
    println("min: $min")
  }
}
