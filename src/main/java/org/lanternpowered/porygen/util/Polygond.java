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
import com.google.common.collect.ImmutableList;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a polygon.
 */
public final class Polygond extends AbstractShape {

    private final List<Vector2d> vertices;

    /**
     * Constructs a {@link Polygond} from the given vertices.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     */
    public Polygond(Vector2d... vertices) {
        checkState(vertices.length >= 3, "There must be at least 3 vertices.");
        this.vertices = ImmutableList.copyOf(vertices);
    }

    /**
     * Constructs a {@link Polygond} from the given vertices.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     */
    public Polygond(Iterable<Vector2d> vertices) {
        this.vertices = ImmutableList.copyOf(vertices);
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

    @Override
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

    @Override
    public boolean contains(double minX, double minY, double maxX, double maxY) {
        // Just check if the 4 corners are inside this polygon
        return contains(minX, minY) &&
                contains(minX, maxY) &&
                contains(maxX, minY) &&
                contains(maxX, maxY);
    }

    @Override
    public boolean contains(int minX, int minY, int maxX, int maxY) {
        // Just check if the 4 corners are inside this polygon
        return contains(minX, minY) &&
                contains(minX, maxY) &&
                contains(maxX, minY) &&
                contains(maxX, maxY);
    }

    @Override
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        int i;
        int j;
        for (i = 0, j = this.vertices.size() - 1; i < this.vertices.size(); j = i++) {
            final Vector2d vi = this.vertices.get(i);
            final Vector2d vj = this.vertices.get(j);
            if (Line2D.linesIntersect(minX, minY, maxX, minY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (Line2D.linesIntersect(maxX, minY, maxX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (Line2D.linesIntersect(minX, minY, minX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (Line2D.linesIntersect(minX, maxY, maxX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Polygond polygon) {
        int i;
        int j;
        for (i = 0, j = this.vertices.size() - 1; i < this.vertices.size(); j = i++) {
            final Vector2d vi = this.vertices.get(i);
            final Vector2d vj = this.vertices.get(j);
            int k;
            int l;
            for (k = 0, l = this.vertices.size() - 1; k < this.vertices.size(); l = k++) {
                final Vector2d vk = polygon.vertices.get(k);
                final Vector2d vl = polygon.vertices.get(l);
                if (Line2D.linesIntersect(vk.getX(), vk.getY(), vl.getX(), vl.getY(), vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                    return true;
                }
            }
        }
        return false;
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
        return getClass().getSimpleName() + Arrays.toString(this.vertices.toArray());
    }
}
