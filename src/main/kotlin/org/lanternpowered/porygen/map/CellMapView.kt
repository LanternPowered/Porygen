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
package org.lanternpowered.porygen.map

/**
 * Represents a piece of a voronoi [CellMap]. It allows access to all the
 * [Cell]s, etc. that are visible in this [CellMapView].
 */
interface CellMapView : CellMapPart {

    /**
     * Gets the parent [CellMap] of this map view.
     *
     * @return The parent
     */
    val parent: CellMap

    /**
     * All the [Cell]s that are visible in this [CellMapView].
     *
     * @return The cells
     */
    val cells: Collection<Cell>

    /**
     * All the [Corner]s that are visible in this [CellMapView].
     *
     * @return The corners
     */
    val corners: Collection<Corner>

    /**
     * All the [Edge]s that are visible in this [CellMapView].
     *
     * @return The edges
     */
    val edges: Collection<Edge>
}
