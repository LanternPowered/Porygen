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
package org.lanternpowered.porygen.api.util.geom

import com.flowpowered.math.vector.Vector2i

import java.awt.Polygon

/**
 * Represents a rectangle.
 */
class Rectanglei : AbstractRectangle<Vector2i> {

    constructor(minX: Int, minY: Int, maxX: Int, maxY: Int) :
            super(Vector2i(Math.min(minX, maxX), Math.min(minY, maxY)), Vector2i(Math.max(minX, maxX), Math.max(minY, maxY)))

    constructor(min: Vector2i, max: Vector2i) :
            super(Vector2i(Math.min(min.x, max.x), Math.min(min.y, max.y)), Vector2i(Math.max(min.x, max.x), Math.max(min.y, max.y)))

    override fun contains(x: Double, y: Double): Boolean {
        return x <= this.max.x && x >= this.min.x &&
                y <= this.max.y && y >= this.min.y
    }

    override fun contains(x: Int, y: Int): Boolean {
        return x <= this.max.x && x >= this.min.x &&
                y <= this.max.y && y >= this.min.y
    }

    override fun toInt() = this

    override fun toDouble(): Rectangled {
        val min = this.min
        val max = this.max
        return Rectangled(min.x.toDouble(), min.y.toDouble(), max.x.toDouble(), max.y.toDouble())
    }

    /**
     * Converts this [Rectangled] into
     * a drawable [Polygon].
     *
     * @return The drawable polygon
     */
    fun toDrawable(): Polygon {
        val pointsX = IntArray(4)
        val pointsY = IntArray(4)
        pointsX[0] = this.min.x
        pointsY[0] = this.min.y
        pointsX[1] = this.max.x
        pointsY[1] = this.min.y
        pointsX[2] = this.max.x
        pointsY[2] = this.max.y
        pointsX[3] = this.min.x
        pointsY[3] = this.max.y
        return Polygon(pointsX, pointsY, 4)
    }
}
