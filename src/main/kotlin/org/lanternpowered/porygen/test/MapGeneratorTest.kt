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
import org.lanternpowered.porygen.noise.Constant
import org.lanternpowered.porygen.noise.Perlin
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.util.graphics.draw
import org.lanternpowered.porygen.util.graphics.showGraphics
import org.lanternpowered.porygen.util.graphics.with
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.noise.module.combiner.Add
import org.spongepowered.noise.module.modifier.ScalePoint
import org.spongepowered.noise.module.source.Const
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

    val terrainScalePoint = ScalePoint()
    terrainScalePoint.setSourceModule(0, terrainPerlin)
    terrainScalePoint.xScale = 0.07
    terrainScalePoint.yScale = 1.0
    terrainScalePoint.zScale = 0.07

    val terrainOffset = Const()
    terrainOffset.value = -1.0

    val terrainHeight = Add()
    terrainHeight.setSourceModule(0, terrainOffset)
    terrainHeight.setSourceModule(1, terrainScalePoint)

    val temperaturePerlin = Perlin(
        frequency = 0.05,
        persistence = 0.5,
        octaves = 10,
        seed = HashCommon.murmurHash3(seed).hashCode()
    )

    val temperatureScalePoint = ScalePoint()
    temperatureScalePoint.setSourceModule(0, temperaturePerlin)
    temperatureScalePoint.xScale = 0.04
    temperatureScalePoint.yScale = 1.0
    temperatureScalePoint.zScale = 0.04

    val moistureModifierPerlin = Perlin(
        frequency = 0.05,
        persistence = 0.5,
        octaves = 5,
        seed = HashCommon.murmurHash3(seed).hashCode()
    )

    val moistureModifierScalePoint = ScalePoint()
    moistureModifierScalePoint.setSourceModule(0, moistureModifierPerlin)
    moistureModifierScalePoint.xScale = 0.04
    moistureModifierScalePoint.yScale = 0.3
    moistureModifierScalePoint.zScale = 0.04

    /*
    showGraphics { bounds, graphics ->
      var maxValue = temperaturePerlin.maxValue
      println("MAX: $maxValue")
      for (x in 0 until bounds.x) {
        for (y in 0 until bounds.y) {
          val value = temperatureScalePoint.getValue(x.toDouble(), 0.0, y.toDouble())
          maxValue = max(maxValue, value)
          /*
          if (value < 0) {
            graphics.color = Color.blue
          } else {
            graphics.color = Color.gray
          }
          */
          val c = ((value / 2.2) * 255).toInt()
          graphics.color = Color(c, c, c)
          graphics.fillRect(x, y, 1, 1)
        }
      }
      println(maxValue)
    }
    */

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
      addProcessor(MoistureProcessor(base = Constant(0.0)))
    }

    val boundsOffset = Vector2i(-0, -0)

    showGraphics { bounds, graphics ->
      val viewRectangle = Rectanglei(Vector2i.ZERO, bounds).translate(boundsOffset)
      val view = map.getSubView(viewRectangle)
      for (cell in view.cells) {
        val drawable = cell.polygon.translate(boundsOffset.negate().toDouble()).toDrawable()
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
            graphics.draw(edge.line.translate(boundsOffset.negate()))
          }
        }
      }
      graphics.with(color = Color.red) {
        for (corner in view.corners) {
          if (corner[DataKeys.DISTANCE_TO_RIVER_START] == 0) {
            val point = corner.point.add(boundsOffset.negate())
            graphics.fillRect(point.x - 2, point.y - 2, 4, 4)
          }
        }
      }
    }

    showGraphics { bounds, graphics ->
      val viewRectangle = Rectanglei(Vector2i.ZERO, bounds).translate(boundsOffset)
      val view = map.getSubView(viewRectangle)
      for (cell in view.cells) {
        val drawable = cell.polygon.translate(boundsOffset.negate().toDouble()).toDrawable()
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
            graphics.draw(edge.line.translate(boundsOffset.negate()))
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
