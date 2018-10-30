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
package org.lanternpowered.porygen.util.geom

import com.flowpowered.math.vector.Vector2i

import java.awt.geom.Line2D

class Line2i(start: Vector2i, end: Vector2i) : AbstractLine2<Vector2i>(start, end) {

    constructor(startX: Int, startY: Int, endX: Int, endY: Int) : this(Vector2i(startX, startY), Vector2i(endX, endY))

    override fun intersects(startX: Double, startY: Double, endX: Double, endY: Double) = Line2D.linesIntersect(
            this.start.x.toDouble(), this.start.y.toDouble(), this.end.x.toDouble(), this.end.y.toDouble(), startX, startY, endX, endY)

    override fun toInt() = this
    override fun toDouble() = Line2d(this.start.toDouble(), this.end.toDouble())
}
