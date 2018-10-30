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
package org.lanternpowered.porygen.map.gen

import com.flowpowered.math.vector.Vector2d
import io.github.jdiemke.triangulation.Triangle2D
import org.lanternpowered.porygen.util.geom.TriangleHelper

/**
 * Represents a provider for the center point of a [Triangle2D].
 *
 * @property function The function which calculates the center point
 * @property alwaysConvexPolygons Whether the usage of the center point always results in convex
 *                                polygons. This reduces the amount of calculations that need to be
 *                                done if this is true.
 */
class VoronoiTriangleCenterProvider(
        val function: (Triangle2D) -> Vector2d,
        val alwaysConvexPolygons: Boolean = false
) {
    companion object {

        val Circumcenter = VoronoiTriangleCenterProvider(TriangleHelper::getCircumcenter, alwaysConvexPolygons = true)
        val Centroid = VoronoiTriangleCenterProvider(TriangleHelper::getCentroid)
        val Incenter = VoronoiTriangleCenterProvider(TriangleHelper::getIncenter)
    }
}
