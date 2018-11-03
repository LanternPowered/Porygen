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
import org.lanternpowered.porygen.api.util.geom.Polygond

/**
 * Represents a cell within a [CellMap].
 */
interface Cell : CellMapElement, DataHolder {

    /**
     * Gets the center point [Vector2i] of this [Cell].
     *
     * @return The center point
     */
    val centerPoint: Vector2i

    /**
     * Gets the [Polygond] of this cell.
     *
     * @return The polygon
     */
    val polygon: Polygond

    /**
     * Gets the neighbor [Cell]s of this cell.
     *
     * @return The neighbor cells
     */
    val neighbors: Collection<Cell>

    /**
     * Gets the [Edge]s of this cell.
     *
     * @return The edges
     */
    val edges: Collection<Edge>

    /**
     * Gets the [Corner]s of this cell.
     *
     * @return The corners
     */
    val corners: Collection<Corner>

    /**
     * Gets whether the specified point [Vector2i]
     * is located inside this [Cell].
     *
     * @param point The point
     * @return Whether the point is located in this cell
     */
    operator fun contains(point: Vector2i) = contains(point.x, point.y)

    /**
     * Gets whether the specified point is located
     * inside this [Cell].
     *
     * @param x The x coordinate
     * @param z The z coordinate
     * @return Whether the point is located in this cell
     */
    fun contains(x: Int, z: Int): Boolean
}
