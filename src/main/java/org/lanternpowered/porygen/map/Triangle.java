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
package org.lanternpowered.porygen.map;

import com.flowpowered.math.vector.Vector2d;
import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.Nullable;

/**
 * Represents a triangle.
 */
public final class Triangle implements Iterable<Vector2d> {

    private final Vector2d[] vertices;
    @Nullable private Vector2d circumcenter;

    /**
     * Constructs a new {@link Triangle}.
     *
     * @param a The first point
     * @param b The second point
     * @param c The third point
     */
    public Triangle(Vector2d a, Vector2d b, Vector2d c) {
        this.vertices = new Vector2d[] { a, b, c };
    }

    /**
     * Gets the first point.
     *
     * @return The point
     */
    public Vector2d a() {
        return get(0);
    }

    /**
     * Gets the second point.
     *
     * @return The point
     */
    public Vector2d b() {
        return get(1);
    }

    /**
     * Gets the third point.
     *
     * @return The point
     */
    public Vector2d c() {
        return get(2);
    }

    /**
     * Gets the point for the specified index (0..2).
     *
     * @param index The index
     * @return The point
     */
    public Vector2d get(int index) {
        return this.vertices[index];
    }

    /**
     * Gets whether the specified {@link Triangle} is
     * a neighbor of this triangle.
     *
     * @param triangle The other triangle
     * @return Whether the other triangle a neighbor is of this triangle
     */
    public boolean isNeighbor(Triangle triangle) {
        int count = 0;
        for (Vector2d vertex : this) {
            if (!triangle.containsVertex(vertex)) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Gets whether the specified vertex is present in
     * this {@link Triangle}.
     *
     * @param vertex The vertex
     * @return Is present
     */
    public boolean containsVertex(Vector2d vertex) {
        for (Vector2d oVertex : this.vertices) {
            if (vertex.equals(oVertex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets whether the specified point is located inside
     * this {@link Triangle}.
     *
     * @param point The point
     * @return Is located inside
     */
    public boolean contains(Vector2d point) {
        // TODO
        return false;
    }

    /**
     * Gets the circumcenter of this {@link Triangle}.
     *
     * @return The circumcenter
     */
    public Vector2d getCircumcenter() {
        // Initialize the circumcenter lazily
        if (this.circumcenter != null) {
            return this.circumcenter;
        }

        final Vector2d a = a();
        final Vector2d b = b();
        final Vector2d c = c();

        // determine midpoints (average of x & y coordinates)
        final Vector2d midAB = a.add(b).div(2);
        final Vector2d midBC = b.add(c).div(2);

        // determine slope
        // we need the negative reciprocal of the slope to get the slope of the perpendicular bisector
        final double slopeAB = -1.0 / slope(a, b);
        final double slopeBC = -1.0 / slope(b, c);

        // y = mx + b
        // solve for b
        final double bAB = midAB.getY() - slopeAB * midAB.getX();
        final double bBC = midBC.getY() - slopeBC * midBC.getX();

        // solve for x & y
        // x = (b1 - b2) / (m2 - m1)
        double x = (bAB - bBC) / (slopeBC - slopeAB);
        double y = (slopeAB * x) + bAB;

        return this.circumcenter = new Vector2d(x, y);
    }

    private static double slope(Vector2d a, Vector2d b) {
        return (b.getY() - a.getY()) / (b.getX() - a.getX());
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return Arrays.stream(this.vertices).iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("points", Arrays.toString(this.vertices))
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a(), b(), c());
    }
}
