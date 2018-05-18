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

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Zooms to a specific area on points from the
 * underlying {@link PointsGenerator}.
 */
public class ZoomPointsGenerator implements PointsGenerator {

    private final PointsGenerator parent;
    private final Vector2d zoomFactor;

    public ZoomPointsGenerator(PointsGenerator parent, Vector2d zoomFactor) {
        this.parent = checkNotNull(parent, "parent");
        this.zoomFactor = checkNotNull(zoomFactor, "zoomFactor");
    }

    @Override
    public List<Vector2d> generatePoints(World world, Random random, Rectangled rectangle) {
        return this.parent.generatePoints(world, random, rectangle).stream()
                .map(point -> point.mul(this.zoomFactor))
                .filter(rectangle::contains)
                .collect(Collectors.toList());
    }
}
