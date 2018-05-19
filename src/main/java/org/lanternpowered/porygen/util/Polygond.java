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
package org.lanternpowered.porygen.util;

import static com.google.common.base.Preconditions.checkState;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import com.google.common.collect.Lists;

import java.awt.Polygon;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a polygon.
 */
public final class Polygond {

    private final List<Vector2d> vertices;

    /**
     * Constructs a {@link Polygond} from the given vertices.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     */
    public Polygond(Vector2d... vertices) {
        checkState(vertices.length >= 3, "There must be at least 3 vertices.");
        this.vertices = Collections.unmodifiableList(Arrays.asList(vertices));
    }

    /**
     * Constructs a {@link Polygond} from the given vertices.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     */
    public Polygond(Iterable<Vector2d> vertices) {
        this.vertices = Lists.newArrayList(vertices);
        checkState(this.vertices.size() >= 3, "There must be at least 3 vertices.");
    }

    /**
     * Gets a {@link List} with all the vertices
     * in this {@link Polygond}.
     * <p>The vertices should be sorted clockwise.
     *
     * @return The vertices
     */
    public List<Vector2d> getVertices() {
        return this.vertices;
    }

    /**
     * Gets whether the given point {@link Vector2d}
     * are located within this {@link Polygond}.
     *
     * @param point The point
     * @return Whether this polygon contains the point
     */
    public boolean contains(Vector2i point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the given point {@link Vector2d}
     * are located within this {@link Polygond}.
     *
     * @param point The point
     * @return Whether this polygon contains the point
     */
    public boolean contains(Vector2d point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the given point coordinates
     * are located within this {@link Polygond}.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this polygon contains the point
     */
    public boolean contains(double x, double y) {
        // https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = this.vertices.size() - 1; i < this.vertices.size(); j = i++) {
            final Vector2d vi = this.vertices.get(i);
            final Vector2d vj = this.vertices.get(j);
            if ((vi.getY() > y) != (vj.getY() > y) &&
                    (x < (vj.getX() - vi.getX()) * (y - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX())) {
                result = !result;
            }
        }
        return result;
    }

    /**
     * Converts this {@link Polygond} into
     * a drawable {@link Polygon}.
     *
     * @return The drawable polygon
     */
    public Polygon toDrawable() {
        final int[] pointsX = new int[this.vertices.size()];
        final int[] pointsY = new int[this.vertices.size()];
        for (int i = 0; i < pointsX.length; i++) {
            final Vector2d vertex = this.vertices.get(i);
            pointsX[i] = (int) vertex.getX();
            pointsY[i] = (int) vertex.getY();
        }
        return new Polygon(pointsX, pointsY, pointsX.length);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(
                this.vertices.stream().map(p -> p.getX() + ", " + p.getY()).toArray());
    }
}
