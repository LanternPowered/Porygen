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

import org.lanternpowered.porygen.map.cellMap
import org.lanternpowered.porygen.map.polygon.TriangleCenterProvider
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.map.processor.DataKeys
import org.lanternpowered.porygen.map.processor.DistanceToOceanProcessor
import org.lanternpowered.porygen.map.processor.OceanLandProcessor
import org.lanternpowered.porygen.map.processor.RiverProcessor
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.util.graphics.draw
import org.lanternpowered.porygen.util.graphics.drawLine
import org.lanternpowered.porygen.util.graphics.showGraphics
import org.lanternpowered.porygen.util.graphics.with
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.noise.module.combiner.Add
import org.spongepowered.noise.module.combiner.Displace
import org.spongepowered.noise.module.modifier.ScalePoint
import org.spongepowered.noise.module.source.Const
import org.spongepowered.noise.module.source.Perlin
import java.awt.BasicStroke
import java.awt.Color
import kotlin.math.abs

object MapGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L

    val perlin = Perlin()
    perlin.frequency = 0.05
    perlin.persistence = 0.5
    perlin.seed = java.lang.Long.hashCode(seed)
    perlin.octaveCount = 10

    val scalePoint = ScalePoint()
    scalePoint.setSourceModule(0, perlin)
    scalePoint.xScale = 0.07
    scalePoint.yScale = 1.0
    scalePoint.zScale = 0.07

    val displaceX = Const()
    val displaceY = Const()
    val displaceZ = Const()

    displaceX.value = 0.0
    displaceZ.value = 0.0

    val displace = Displace()
    displace.setSourceModule(0, scalePoint)
    displace.setDisplaceModules(displaceX, displaceY, displaceZ)

    val constant = Const()
    constant.value = -1.0

    val terrainHeight = Add()
    terrainHeight.setSourceModule(0, constant)
    terrainHeight.setSourceModule(1, displace)

    /*
    showGraphics { bounds, graphics ->
      for (x in 0 until bounds.x) {
        for (y in 0 until bounds.y) {
          val value = scalePoint.getValue(x.toDouble(), 1.0, y.toDouble()) - 1.0
          if (value < 0) {
            graphics.color = Color.blue
          } else {
            graphics.color = Color.gray
          }
          graphics.fillRect(x, y, 1, 1)
        }
      }
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
            graphics.fillRect(corner.point.x - 2, corner.point.y - 2, 4, 4)
          }
        }
      }
      graphics.with(color = Color.red) {
        for (corner in view.corners) {
          val distance = corner[DataKeys.DISTANCE_TO_OCEAN]
          if (distance != null) {
            graphics.drawString(distance.toString(), corner.point.x, corner.point.y)
          }
        }
      }
      graphics.with(color = Color.red) {
        graphics.drawLine(Vector2i(sectionSize.x, 0), Vector2i(sectionSize.x, bounds.y))
        graphics.drawLine(Vector2i(0, sectionSize.y), Vector2i(bounds.x, sectionSize.y))
      }
    }
  }
}
