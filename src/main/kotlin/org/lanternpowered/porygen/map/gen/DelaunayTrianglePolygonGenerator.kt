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
import io.github.jdiemke.triangulation.DelaunayTriangulator
import io.github.jdiemke.triangulation.Vector2D
import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.util.geom.Polygond
import org.lanternpowered.porygen.util.geom.Rectangled
import org.lanternpowered.porygen.util.geom.TriangleHelper
import java.util.*
import java.util.stream.Collectors

/**
 * Generates [Cell]s that are delaunay triangles.
 */
class DelaunayTrianglePolygonGenerator : CenteredPolygonGenerator {

    override fun generate(context: GeneratorContext, rectangle: Rectangled, points: List<Vector2d>): List<CenteredPolygon> {
        val centeredPolygons = ArrayList<CenteredPolygon>()
        val pointSet = points.stream()
                .map { v -> Vector2D(v.x, v.y) }
                .collect(Collectors.toList())

        val delaunayTriangulator = DelaunayTriangulator(pointSet)
        delaunayTriangulator.triangulate()

        val triangles = delaunayTriangulator.triangles
        for (triangle in triangles) {
            // Use the centroid as center point to make sure
            // that it falls inside the constructed triangle
            val center = TriangleHelper.getCentroid(triangle)
            val polygon = Polygond.newConvexPolygon(
                    Vector2d(triangle.a.x, triangle.a.y),
                    Vector2d(triangle.b.x, triangle.b.y),
                    Vector2d(triangle.c.x, triangle.c.y))
            centeredPolygons.add(CenteredPolygon(center, polygon))
        }

        return centeredPolygons
    }
}
