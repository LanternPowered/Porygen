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
import io.github.jdiemke.triangulation.Triangle2D
import io.github.jdiemke.triangulation.Vector2D

object TriangleHelper {

    private fun testSign(x: Double, y: Double, b: Vector2D, c: Vector2D): Boolean {
        return (x - c.x) * (b.y - c.y) - (b.x - c.x) * (y - c.y) < 0
    }

    /**
     * Gets whether the given point [Vector2D] is located inside the triangle.
     *
     * @param point The point
     * @param triangle The triangle
     * @return Is inside
     */
    fun isInside(point: Vector2d, triangle: Triangle2D): Boolean {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        // Yeah, I know, not optimized, worries for later
        val b = testSign(point.x, point.y, triangle.a, triangle.b)
        return b == testSign(point.x, point.y, triangle.b, triangle.c) && b == testSign(point.x, point.y, triangle.c, triangle.a)
    }

    /**
     * Gets whether the given point [Vector2D] is located inside the triangle.
     *
     * @param point The point
     * @param triangle The triangle
     * @return Is inside
     */
    fun isInside(point: Vector2D, triangle: Triangle2D): Boolean {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        // Yeah, I know, not optimized, worries for later
        val b = testSign(point.x, point.y, triangle.a, triangle.b)
        return b == testSign(point.x, point.y, triangle.b, triangle.c) && b == testSign(point.x, point.y, triangle.c, triangle.a)
    }

    /**
     * Gets the incenter from the given [Triangle2D].
     *
     * @param triangle The triangle
     * @return The incenter
     */
    fun getIncenter(triangle: Triangle2D): Vector2d {
        // https://www.mathopenref.com/coordincenter.html
        val a = triangle.a
        val b = triangle.b
        val c = triangle.c

        var dx = b.x - c.x
        var dy = b.y - c.y
        val da = Math.sqrt(dx * dx + dy * dy)

        dx = c.x - a.x
        dy = c.y - a.y
        val db = Math.sqrt(dx * dx + dy * dy)

        dx = a.x - b.x
        dy = a.y - b.y
        val dc = Math.sqrt(dx * dx + dy * dy)

        val dp = da + db + dc
        val ox = (da * a.x + db * b.x + dc * c.x) / dp
        val oy = (da * a.y + db * b.y + dc * c.y) / dp

        return Vector2d(ox, oy)
    }

    /**
     * Gets the circumcenter from the given [Triangle2D].
     *
     * @param triangle The triangle
     * @return The circumcenter
     */
    fun getCircumcenter(triangle: Triangle2D): Vector2d {
        val a = triangle.a
        val b = triangle.b
        val c = triangle.c

        val abCenter = a.add(b).mult(0.5)
        val bcCenter = b.add(c).mult(0.5)

        val abSlope = -1.0 / ((b.y - a.y) / (b.x - a.x))
        val bcSlope = -1.0 / ((c.y - b.y) / (c.x - b.x))

        val bAB = abCenter.y - abSlope * abCenter.x
        val bBC = bcCenter.y - bcSlope * bcCenter.x

        val x = (bAB - bBC) / (bcSlope - abSlope)
        return Vector2d(x, abSlope * x + bAB)
    }

    /**
     * Gets the centroid from the given [Triangle2D].
     *
     * @param triangle The triangle
     * @return The centroid
     */
    fun getCentroid(triangle: Triangle2D): Vector2d {
        return Vector2d(
                (triangle.a.x + triangle.b.x + triangle.c.x) / 3.0,
                (triangle.a.y + triangle.b.y + triangle.c.y) / 3.0)
    }
}
