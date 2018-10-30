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
import org.lanternpowered.porygen.util.geom.Polygond
import org.lanternpowered.porygen.util.geom.Rectangled
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class VoronoiPolygonGenerator : CenteredPolygonGenerator {

    // The function to use to generate the center point of a triangle, the
    // circumcenter one is the one used for real voronoi diagrams
    private var triangleCenterProvider = VoronoiTriangleCenterProvider.Circumcenter

    override fun generate(context: GeneratorContext, rectangle: Rectangled, points: List<Vector2d>): List<CenteredPolygon> {
        val centeredPolygons = ArrayList<CenteredPolygon>()
        val pointSet = points.stream()
                .map { v -> Vector2D(v.x, v.y) }
                .toList()

        val delaunayTriangulator = DelaunayTriangulator(pointSet)
        delaunayTriangulator.triangulate()

        val triangles = delaunayTriangulator.triangles

        // Go through all the vertices, to find all the touching triangles,
        // for this triangles is each circumcenter a point of the polygon shape
        for (vertex in delaunayTriangulator.pointSet) {
            val polygonVertices = ArrayList<VertexEntry>()
            for (triangle in triangles) {
                if (triangle.hasVertex(vertex)) {
                    val point = this.triangleCenterProvider.function(triangle)
                    val angle = Math.atan2(point.x - vertex.x, point.y - vertex.y)
                    polygonVertices.add(VertexEntry(point, angle))
                }
            }
            if (polygonVertices.size <= 2) { // Not enough vertices
                continue
            }
            // Create a polygon from vertices that are sorted clockwise+
            val orderedVertices = polygonVertices.stream()
                    .sorted().map { e -> e.point }.collect(Collectors.toList())
            val polygon = if (this.triangleCenterProvider.alwaysConvexPolygons) {
                Polygond.newConvexPolygon(orderedVertices)
            } else {
                Polygond(orderedVertices)
            }
            centeredPolygons.add(CenteredPolygon(polygon.centroid, polygon))
        }

        return centeredPolygons
    }

    private class VertexEntry constructor(val point: Vector2d, private val angle: Double) : Comparable<VertexEntry> {

        override fun compareTo(other: VertexEntry): Int {
            return if (this.angle > other.angle) 1 else -1
        }
    }
}
