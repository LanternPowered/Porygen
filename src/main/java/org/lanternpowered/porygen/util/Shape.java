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

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;

public interface Shape {

    /**
     * Gets whether the given {@link Shape}
     * is located inside this {@link Shape}.
     *
     * @param shape The shape
     * @return Whether the shape is located inside this 2d shape
     */
    default boolean contains(Shape shape) {
        if (shape instanceof Rectangled) {
            return contains((Rectangled) shape);
        } else if (shape instanceof Rectanglei) {
            return contains((Rectanglei) shape);
        } else if (shape instanceof Polygond) {
            return contains((Polygond) shape);
        }
        throw new IllegalStateException();
    }

    /**
     * Gets whether the given {@link Rectanglei}
     * is located inside this {@link Shape}.
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is located inside this 2d shape
     */
    default boolean contains(Rectanglei rectangle) {
        final Vector2i min = rectangle.getMin();
        final Vector2i max = rectangle.getMax();
        return contains(min.getX(), min.getY(), max.getX(), max.getY());
    }

    /**
     * Gets whether the given {@link Rectangled}
     * is located inside this {@link Shape}.
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is located inside this 2d shape
     */
    default boolean contains(Rectangled rectangle) {
        final Vector2d min = rectangle.getMin();
        final Vector2d max = rectangle.getMax();
        return contains(min.getX(), min.getY(), max.getX(), max.getY());
    }

    /**
     * Gets whether the given rectangle coordinates
     * are located in this {@link Shape}.
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is located inside this 2d shape
     */
    boolean contains(double minX, double minY, double maxX, double maxY);

    /**
     * Gets whether the given rectangle coordinates
     * are located in this {@link Shape}.
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is located inside this 2d shape
     */
    default boolean contains(int minX, int minY, int maxX, int maxY) {
        return contains((double) minX, (double) minY, (double) maxX, (double) maxY);
    }

    /**
     * Gets whether the given {@link Polygond}
     * is located inside this {@link Shape}.
     *
     * @param polygon The polygon
     * @return Whether the polygon is located inside this 2d shape
     */
    boolean contains(Polygond polygon);

    /**
     * Gets whether the given {@link Shape}
     * intersects with this {@link Shape}.
     *
     * @param shape The shape
     * @return Whether the shape is intersecting with this shape
     */
    default boolean intersects(Shape shape) {
        if (shape instanceof Rectangled) {
            return intersects((Rectangled) shape);
        } else if (shape instanceof Rectanglei) {
            return intersects((Rectanglei) shape);
        } else if (shape instanceof Polygond) {
            return intersects((Polygond) shape);
        }
        throw new IllegalStateException();
    }

    /**
     * Gets whether the given {@link Rectanglei}
     * intersects with this {@link Shape}.
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is intersecting with this shape
     */
    default boolean intersects(Rectanglei rectangle) {
        final Vector2i min = rectangle.getMin();
        final Vector2i max = rectangle.getMax();
        return intersects(min.getX(), min.getY(), max.getX(), max.getY());
    }

    /**
     * Gets whether the given {@link Rectangled}
     * intersects with this {@link Shape}.
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is intersecting with this shape
     */
    default boolean intersects(Rectangled rectangle) {
        final Vector2d min = rectangle.getMin();
        final Vector2d max = rectangle.getMax();
        return intersects(min.getX(), min.getY(), max.getX(), max.getY());
    }

    /**
     * Gets whether the given rectangle coordinates
     * intersect with this {@link Shape}.
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is intersecting with this shape
     */
    boolean intersects(double minX, double minY, double maxX, double maxY);

    /**
     * Gets whether the given rectangle coordinates
     * intersects with this {@link Shape}.
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is intersecting with this shape
     */
    default boolean intersects(int minX, int minY, int maxX, int maxY) {
        return intersects((double) minX, (double) minY, (double) maxX, (double) maxY);
    }

    /**
     * Gets whether the given {@link Polygond}
     * intersects with this {@link Shape}.
     *
     * @param polygon The polygon
     * @return Whether the polygon is intersecting with this shape
     */
    boolean intersects(Polygond polygon);

    /**
     * Gets whether the given point {@link Vector2d}
     * are located within this {@link Shape}.
     *
     * @param point The point
     * @return Whether this 2d shape contains the point
     */
    default boolean contains(Vector2i point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the given point {@link Vector2d}
     * are located within this {@link Shape}.
     *
     * @param point The point
     * @return Whether this 2d shape contains the point
     */
    default boolean contains(Vector2d point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the given point coordinates
     * are located within this {@link Shape}.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this 2d shape contains the point
     */
    boolean contains(double x, double y);

    /**
     * Gets whether the given point coordinates
     * are located within this {@link Shape}.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this 2d shape contains the point
     */
    default boolean contains(int x, int y) {
        return contains((double) x, (double) y);
    }
}
