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

import org.lanternpowered.porygen.api.data.SimpleDataHolder
import org.lanternpowered.porygen.api.map.*
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.spongepowered.api.world.World

class PorygenMapView(
        override val map: CellMap,
        override val viewRectangle: Rectanglei,
        override val cells: Collection<Cell>,
        override val corners: Collection<Corner>,
        override val edges: Collection<Edge>
) : SimpleDataHolder(), CellMapView {

    override val world: World get() = this.map.world

    override fun getCorner(id: Long) = this.map.getCorner(id)
    override fun getCell(id: Long) = this.map.getCell(id)
    override fun getEdge(id: Long) = this.map.getEdge(id)

    override fun getSubView(rectangle: Rectanglei): CellMapView {
        check(this.viewRectangle.contains(rectangle)) { "The target rectangle $rectangle must be inside this map view rectangle $viewRectangle" }
        return this.map.getSubView(rectangle)
    }

    override fun getCell(x: Int, z: Int): Cell {
        check(this.viewRectangle.contains(x, z)) { "The target block coordinates ($x, $z) must be inside this map view rectangle $viewRectangle" }
        return this.map.getCell(x, z)
    }

}
