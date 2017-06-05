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

import java.util.List;

public interface Cell {

    /**
     * Gets the {@link Site} of this {@link Cell}.
     *
     * @return The site
     */
    Site getSite();

    /**
     * Gets whether the specified point is located
     * inside this {@link Cell}.
     *
     * @param point The point
     * @return Whether the point is located in this cell
     */
    boolean contains(Vector2d point);

    /**
     * Gets the neighbor {@link Cell}s of this cell.
     *
     * @return The neighbor cells
     */
    List<Cell> getNeighbors();

    /**
     * Gets the vertices of this {@link Cell}. These vertices
     * are sorted to form a polygon.
     *
     * @return The vertices
     */
    List<Vector2d> getVertices();
}
