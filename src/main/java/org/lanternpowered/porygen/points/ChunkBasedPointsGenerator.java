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
import org.lanternpowered.porygen.util.Rectangled;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This generator generates points for a specified world, the points are generated
 * based of the chunk coordinates and the world seed resulting in always the same
 * results when the points generator is re-run.
 */
public class ChunkBasedPointsGenerator implements PointsGenerator {

    private final PointsGenerator parent;

    public ChunkBasedPointsGenerator(PointsGenerator parent) {
        this.parent = checkNotNull(parent, "parent");
    }

    @Override
    public void generatePoints(World world, Random random, Rectangled rectangle, List<Vector2d> points) {
        final int minX = rectangle.getMin().getFloorX() >> 4;
        final int minZ = rectangle.getMin().getFloorY() >> 4;
        final int maxX = rectangle.getMax().getFloorX() >> 4;
        final int maxZ = rectangle.getMax().getFloorY() >> 4;
        final long worldSeed = world.getProperties().getSeed();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                random.setSeed(((long) x * 341873128712L + (long) z * 132897987541L) ^ worldSeed);
                final int xPos = x << 4;
                final int zPos = z << 4;
                final Rectangled chunkArea = new Rectangled(xPos, zPos, xPos & 0xf, zPos & 0xf);

                // When generating points for the edge chunks, points may fall
                // outside the scope of the original rectangle, filter out those
                if (x == minX || z == minZ || x == maxX || z == maxZ) {
                    final List<Vector2d> chunkPoints = new ArrayList<>();
                    this.parent.generatePoints(world, random, chunkArea, chunkPoints);
                    chunkPoints.stream().filter(rectangle::contains).forEach(points::add);
                } else {
                    this.parent.generatePoints(world, random, chunkArea, points);
                }
            }
        }
    }
}
