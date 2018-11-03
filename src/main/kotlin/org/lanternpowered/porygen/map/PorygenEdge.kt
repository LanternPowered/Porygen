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
import org.lanternpowered.porygen.api.map.Cell
import org.lanternpowered.porygen.api.map.Corner
import org.lanternpowered.porygen.api.map.Edge
import org.lanternpowered.porygen.api.util.geom.Line2i
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import java.util.*

class PorygenEdge(
        override val line: Line2i,
        override val map: PorygenMap
) : Edge, SimpleDataHolder() {

    override val id = packIntPair(this.line.end.x - this.line.start.x, this.line.end.y - this.line.start.y)

    internal val theCells = HashSet<PorygenCell>()
    internal val theCorners = HashSet<PorygenCorner>()

    override val cells: Collection<Cell> = Collections.unmodifiableCollection(this.theCells)
    override val corners: Collection<Corner> = Collections.unmodifiableCollection(this.theCorners)
}
