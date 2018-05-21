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
package org.lanternpowered.porygen.map.gen;

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector2d;
import com.google.common.base.MoreObjects;
import org.lanternpowered.porygen.points.PointsGenerator;
import org.lanternpowered.porygen.util.geom.Polygond;

import java.util.Arrays;

/**
 * Represents a {@link Polygond} with
 * it's center point {@link Vector2d}.
 */
public final class CenteredPolygon {

    private final Vector2d center;
    private final Polygond polygon;

    /**
     * Constructs a new {@link CenteredPolygon} with
     * the given center point and {@link Polygond}.
     *
     * @param center The center point
     * @param polygon The polygon
     */
    public CenteredPolygon(Vector2d center, Polygond polygon) {
        checkNotNull(center, "center");
        checkNotNull(polygon, "polygon");
        this.center = center;
        this.polygon = polygon;
    }

    /**
     * Gets the center {@link Vector2d} of the {@link Polygond}.
     * <p>This should be a point that was generated by a
     * {@link PointsGenerator}.
     *
     * @return The center point
     */
    public Vector2d getCenter() {
        return this.center;
    }

    /**
     * Gets the {@link Polygond}.
     *
     * @return The polygon
     */
    public Polygond getPolygon() {
        return this.polygon;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("center", this.center)
                .add("vertices", Arrays.toString(this.polygon.getVertices().toArray()))
                .toString();
    }
}
