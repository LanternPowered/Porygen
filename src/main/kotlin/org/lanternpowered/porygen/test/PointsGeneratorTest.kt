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

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.gen.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.gen.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.map.gen.polygon.VoronoiTriangleCenterProvider
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.SectionBasedPointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.points.random.BlueNoiseRandomPointsGenerator
import org.lanternpowered.porygen.util.random.XorWowRandom
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

object PointsGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L
    val random = XorWowRandom(seed)

    // The random points generator
    var generator: PointsGenerator

    generator = BlueNoiseRandomPointsGenerator().apply {
      this.setCellCoverage(0.8, 0.8)
      this.setCells(30, 30)
      this.points = 1000..2000
    }

    /*
    generator = new WhiteNoiseRandomPointsGenerator()
            .setPoints(new Rangei(500, 1000));*/

    /*
    generator = GridBasedRandomPointsGenerator().apply {
        this.setGrid(30, 30)
        this.points = 1000 .. 2000
    }*/

    val chunkSize = Vector2i(16, 16)
    generator = ZoomPointsGenerator(generator, Vector2d(3.0, 3.0))
    generator = SectionBasedPointsGenerator(generator, chunkSize.mul(16))

    val width = 1000
    val height = 1000

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.graphics
    val context = DummyContext(graphics, seed)

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

    /*
    for (x in 0 until width) {
        for (y in 0 until height) {
            val value = scalePoint.getValue(x.toDouble(), 1.0, y.toDouble())
            // System.out.println(value);
            if (value < 1.0) {
                graphics.color = Color(0, (255.0 - 255.0 * (1.4 - value * 1.3)).toInt(), (255.0 - 255.0 * (1.4 - value * 1.3)).toInt())
                // graphics.setColor(Color.CYAN);
            } else {
                graphics.color = Color((255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), (255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), 0)
                // graphics.setColor(Color.ORANGE);
            }
            graphics.drawRect(x, y, 1, 1)
        }
    }
    */

    /*
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    */
    val points = generator.generatePoints(
        context, Rectangled(0.0, 0.0, width.toDouble(), height.toDouble()))
    graphics.color = Color.BLACK
    graphics.drawPolygon(Rectanglei(Vector2i(0, 0), Vector2i(width - 1, height - 1)).toDrawable())
    for (point in points) {
      val x = point.floorX
      val y = point.floorY
      graphics.fillRect(x, y, 3, 3)
    }

    var centeredPolygonGenerator: CellPolygonGenerator
    // centeredPolygonGenerator = DelaunayTrianglePolygonGenerator()
    centeredPolygonGenerator = VoronoiPolygonGenerator().apply { this.triangleCenterProvider = VoronoiTriangleCenterProvider.Centroid }
    val centeredPolygons = centeredPolygonGenerator.generate(context,
        Rectangled(0.0, 0.0, width.toDouble(), height.toDouble()), points)

    for (polygon in centeredPolygons) {
      val center = polygon.center
      val value = scalePoint.getValue(center.x, 1.0, center.y)
      if (value < 1.0) {
        graphics.color = Color(0, (255.0 - 255.0 * (1.4 - value * 1.3)).toInt(), (255.0 - 255.0 * (1.4 - value * 1.3)).toInt())
      } else {
        graphics.color = Color((255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), (255.0 - 255.0 * ((value - 1.0) * 5)).toInt(), 0)
      }
      /*
      if (scalePoint.getValue(center.getX(), 1, center.getY()) < 1) {
          graphics.setColor(Color.CYAN);
      } else {
          graphics.setColor(Color.ORANGE);
      }*/
      graphics.fillPolygon(polygon.polygon.toDrawable())
      graphics.color = Color.BLACK
      graphics.fillRect(polygon.center.floorX, polygon.center.floorY, 3, 3)
      graphics.drawPolygon(polygon.polygon.toDrawable())
    }
    /*
    centeredPolygonGenerator = new VoronoiPolygonGenerator().setTriangleCenterProvider(TriangleHelper::getIncenter, true);
    centeredPolygons = centeredPolygonGenerator.generate(context,
            new Rectangled(0, 0, width, height), points);
    for (CenteredPolygon polygon : centeredPolygons) {
        graphics.setColor(Color.RED);
        graphics.fillRect(polygon.getCenter().getFloorX(), polygon.getCenter().getFloorY(), 3, 3);
        graphics.drawPolygon(polygon.getPolygon().toDrawable());
    }
*/
    /*
    centeredPolygonGenerator = new DelVorHybridPolygonGenerator();
    centeredPolygons = centeredPolygonGenerator.generate(context,
            new Rectangled(0, 0, width, height), points);
    for (CenteredPolygon polygon : centeredPolygons) {
        graphics.setColor(Color.GREEN);
        graphics.fillRect(polygon.getCenter().getFloorX(), polygon.getCenter().getFloorY(), 3, 3);
        graphics.drawPolygon(polygon.getPolygon().toDrawable());
    }*/

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

  private class DummyContext constructor(override val debugGraphics: Graphics?, override val seed: Long) : GeneratorContext
}
