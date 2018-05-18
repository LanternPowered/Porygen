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
import org.lanternpowered.porygen.util.Rectangled;

import java.util.List;

/**
 * Represents a piece of a voronoi {@link Map}. It allows access to all the
 * {@link Cell}s, etc. that are visible in this {@link MapView}.
 */
public interface MapView {

    /**
     * Gets the parent {@link Map} of this map view.
     *
     * @return The parent
     */
    Map getParent();

    /**
     * Gets a sub {@link MapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param min The area minimum coordinate
     * @param max The area maximum coordinate
     * @return The new map view
     */
    default MapView getSubView(Vector2d min, Vector2d max) {
        return getSubView(new Rectangled(min, max));
    }

    /**
     * Gets a sub {@link MapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param rectangle The area
     * @return The new map view
     */
    MapView getSubView(Rectangled rectangle);

    /**
     * Gets the {@link Cell} the specified point is located in.
     *
     * @param point The point
     * @return The cell
     */
    Cell getCell(Vector2d point);

    /**
     * Gets all the {@link Cell}s that are visible
     * in this {@link MapView}.
     *
     * @return The cells
     */
    List<Cell> getCells();

    /**
     * Gets all the {@link Site}s that are visible
     * in this {@link MapView}.
     *
     * @return The sites
     */
    List<Site> getSites();
}