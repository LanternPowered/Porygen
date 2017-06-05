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
 * Rescales the x and y coordinates with the specified scale. This generator
 * assumes that the points are already generated, this will only rescale existing
 * points.
 */
public class RescalablePointsGenerator implements PointsGenerator {

    private final PointsGenerator parent;
    private final Vector2d scale;

    public RescalablePointsGenerator(PointsGenerator parent, Vector2d scale) {
        this.parent = checkNotNull(parent, "parent");
        this.scale = checkNotNull(scale, "scale");
    }

    @Override
    public void generatePoints(World world, Random random, Rectangled rectangle, List<Vector2d> points) {
        final List<Vector2d> result = new ArrayList<>();
        this.parent.generatePoints(world, random, new Rectangled(rectangle.getMin().mul(this.scale), rectangle.getMax().mul(this.scale)), result);
        result.forEach(point -> points.add(point.mul(this.scale)));
    }
}
