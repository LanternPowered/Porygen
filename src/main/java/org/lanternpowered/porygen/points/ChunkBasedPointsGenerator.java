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

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector2d;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.util.Rectangled;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This generator generates points for a specified world, the points are generated
 * based of the chunk coordinates and the world seed resulting in always the same
 * results when the points generator is re-run.
 * <p>When generating underlying points, chunks are grouped into sections
 * and for this section will the underlying algorithm be executed, if the point
 * lies outside of the original rectangle, it will be filtered out.
 */
public class ChunkBasedPointsGenerator implements PointsGenerator {

    private final PointsGenerator parent;

    private int chunksPerXSection = 1;
    private int chunksPerYSection = 1;

    public ChunkBasedPointsGenerator(PointsGenerator parent) {
        this.parent = checkNotNull(parent, "parent");
    }

    public int getChunksPerXSection() {
        return this.chunksPerXSection;
    }

    public ChunkBasedPointsGenerator setChunksPerXSection(int chunksPerXSection) {
        this.chunksPerXSection = chunksPerXSection;
        return this;
    }

    public int getChunksPerYSection() {
        return this.chunksPerYSection;
    }

    public ChunkBasedPointsGenerator setChunksPerYSection(int chunksPerYSection) {
        this.chunksPerYSection = chunksPerYSection;
        return this;
    }

    public ChunkBasedPointsGenerator setChunksPerSection(int chunksPerXSection, int chunksPerYSection) {
        this.chunksPerXSection = chunksPerXSection;
        this.chunksPerYSection = chunksPerYSection;
        return this;
    }

    @Override
    public List<Vector2d> generatePoints(GeneratorContext context, Random random, Rectangled rectangle) {
        final List<Vector2d> points = new ArrayList<>();

        int minX = rectangle.getMin().getFloorX() >> 4;
        minX -= minX % this.chunksPerXSection;
        int minY = rectangle.getMin().getFloorY() >> 4;
        minY -= minY % this.chunksPerYSection;
        int maxX = rectangle.getMax().getFloorX() >> 4;
        maxX -= maxX % this.chunksPerXSection;
        int maxY = rectangle.getMax().getFloorY() >> 4;
        maxY -= maxY % this.chunksPerYSection;

        final long worldSeed = context.getSeed();
        for (int x = minX; x <= maxX; x += this.chunksPerXSection) {
            for (int y = minY; y <= maxY; y += this.chunksPerYSection) {
                final int xStart = x << 4;
                final int yStart = y << 4;
                final int xEnd = ((x + this.chunksPerXSection - 1) << 4) | 0xf;
                final int yEnd = ((y + this.chunksPerYSection - 1) << 4) | 0xf;

                context.getDebugGraphics().ifPresent(graphics -> {
                    final Color color = graphics.getColor();
                    graphics.setColor(Color.RED);
                    graphics.drawRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
                    graphics.setColor(color);
                });

                final Rectangled chunkArea = new Rectangled(xStart, yStart, xEnd, yEnd);
                random.setSeed(((long) x * 341873128712L + (long) y * 132897987541L) ^ worldSeed);

                // When generating points for the edge chunks, points may fall
                // outside the scope of the original rectangle, filter out those
                if (x == minX || y == minY || x == maxX || y == maxY) {
                    this.parent.generatePoints(context, random, chunkArea).stream()
                            .filter(rectangle::contains).forEach(points::add);
                } else {
                    points.addAll(this.parent.generatePoints(context, random, chunkArea));
                }
            }
        }

        return points;
    }
}
