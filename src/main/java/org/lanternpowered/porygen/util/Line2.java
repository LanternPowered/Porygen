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

public interface Line2<P> {

    /**
     * Gets the start point of this {@link Line2}.
     *
     * @return The start point
     */
    P getStart();

    /**
     * Gets the end point of this {@link Line2}.
     *
     * @return The end point
     */
    P getEnd();

    /**
     * Gets whether this {@link Line2} intersects with
     * the given {@link Line2d}.
     *
     * @param line The line
     * @return Whether the lines intersect
     */
    default boolean intersects(Line2d line) {
        return intersects(line.getStart(), line.getEnd());
    }

    /**
     * Gets whether this {@link Line2} intersects with
     * the given coordinate based line.
     *
     * @param start The start point
     * @param end The end point
     * @return Whether the lines intersect
     */
    default boolean intersects(Vector2i start, Vector2i end) {
        return intersects(start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Gets whether this {@link Line2} intersects with
     * the given coordinate based line.
     *
     * @param start The start point
     * @param end The end point
     * @return Whether the lines intersect
     */
    default boolean intersects(Vector2d start, Vector2d end) {
        return intersects(start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Gets whether this {@link Line2} intersects with
     * the given coordinate based line.
     *
     * @param startX The start x coordinate
     * @param startY The start y coordinate
     * @param endX The end x coordinate
     * @param endY The end y coordinate
     * @return Whether the lines intersect
     */
    boolean intersects(double startX, double startY, double endX, double endY);

    /**
     * Gets whether this {@link Line2} intersects with
     * the given coordinate based line.
     *
     * @param startX The start x coordinate
     * @param startY The start y coordinate
     * @param endX The end x coordinate
     * @param endY The end y coordinate
     * @return Whether the lines intersect
     */
    default boolean intersects(int startX, int startY, int endX, int endY) {
        return intersects((double) startX, (double) startY, (double) endX, (double) endY);
    }

    /**
     * Converts this {@link Line2}
     * into a {@link Line2i}.
     *
     * @return The int line
     */
    Line2i toInt();

    /**
     * Converts this {@link Line2}
     * into a {@link Line2d}.
     *
     * @return The double line
     */
    Line2d toDouble();
}
