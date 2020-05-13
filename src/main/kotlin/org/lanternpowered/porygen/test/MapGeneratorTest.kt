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
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.map.processor.DistanceToOceanProcessor
import org.lanternpowered.porygen.map.processor.OceanLandProcessor
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.noise.module.modifier.ScalePoint
import org.spongepowered.noise.module.source.Perlin
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

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

    val map = cellMap {
      seed(seed)

      var pointsGenerator: PointsGenerator = BlueNoisePointsGenerator(amount = 200..250)
      pointsGenerator = ZoomPointsGenerator(pointsGenerator, Vector2d(1.1, 1.1))

      pointsGenerator(pointsGenerator)
      polygonGenerator(VoronoiPolygonGenerator())
      sectionSize(Vector2i(512, 512))

      addProcessor(OceanLandProcessor(scalePoint))
      addProcessor(DistanceToOceanProcessor())
    }

    showGraphics { bounds, graphics ->
      val view = map.getSubView(Rectanglei(Vector2i.ZERO, bounds))
      for (cell in view.cells) {
        graphics.color = Color.black
        graphics.drawPolygon(cell.polygon.toDrawable())
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
