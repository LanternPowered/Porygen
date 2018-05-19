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
package org.lanternpowered.porygen.points;

import com.flowpowered.math.vector.Vector2d;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.points.random.BlueNoiseRandomPointsGenerator;
import org.lanternpowered.porygen.util.Rangei;
import org.lanternpowered.porygen.util.Rectangled;
import org.lanternpowered.porygen.util.dsi.SeedUtil;
import org.lanternpowered.porygen.util.dsi.XoRoShiRo128PlusRandom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PointsGeneratorTest {

    public static void main(String... args) {
        final Random random = new XoRoShiRo128PlusRandom();

        // The random points generator
        PointsGenerator generator;

        generator = new BlueNoiseRandomPointsGenerator()
                .setCellCoverage(0.8, 0.8)
                .setCells(10, 10)
                .setPoints(new Rangei(100, 200));

        /*
        generator = new WhiteNoiseRandomPointsGenerator()
                .setPoints(new Rangei(200, 500));
        */

        generator = new ZoomPointsGenerator(generator, new Vector2d(3.0, 3.0));
        generator = new ChunkBasedPointsGenerator(generator)
                .setChunksPerSection(16, 16);

        final int width = 1000;
        final int height = 1000;

        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = image.getGraphics();
        final DummyContext context = new DummyContext(graphics);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        final List<Vector2d> points = generator.generatePoints(
                context, random, new Rectangled(0, 0, width, height));
        graphics.setColor(Color.BLACK);
        graphics.drawLine(0, 0, width - 1, 0);
        graphics.drawLine(width - 1, 0, width - 1, height - 1);
        graphics.drawLine(0, 0, 0, height - 1);
        graphics.drawLine(0, height - 1, width - 1, height - 1);
        for (Vector2d point : points) {
            final int x = point.getFloorX();
            final int y = point.getFloorY();
            graphics.fillRect(x, y, 3, 3);
        }

        final JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }
        };

        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.setSize(width, height);
        frame.setVisible(true);

        canvas.repaint();
    }

    private static final class DummyContext implements GeneratorContext {

        private final long seed = SeedUtil.randomSeed();
        private final Graphics graphics;

        private DummyContext(Graphics graphics) {
            this.graphics = graphics;
        }

        @Override
        public long getSeed() {
            return this.seed;
        }

        @Override
        public Optional<Graphics> getDebugGraphics() {
            return Optional.of(this.graphics);
        }
    }
}
