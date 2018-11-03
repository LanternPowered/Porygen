/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen

import com.flowpowered.math.vector.Vector2d
import com.flowpowered.noise.module.modifier.ScalePoint
import com.flowpowered.noise.module.source.Perlin
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.points.ChunkBasedPointsGenerator
import org.lanternpowered.porygen.api.points.PointsGenerator
import org.lanternpowered.porygen.api.points.ZoomPointsGenerator
import org.lanternpowered.porygen.api.points.random.BlueNoiseRandomPointsGenerator
import org.lanternpowered.porygen.api.util.dsi.XoRoShiRo128PlusRandom
import org.lanternpowered.porygen.api.util.geom.Rectangled
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.lanternpowered.porygen.api.map.gen.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.api.map.gen.polygon.VoronoiPolygonGenerator
import org.lanternpowered.porygen.api.map.gen.polygon.VoronoiTriangleCenterProvider
import org.lanternpowered.porygen.api.points.random.GridBasedRandomPointsGenerator
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel

object PointsGeneratorTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val seed = 1452454844896546548L
        val random = XoRoShiRo128PlusRandom(seed)

        // The random points generator
        var generator: PointsGenerator

        generator = BlueNoiseRandomPointsGenerator().apply {
            this.setCellCoverage(0.8, 0.8)
            this.setCells(30, 30)
            this.points = 1000 .. 2000
        }

        /*
        generator = new WhiteNoiseRandomPointsGenerator()
                .setPoints(new Rangei(500, 1000));*/

        /*
        generator = GridBasedRandomPointsGenerator().apply {
            this.setGrid(30, 30)
            this.points = 1000 .. 2000
        }*/

        generator = ZoomPointsGenerator(generator, Vector2d(4.0, 4.0))
        generator = ChunkBasedPointsGenerator(generator)
                .setChunksPerSection(16, 16)

        val width = 1000
        val height = 1000

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.graphics
        val context = DummyContext(graphics, seed, random)

        val perlin = Perlin()
        perlin.frequency = 0.05
        perlin.persistence = 0.5
        perlin.seed = java.lang.Long.hashCode(seed)
        perlin.octaveCount =10

        val scalePoint = ScalePoint()
        scalePoint.setSourceModule(0, perlin)
        scalePoint.xScale = 0.07
        scalePoint.yScale = 1.0
        scalePoint.zScale = 0.07
        /*
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final double value = scalePoint.getValue(x, 1, y);
                // System.out.println(value);
                if (value < 1.0) {
                    graphics.setColor(new Color(0, (int) (255.0 - 255.0 * (1.4 - value * 1.3)), (int) (255.0 - 255.0 * (1.4 - value * 1.3))));
                    // graphics.setColor(Color.CYAN);
                } else {
                    graphics.setColor(new Color((int) (255.0 - (255.0 * ((value - 1.0) * 5))), (int) (255.0 - (255.0 * ((value - 1.0) * 5))), 0));
                    // graphics.setColor(Color.ORANGE);
                }
                graphics.drawRect(x, y, 1, 1);
            }
        }*/

        /*
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        */
        val points = generator.generatePoints(
                context, Rectangled(0.0, 0.0, width.toDouble(), height.toDouble()))
        graphics.color = Color.BLACK
        graphics.drawPolygon(Rectanglei(0, 0, width - 1, height - 1).toDrawable())
        for (point in points) {
            val x = point.floorX
            val y = point.floorY
            graphics.fillRect(x, y, 3, 3)
        }

        var centeredPolygonGenerator: CellPolygonGenerator
        //centeredPolygonGenerator = DelaunayTrianglePolygonGenerator()
        centeredPolygonGenerator = VoronoiPolygonGenerator().apply { this.triangleCenterProvider = VoronoiTriangleCenterProvider.Incenter }
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

    private class DummyContext constructor(override val debugGraphics: Graphics?, override val seed: Long, override val random: Random) : GeneratorContext
}
