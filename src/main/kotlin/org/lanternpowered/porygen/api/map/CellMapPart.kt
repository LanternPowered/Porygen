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
package org.lanternpowered.porygen.api.map

import com.flowpowered.math.vector.Vector2i
import org.lanternpowered.porygen.api.data.DataHolder
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.spongepowered.api.world.World

interface CellMapPart : DataHolder {

    /**
     * The world this cell map part is part of.
     */
    val world: World

    /**
     * Gets a sub [CellMapView] of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param min The area minimum coordinate
     * @param max The area maximum coordinate
     * @return The new map view
     */
    fun getSubView(min: Vector2i, max: Vector2i) = getSubView(Rectanglei(min, max))

    /**
     * Gets a sub [CellMapView] of this map view. The minimum and maximum
     * coordinates will be clamped to the bounds of this view.
     *
     * @param rectangle The area
     * @return The new map view
     */
    fun getSubView(rectangle: Rectanglei): CellMapView

    /**
     * Gets the [Cell] the specified point is located in.
     *
     * @param point The point
     * @return The cell
     */
    fun getCell(point: Vector2i) = getCell(point.x, point.y)

    /**
     * Gets the [Cell] the specified point is located in.
     *
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The cell
     */
    fun getCell(x: Int, z: Int): Cell

    /**
     * Gets the [Corner] for the specified id. Or null if it doesn't exist.
     *
     * @param id The corner id
     * @return The corner
     */
    fun getCorner(id: Long): Corner?

    /**
     * Gets the [Edge] for the specified id. Or null if it doesn't exist.
     *
     * @param id The edge id
     * @return The edge
     */
    fun getEdge(id: Long): Edge?

    /**
     * Gets the [Cell] for the specified id. Or null if it doesn't exist.
     *
     * @param id The cell id
     * @return The cell
     */
    fun getCell(id: Long): Cell?
}
