/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.test

import it.unimi.dsi.fastutil.HashCommon
import org.lanternpowered.porygen.map.cellMap
import org.lanternpowered.porygen.map.polygon.TriangleCenterProvider
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.map.processor.DataKeys
import org.lanternpowered.porygen.map.processor.DistanceToOceanProcessor
import org.lanternpowered.porygen.map.processor.MoistureProcessor
import org.lanternpowered.porygen.map.processor.OceanLandProcessor
import org.lanternpowered.porygen.map.processor.RiverProcessor
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i
import org.lanternpowered.porygen.noise.module.minus
import org.lanternpowered.porygen.noise.module.plus
import org.lanternpowered.porygen.noise.module.scalePoint
import org.lanternpowered.porygen.noise.module.source.Perlin
import org.lanternpowered.porygen.noise.module.times
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.util.graphics.draw
import org.lanternpowered.porygen.util.graphics.showGraphics
import org.lanternpowered.porygen.util.graphics.with
import java.awt.BasicStroke
import java.awt.Color
import kotlin.math.abs

object MapGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L

    val terrainPerlin = Perlin(
        frequency = 0.05,
        persistence = 0.5,
        octaves = 10,
        seed = seed.hashCode()
    )

    val terrainScalePoint = terrainPerlin.scalePoint(x = 0.07, z = 0.07)
    val terrainHeight = terrainScalePoint - 1.0

    val temperaturePerlin = Perlin(
        frequency = 0.05,
        persistence = 0.5,
        octaves = 10,
        seed = HashCommon.murmurHash3(seed).hashCode()
    )
    val temperatureScalePoint = temperaturePerlin.scalePoint(x = 0.04, z = 0.04)

    val moisturePerlin = Perlin(
        frequency = 0.01,
        persistence = 0.3,
        octaves = 2,
        seed = HashCommon.murmurHash3(seed + 2).hashCode()
    )
    val moistureScalePoint = moisturePerlin.scalePoint(x = 0.3, z = 0.3)
    val moistureModifier = (moistureScalePoint * 0.6) + 1.0
    val moistureBase = (moistureScalePoint * 0.4) + 0.2

    showGraphics { bounds, graphics ->
      for (x in 0 until bounds.x) {
        for (y in 0 until bounds.y) {
          val value = moistureScalePoint[x.toDouble(), 0.0, y.toDouble()]
          val c = ((value.coerceIn(0.0, 2.2) / 2.2) * 255).toInt()
          graphics.color = Color(c, c, c)
          graphics.fillRect(x, y, 1, 1)
        }
      }
    }

    val sectionSize = Vector2i(512, 512)
    val maxOceanCellDistance = 5
    val map = cellMap {
      seed(seed)

      var pointsGenerator: PointsGenerator = BlueNoisePointsGenerator(amount = 300..350)
      pointsGenerator = ZoomPointsGenerator(pointsGenerator, Vector2d(1.1, 1.1))

      pointsGenerator(pointsGenerator)
      polygonGenerator(VoronoiPolygonGenerator(TriangleCenterProvider.Centroid))
      sectionSize(sectionSize)

      addProcessor(OceanLandProcessor(terrainHeight))
      addProcessor(DistanceToOceanProcessor(maxOceanCellDistance = maxOceanCellDistance))
      addProcessor(RiverProcessor())
      addProcessor(MoistureProcessor(base = moistureBase, modifier = moistureModifier))
    }

    val boundsOffset = Vector2i(-0, -0)

    showGraphics { bounds, graphics ->
      val viewRectangle = Rectanglei(Vector2i.ZERO, bounds).translate(boundsOffset)
      val view = map.getSubView(viewRectangle)
      for (cell in view.cells) {
        val drawable = cell.polygon.translate(-boundsOffset.toDouble()).toDrawable()
        val distance = cell[DataKeys.DISTANCE_TO_OCEAN]
        var color = if (cell[DataKeys.IS_OCEAN] == true) Color.blue else Color.yellow
        if (distance != null) {
          repeat(abs(distance)) {
            color = color.darker()
          }
        } else {
          repeat(maxOceanCellDistance + 1) {
            color = color.darker()
          }
        }
        graphics.color = color
        graphics.fillPolygon(drawable)
        graphics.color = Color.black
        graphics.drawPolygon(drawable)
      }
      graphics.with(stroke = BasicStroke(4f), color = Color.green) {
        for (edge in view.edges) {
          if (edge[DataKeys.IS_RIVER] == true) {
            graphics.draw(edge.line.translate(-boundsOffset))
          }
        }
      }
      graphics.with(color = Color.red) {
        for (corner in view.corners) {
          if (corner[DataKeys.DISTANCE_TO_RIVER_START] == 0) {
            val point = corner.point + -boundsOffset
            graphics.fillRect(point.x - 2, point.y - 2, 4, 4)
          }
        }
      }
    }

    showGraphics { bounds, graphics ->
      val viewRectangle = Rectanglei(Vector2i.ZERO, bounds).translate(boundsOffset)
      val view = map.getSubView(viewRectangle)
      for (cell in view.cells) {
        val drawable = cell.polygon.translate(-boundsOffset.toDouble()).toDrawable()
        val moisture = cell.require(DataKeys.MOISTURE)
        var color: Color
        if (cell[DataKeys.IS_OCEAN] != true) {
          color = Color.yellow
          if (moisture > 0)
            color = color.darker()
          repeat((moisture * 7).toInt() - 1) {
            color = color.darker()
          }
        } else {
          color = Color.blue
        }
        graphics.color = color
        graphics.fillPolygon(drawable)
        graphics.color = Color.black
        graphics.drawPolygon(drawable)
      }
      graphics.with(stroke = BasicStroke(4f), color = Color.green) {
        for (edge in view.edges) {
          if (edge[DataKeys.IS_RIVER] == true) {
            graphics.draw(edge.line.translate(-boundsOffset))
          }
        }
      }
      /*
      println(view.edges.size)
      println(view.edges.sortedBy { it.line.center }.joinToString("\n"))
      graphics.with(color = Color.red) {
        for (edge in view.edges) {
          val center = edge.line.center
          graphics.drawString(edge.cells.size.toString(), center.x, center.y)
        }
      }
      */
    }
  }
}
