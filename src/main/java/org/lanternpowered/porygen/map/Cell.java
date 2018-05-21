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
import com.flowpowered.math.vector.Vector2i;
import org.lanternpowered.porygen.util.geom.Polygond;

import java.util.List;
import java.util.Optional;

public interface Cell {

    /**
     * Gets the data attached for the given key.
     *
     * @param key The key
     * @param <T> The object type
     * @return The object if present, otherwise {@link Optional#empty()}
     */
    <T> Optional<T> get(DataKey<T> key);

    /**
     * Sets the data attached for the given key.
     *
     * @param key The key
     * @param object The object
     * @param <T> The object type
     */
    <T> void set(DataKey<T> key, T object);

    /**
     * Gets the center point {@link Vector2d} of this {@link Cell}.
     *
     * @return The center point
     */
    Vector2d getCenterPoint();

    /**
     * Gets whether the specified point {@link Vector2d}
     * is located inside this {@link Cell}.
     *
     * @param point The point
     * @return Whether the point is located in this cell
     */
    default boolean contains(Vector2d point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the specified point {@link Vector2i}
     * is located inside this {@link Cell}.
     *
     * @param point The point
     * @return Whether the point is located in this cell
     */
    default boolean contains(Vector2i point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether the specified point is located
     * inside this {@link Cell}.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether the point is located in this cell
     */
    boolean contains(double x, double y);

    /**
     * Gets the {@link Polygond} of this cell.
     *
     * @return The polygon
     */
    Polygond getPolygon();

    /**
     * Gets the neighbor {@link Cell}s of this cell.
     *
     * @return The neighbor cells
     */
    List<Cell> getNeighbors();

    /**
     * Gets the {@link Edge}s of this cell.
     *
     * @return The edges
     */
    List<Edge> getEdges();

    /**
     * Gets the {@link Corner}s of this cell.
     *
     * @return The corners
     */
    List<Corner> getCorners();
}
