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
import org.lanternpowered.porygen.util.geom.Rectangled;
import org.lanternpowered.porygen.util.geom.Rectanglei;

import java.util.List;

/**
 * Represents a piece of a voronoi {@link CellMap}. It allows access to all the
 * {@link Cell}s, etc. that are visible in this {@link CellMapView}.
 */
public interface CellMapView {

    /**
     * Gets the parent {@link CellMap} of this map view.
     *
     * @return The parent
     */
    CellMap getParent();

    /**
     * Gets a sub {@link CellMapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param min The area minimum coordinate
     * @param max The area maximum coordinate
     * @return The new map view
     */
    default CellMapView getSubView(Vector2d min, Vector2d max) {
        return getSubView(new Rectangled(min, max));
    }

    /**
     * Gets a sub {@link CellMapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param min The area minimum coordinate
     * @param max The area maximum coordinate
     * @return The new map view
     */
    default CellMapView getSubView(Vector2i min, Vector2i max) {
        return getSubView(new Rectanglei(min, max));
    }

    /**
     * Gets a sub {@link CellMapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param rectangle The area
     * @return The new map view
     */
    CellMapView getSubView(Rectangled rectangle);

    /**
     * Gets a sub {@link CellMapView} of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param rectangle The area
     * @return The new map view
     */
    CellMapView getSubView(Rectanglei rectangle);

    /**
     * Gets the {@link Cell} the specified point is located in.
     *
     * @param point The point
     * @return The cell
     */
    default Cell getCell(Vector2d point) {
        return getCell(point.getX(), point.getY());
    }

    /**
     * Gets the {@link Cell} the specified point is located in.
     *
     * @param point The point
     * @return The cell
     */
    default Cell getCell(Vector2i point) {
        return getCell(point.getX(), point.getY());
    }

    /**
     * Gets the {@link Cell} the specified point is located in.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The cell
     */
    Cell getCell(double x, double y);

    /**
     * Gets all the {@link Cell}s that are visible
     * in this {@link CellMapView}.
     *
     * @return The cells
     */
    List<Cell> getCells();

    /**
     * Gets all the {@link Site}s that are visible
     * in this {@link CellMapView}.
     *
     * @return The sites
     */
    List<Site> getSites();
}
