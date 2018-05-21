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
package org.lanternpowered.porygen.util.geom;

import static com.google.common.base.Preconditions.checkState;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import com.google.common.collect.ImmutableList;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a polygon.
 */
@SuppressWarnings("RedundantIfStatement")
public final class Polygond extends AbstractShape {

    /**
     * Creates a {@link Polygond} from the given vertices
     * of which is known that it is a convex polygon.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     * @return The polygon
     */
    public static Polygond newConvexPolygon(Vector2d... vertices) {
        final Polygond polygon = new Polygond(vertices);
        polygon.isConvex = 1;
        return polygon;
    }

    /**
     * Creates a {@link Polygond} from the given vertices
     * of which is known that it is a convex polygon.
     * <p>The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     * @return The polygon
     */
    public static Polygond newConvexPolygon(Iterable<Vector2d> vertices) {
        final Polygond polygon = new Polygond(vertices);
        polygon.isConvex = 1;
        return polygon;
    }

    private final List<Vector2d> vertices;

    // Whether the polygon is convex, -1 means not yet computed
    private int isConvex = -1;

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
     * Gets the centroid of this polygon.
     *
     * @return The centroid
     */
    public Vector2d getCentroid() {
        double x = 0;
        double y = 0;
        for (Vector2d vertex : this.vertices) {
            x += vertex.getX();
            y += vertex.getY();
        }
        return new Vector2d(
                x / (double) this.vertices.size(),
                y / (double) this.vertices.size());
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
     * Gets whether this is a convex {@link Polygond}.
     *
     * @return Is convex polygon
     */
    public boolean isConvex() {
        // Lazily compute convex
        if (this.isConvex != -1) {
            return this.isConvex > 0;
        }
        final boolean isConvex = isConvex0();
        this.isConvex = isConvex ? 1 : 0;
        return isConvex;
    }

    private boolean isConvex0() {
        // https://stackoverflow.com/questions/471962/how-do-i-efficiently-determine-if-a-polygon-is-convex-non-convex-or-complex
        if (this.vertices.size() < 4) {
            return true;
        }
        boolean sign = false;
        final int n = this.vertices.size();
        for (int i = 0; i < n; i++) {
            final Vector2d a = this.vertices.get(i);
            final Vector2d b = this.vertices.get((i + 1) % n);
            final Vector2d c = this.vertices.get((i + 2) % n);

            final double dx1 = c.getX() - b.getX();
            final double dy1 = c.getY() - b.getY();
            final double dx2 = a.getX() - b.getX();
            final double dy2 = a.getY() - b.getY();

            final boolean sign1 = dx1 * dy2 - dy1 * dx2 > 0;
            if (i == 0) {
                sign = sign1;
            } else if (sign != sign1) {
                return false;
            }
        }
        return true;
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
        // Non convex polygons need special handling
        if (!isConvex() && trueIntersection(minX, minY, maxX, maxY)) {
            return false;
        }
        // Just check if the 4 corners are inside this polygon
        return contains(minX, minY) &&
                contains(minX, maxY) &&
                contains(maxX, minY) &&
                contains(maxX, maxY);
    }

    @Override
    public boolean contains(int minX, int minY, int maxX, int maxY) {
        // Non convex polygons need special handling
        if (!isConvex() && trueIntersection(minX, minY, maxX, maxY)) {
            return false;
        }
        // Just check if the 4 corners are inside this polygon
        return contains(minX, minY) &&
                contains(minX, maxY) &&
                contains(maxX, minY) &&
                contains(maxX, maxY);
    }

    boolean trueIntersection(Shape shape) {
        if (shape instanceof Rectangled) {
            return trueIntersection((Rectangled) shape);
        } else if (shape instanceof Rectanglei) {
            return trueIntersection((Rectanglei) shape);
        } else if (shape instanceof Polygond) {
            return trueIntersection((Polygond) shape);
        }
        throw new IllegalStateException();
    }

    private boolean trueIntersection(Rectanglei rectangle) {
        final Vector2i min = rectangle.getMin();
        final Vector2i max = rectangle.getMax();
        return trueIntersection(min.getX(), min.getY(), max.getX(), max.getY());
    }

    private boolean trueIntersection(Rectangled rectangle) {
        final Vector2d min = rectangle.getMin();
        final Vector2d max = rectangle.getMax();
        return trueIntersection(min.getX(), min.getY(), max.getX(), max.getY());
    }

    private boolean trueIntersection(Polygond polygon) {
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
                if (trueIntersection(vk.getX(), vk.getY(), vl.getX(), vl.getY(), vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Similar to {@link #intersects(double, double, double, double)}, but doesn't see
     * parallel lines as an intersection, this method will be used by contains methods.
     * So, lines really have to cross in order to have an intersection, and lines can not
     * be just on top of each other.
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether there is a true intersection
     */
    private boolean trueIntersection(double minX, double minY, double maxX, double maxY) {
        int i;
        int j;
        for (i = 0, j = this.vertices.size() - 1; i < this.vertices.size(); j = i++) {
            final Vector2d vi = this.vertices.get(i);
            final Vector2d vj = this.vertices.get(j);
            if (trueIntersection(minX, minY, maxX, minY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (trueIntersection(maxX, minY, maxX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (trueIntersection(minX, minY, minX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
            if (trueIntersection(minX, maxY, maxX, maxY, vi.getX(), vi.getY(), vj.getX(), vj.getY())) {
                return true;
            }
        }
        return false;
    }

    private static boolean trueIntersection(
            double x1, double y1,
            double x2, double y2,
            double x3, double y3,
            double x4, double y4) {
        // Fail fast if possible
        if ((x1 == x3 && y1 == y3) ||
                (x2 == x4 && y2 == y4)) {
            return false;
        }
        // If they don't intersect, well that's the end
        if (!Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
            return false;
        }

        // https://stackoverflow.com/questions/17692922/check-is-a-point-x-y-is-between-two-points-drawn-on-a-straight-line

        // C: start point line 1
        // B: end point line 1
        // A: a point from line 2
        // C---A-------B

        // |CB|
        final double dx1 = x2 - x1;
        final double dy1 = y2 - y1;
        // square distance
        final double d1 = dx1 * dx1 + dy1 * dy1;

        // first point 3, start point from line 2

        // |CA|
        double dx2 = x3 - x1;
        double dy2 = x3 - x1;
        // square distance
        double d2 = dx2 * dx2 + dy2 * dy2;

        // |AB|
        double dx3 = x2 - x3;
        double dy3 = y2 - y3;
        // square distance
        double d3 = dx3 * dx3 + dy3 * dy3;
        if (d2 + d3 == d1) {
            // point a is on the line, true intersection is not possible
            return false;
        }

        // then point 4, end point from line 2

        // |CA|
        dx2 = x4 - x1;
        dy2 = y4 - y1;
        // square distance
        d2 = dx2 * dx2 + dy2 * dy2;

        // |AB|
        dx3 = x2 - x4;
        dy3 = y2 - y4;
        // square distance
        d3 = dx3 * dx3 + dy3 * dy3;
        if (d2 + d3 == d1) {
            // point a is on the line, true intersection is not possible
            return false;
        }

        return true;
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
