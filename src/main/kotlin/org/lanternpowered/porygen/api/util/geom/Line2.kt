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

import com.flowpowered.math.vector.Vector2d
import com.flowpowered.math.vector.Vector2i

interface Line2<P> {

    /**
     * Gets the start point of this [Line2].
     *
     * @return The start point
     */
    val start: P

    /**
     * Gets the end point of this [Line2].
     *
     * @return The end point
     */
    val end: P

    /**
     * Gets whether this [Line2] intersects with
     * the given [Line2d].
     *
     * @param line The line
     * @return Whether the lines intersect
     */
    fun intersects(line: Line2d): Boolean {
        return intersects(line.start, line.end)
    }

    /**
     * Gets whether this [Line2] intersects with
     * the given coordinate based line.
     *
     * @param start The start point
     * @param end The end point
     * @return Whether the lines intersect
     */
    fun intersects(start: Vector2i, end: Vector2i): Boolean {
        return intersects(start.x, start.y, end.x, end.y)
    }

    /**
     * Gets whether this [Line2] intersects with
     * the given coordinate based line.
     *
     * @param start The start point
     * @param end The end point
     * @return Whether the lines intersect
     */
    fun intersects(start: Vector2d, end: Vector2d): Boolean {
        return intersects(start.x, start.y, end.x, end.y)
    }

    /**
     * Gets whether this [Line2] intersects with
     * the given coordinate based line.
     *
     * @param startX The start x coordinate
     * @param startY The start y coordinate
     * @param endX The end x coordinate
     * @param endY The end y coordinate
     * @return Whether the lines intersect
     */
    fun intersects(startX: Double, startY: Double, endX: Double, endY: Double): Boolean

    /**
     * Gets whether this [Line2] intersects with
     * the given coordinate based line.
     *
     * @param startX The start x coordinate
     * @param startY The start y coordinate
     * @param endX The end x coordinate
     * @param endY The end y coordinate
     * @return Whether the lines intersect
     */
    fun intersects(startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        return intersects(startX.toDouble(), startY.toDouble(), endX.toDouble(), endY.toDouble())
    }

    /**
     * Converts this [Line2]
     * into a [Line2i].
     *
     * @return The int line
     */
    fun toInt(): Line2i

    /**
     * Converts this [Line2]
     * into a [Line2d].
     *
     * @return The double line
     */
    fun toDouble(): Line2d
}
