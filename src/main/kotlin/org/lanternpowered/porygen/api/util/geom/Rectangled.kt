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

import com.flowpowered.math.GenericMath
import com.flowpowered.math.vector.Vector2d
import com.google.common.base.MoreObjects
import com.google.common.base.Objects

import java.awt.Polygon

class Rectangled : AbstractRectangle<Vector2d> {

    constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) :
            super(Vector2d(Math.min(minX, maxX), Math.min(minY, maxY)), Vector2d(Math.max(minX, maxX), Math.max(minY, maxY)))

    constructor(min: Vector2d, max: Vector2d) :
            super(Vector2d(Math.min(min.x, max.x), Math.min(min.y, max.y)), Vector2d(Math.max(min.x, max.x), Math.max(min.y, max.y)))

    override fun contains(x: Double, y: Double): Boolean {
        return x <= this.max.x && x >= this.min.x &&
                y <= this.max.y && y >= this.min.y
    }

    override fun toInt(): Rectanglei {
        val min = this.min
        val max = this.max
        return Rectanglei(
                GenericMath.floor(min.x),
                GenericMath.floor(min.y),
                GenericMath.floor(max.x),
                GenericMath.floor(max.y))
    }

    override fun toDouble(): Rectangled = this

    /**
     * Converts this [Rectangled] into a [Polygond].
     *
     * @return The polygon
     */
    fun toPolygon(): Polygond {
        return Polygond.newConvexPolygon(
                Vector2d(this.min.x, this.min.y),
                Vector2d(this.max.x, this.min.y),
                Vector2d(this.max.x, this.max.y),
                Vector2d(this.min.x, this.max.y))
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
        pointsX[0] = GenericMath.floor(this.min.x)
        pointsY[0] = GenericMath.floor(this.min.y)
        pointsX[1] = GenericMath.floor(this.max.x)
        pointsY[1] = GenericMath.floor(this.min.y)
        pointsX[2] = GenericMath.floor(this.max.x)
        pointsY[2] = GenericMath.floor(this.max.y)
        pointsX[3] = GenericMath.floor(this.min.x)
        pointsY[3] = GenericMath.floor(this.max.y)
        return Polygon(pointsX, pointsY, 4)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("min", this.min)
                .add("max", this.max)
                .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Rectangled) {
            return false
        }
        return other.min == this.min && other.max == this.max
    }

    override fun hashCode(): Int = Objects.hashCode(this.min, this.max)
}
