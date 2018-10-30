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
package org.lanternpowered.porygen.map.impl

import org.lanternpowered.porygen.map.*
import org.lanternpowered.porygen.util.geom.Rectangled
import org.lanternpowered.porygen.util.geom.Rectanglei

import java.util.Collections

class PorygenMapView : CellMapView {
    override val corners: Collection<Corner>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val edges: Collection<Edge>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val cells: List<Cell>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    // The current size of the view
    private val view: Rectangled = TODO("not implemented")

    // All the cells
    private val cells_ = ArrayList<Cell>()

    override val parent: CellMap
        get() = TODO("not implemented")

    override fun getSubView(rectangle: Rectangled): CellMapView {
        TODO("not implemented")
    }

    override fun getSubView(rectangle: Rectanglei): CellMapView {
        TODO("not implemented")
    }

    override fun getCell(x: Double, y: Double): Cell {
        TODO("not implemented")
    }
}
