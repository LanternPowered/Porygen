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
import org.lanternpowered.porygen.impl.map.SectionPolygonGenerator
import org.lanternpowered.porygen.impl.map.SectionPosition
import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.polygon.DelaunayTrianglePolygonGenerator
import org.lanternpowered.porygen.map.polygon.TriangleCenterProvider
import org.lanternpowered.porygen.map.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.points.BlueNoisePointsGenerator
import org.lanternpowered.porygen.points.GridBasedPointsGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.points.ZoomPointsGenerator
import org.lanternpowered.porygen.util.random.Xor128Random
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.noise.module.modifier.ScalePoint
import org.spongepowered.noise.module.source.Perlin
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.lang.Exception
import javax.swing.JFrame
import javax.swing.JPanel

object PointsGeneratorTest {

  @JvmStatic
  fun main(args: Array<String>) {
    val seed = 1452454844896546548L
    val random = Xor128Random(seed)

    // The random points generator
    var generator: PointsGenerator = BlueNoisePointsGenerator(amount = 200..250)
    // generator = GridBasedPointsGenerator(amount = 200..250)
    generator = ZoomPointsGenerator(generator, Vector2d(1.1, 1.1))

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
    /*
    val points = sectionBasedPointsGenerator.generate(
        context, Rectangled(0.0, 0.0, width.toDouble(), height.toDouble()))
    graphics.color = Color.BLACK
    graphics.drawPolygon(Rectanglei(Vector2i(0, 0), Vector2i(width - 1, height - 1)).toDrawable())
    for (point in points) {
      val x = point.floorX
      val y = point.floorY
      graphics.fillRect(x, y, 3, 3)
    }
    */

    /*
    for (point in generator.generate(Xor128Random(seed))) {
      graphics.color = Color.BLACK
      graphics.fillRect((point.x * width).toInt(), (point.y * height).toInt(), 3, 3)
    }*/

    var polygonGenerator: CellPolygonGenerator
    val scale = Vector2d(width.toDouble(), height.toDouble())
    polygonGenerator = VoronoiPolygonGenerator(TriangleCenterProvider.Circumcenter)
    // polygonGenerator = DelaunayTrianglePolygonGenerator()

    val sectionPolygonGenerator = SectionPolygonGenerator(seed, Vector2i(300, 300), generator, polygonGenerator)
    val centeredPolygons = mutableListOf<CellPolygon>()
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 0)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(0, 1)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(1, 1)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 2)))
    centeredPolygons.addAll(sectionPolygonGenerator.generate(SectionPosition(2, 0)))

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
