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
package org.lanternpowered.porygen.points.random;

import com.flowpowered.math.vector.Vector2d;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.points.PointsGenerator;
import org.lanternpowered.porygen.util.Rectangled;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link PointsGenerator} that generates random
 * points within the {@link Rectangled}.
 */
public class WhiteNoiseRandomPointsGenerator extends AbstractRandomPointsGenerator<WhiteNoiseRandomPointsGenerator> {

    @Override
    public List<Vector2d> generatePoints(GeneratorContext context, Random random, Rectangled rectangle) {
        final List<Vector2d> points = new ArrayList<>();

        final int min = getPoints().getMin();
        final int max = getPoints().getMax();

        // Randomize the amount of points that will be generated
        final int amount = min + random.nextInt(max - min + 1);

        final double minX = rectangle.getMin().getX();
        final double minY = rectangle.getMin().getY();
        final double maxX = rectangle.getMax().getX();
        final double maxY = rectangle.getMax().getY();

        final double dX = maxX - minX;
        final double dY = maxY - minY;

        for (int i = 0; i < amount; i++) {
            final double x = minX + random.nextDouble() * dX;
            final double y = minY + random.nextDouble() * dY;
            points.add(new Vector2d(x, y));
        }

        return points;
    }
}
