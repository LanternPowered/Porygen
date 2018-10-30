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

import com.flowpowered.math.vector.Vector2d
import com.flowpowered.math.vector.Vector2i

interface Shape {

    /**
     * Gets whether the given [Shape]
     * is located inside this [Shape].
     *
     * @param shape The shape
     * @return Whether the shape is located inside this 2d shape
     */
    operator fun contains(shape: Shape): Boolean {
        return when (shape) {
            is Rectangled -> contains(shape)
            is Rectanglei -> contains(shape)
            is Polygond -> contains(shape)
            else -> throw IllegalStateException()
        }
    }

    /**
     * Gets whether the given [Rectanglei]
     * is located inside this [Shape].
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is located inside this 2d shape
     */
    operator fun contains(rectangle: Rectanglei): Boolean {
        val min = rectangle.min
        val max = rectangle.max
        return contains(min.x, min.y, max.x, max.y)
    }

    /**
     * Gets whether the given [Rectangled]
     * is located inside this [Shape].
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is located inside this 2d shape
     */
    operator fun contains(rectangle: Rectangled): Boolean {
        val min = rectangle.min
        val max = rectangle.max
        return contains(min.x, min.y, max.x, max.y)
    }

    /**
     * Gets whether the given rectangle coordinates
     * are located in this [Shape].
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is located inside this 2d shape
     */
    fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean

    /**
     * Gets whether the given rectangle coordinates
     * are located in this [Shape].
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is located inside this 2d shape
     */
    fun contains(minX: Int, minY: Int, maxX: Int, maxY: Int): Boolean {
        return contains(minX.toDouble(), minY.toDouble(), maxX.toDouble(), maxY.toDouble())
    }

    /**
     * Gets whether the given [Polygond]
     * is located inside this [Shape].
     *
     * @param polygon The polygon
     * @return Whether the polygon is located inside this 2d shape
     */
    operator fun contains(polygon: Polygond): Boolean

    /**
     * Gets whether the given [Shape]
     * intersects with this [Shape].
     *
     * @param shape The shape
     * @return Whether the shape is intersecting with this shape
     */
    fun intersects(shape: Shape): Boolean {
        return when (shape) {
            is Rectangled -> intersects(shape)
            is Rectanglei -> intersects(shape)
            is Polygond -> intersects(shape)
            else -> throw IllegalStateException()
        }
    }

    /**
     * Gets whether the given [Rectanglei]
     * intersects with this [Shape].
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is intersecting with this shape
     */
    fun intersects(rectangle: Rectanglei): Boolean {
        val min = rectangle.min
        val max = rectangle.max
        return intersects(min.x, min.y, max.x, max.y)
    }

    /**
     * Gets whether the given [Rectangled]
     * intersects with this [Shape].
     *
     * @param rectangle The rectangle
     * @return Whether the rectangle is intersecting with this shape
     */
    fun intersects(rectangle: Rectangled): Boolean {
        val min = rectangle.min
        val max = rectangle.max
        return intersects(min.x, min.y, max.x, max.y)
    }

    /**
     * Gets whether the given rectangle coordinates
     * intersect with this [Shape].
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is intersecting with this shape
     */
    fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean

    /**
     * Gets whether the given rectangle coordinates
     * intersects with this [Shape].
     *
     * @param minX The minimum x value
     * @param minY The minimum y value
     * @param maxX The maximum x value
     * @param maxY The maximum y value
     * @return Whether the rectangle is intersecting with this shape
     */
    fun intersects(minX: Int, minY: Int, maxX: Int, maxY: Int): Boolean {
        return intersects(minX.toDouble(), minY.toDouble(), maxX.toDouble(), maxY.toDouble())
    }

    /**
     * Gets whether the given [Polygond]
     * intersects with this [Shape].
     *
     * @param polygon The polygon
     * @return Whether the polygon is intersecting with this shape
     */
    fun intersects(polygon: Polygond): Boolean

    /**
     * Gets whether the given point [Vector2d]
     * are located within this [Shape].
     *
     * @param point The point
     * @return Whether this 2d shape contains the point
     */
    operator fun contains(point: Vector2i): Boolean {
        return contains(point.x, point.y)
    }

    /**
     * Gets whether the given point [Vector2d]
     * are located within this [Shape].
     *
     * @param point The point
     * @return Whether this 2d shape contains the point
     */
    operator fun contains(point: Vector2d): Boolean {
        return contains(point.x, point.y)
    }

    /**
     * Gets whether the given point coordinates
     * are located within this [Shape].
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this 2d shape contains the point
     */
    fun contains(x: Double, y: Double): Boolean

    /**
     * Gets whether the given point coordinates
     * are located within this [Shape].
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this 2d shape contains the point
     */
    fun contains(x: Int, y: Int): Boolean {
        return contains(x.toDouble(), y.toDouble())
    }
}
