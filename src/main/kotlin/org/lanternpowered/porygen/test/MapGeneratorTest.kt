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
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.noise.module.combiner.Add
import org.spongepowered.noise.module.modifier.ScalePoint
import org.spongepowered.noise.module.source.Const
import org.spongepowered.noise.module.source.Perlin
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
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

    val constant = Const()
    constant.value = -1.0

    val terrainHeight = Add()
    terrainHeight.setSourceModule(0, constant)
    terrainHeight.setSourceModule(1, scalePoint)

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

    val maxOceanCellDistance = 3
    val map = cellMap {
      seed(seed)

      var pointsGenerator: PointsGenerator = BlueNoisePointsGenerator(amount = 300..350)
      pointsGenerator = ZoomPointsGenerator(pointsGenerator, Vector2d(1.1, 1.1))

      pointsGenerator(pointsGenerator)
      polygonGenerator(VoronoiPolygonGenerator(TriangleCenterProvider.Centroid))
      sectionSize(Vector2i(512, 512))

      addProcessor(OceanLandProcessor(terrainHeight))
      addProcessor(DistanceToOceanProcessor(maxOceanCellDistance = maxOceanCellDistance))
    }

    showGraphics { bounds, graphics ->
      val view = map.getSubView(Rectanglei(Vector2i.ZERO, bounds))
      for (cell in view.cells) {
        val drawable = cell.polygon.toDrawable()
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
      // Color invalid cells
      for (cell in view.cells) {
        if (cell.neighbors.size > 11) {
          graphics.color = Color.red
          val drawable = cell.polygon.toDrawable()
          graphics.fillPolygon(drawable)
          graphics.color = Color.black
          graphics.drawPolygon(drawable)
        }
      }
    }
  }

  fun showGraphics(fn: (bounds: Vector2i, graphics: Graphics) -> Unit) {
    val width = 1000
    val height = 1000

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.graphics
    fn(Vector2i(width, height), graphics)

    val canvas = object : JPanel() {
      override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, this)
      }
    }

    val frame = JFrame()
    frame.layout = BorderLayout()
    frame.add(canvas, BorderLayout.CENTER)
    frame.setSize(width, height)
    frame.isVisible = true

    canvas.repaint()
  }
}
