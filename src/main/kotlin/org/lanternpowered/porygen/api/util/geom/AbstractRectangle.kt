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

import com.google.common.base.MoreObjects

import java.util.Objects

abstract class AbstractRectangle<T : Comparable<T>> internal constructor(
        override val min: T,
        override val max: T
) : AbstractShape(), Rectangle<T> {

    override fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean {
        var inside = 0
        // If more than 0, but less than 3 points
        // are inside, then there is an intersection
        if (contains(minX, minY)) {
            inside++
        }
        if (contains(maxX, minY)) {
            inside++
        }
        if (contains(maxX, maxY) && ++inside == 3) {
            return false
        }
        return if (contains(minX, maxY) && ++inside == 3) {
            false
        } else inside > 0
    }

    override fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double) = contains(minX, minY) && contains(maxX, maxY)
    override fun contains(minX: Int, minY: Int, maxX: Int, maxY: Int) = contains(minX, minY) && contains(maxX, maxY)

    override fun intersects(polygon: Polygond) = polygon.intersects(this)

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("min", this.min)
                .add("max", this.max)
                .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != javaClass) {
            return false
        }
        other as AbstractRectangle<*>
        return other.min == this.min && other.max == this.max
    }

    override fun hashCode(): Int = Objects.hash(this.min, this.max)
}
