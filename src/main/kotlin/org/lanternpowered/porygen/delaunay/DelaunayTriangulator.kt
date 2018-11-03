/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Johannes Diemke
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
package org.lanternpowered.porygen.delaunay

import org.lanternpowered.porygen.math.geom.Triangle2d
import org.spongepowered.math.vector.Vector2d
import kotlin.math.max

/**
 * An implementation of an incremental 2D Delaunay triangulation algorithm.
 *
 * @property points The point set
 * @throws IllegalArgumentException Thrown when the point set contains less than three points
 */
class DelaunayTriangulator(val points: List<Vector2d>) {

  private val mutableTriangles = mutableListOf<Triangle2d>()

  /**
   * The triangles of the triangulation.
   */
  val triangles: List<Triangle2d>
    get() = mutableTriangles

  init {
    if (points.size < 3)
      throw IllegalArgumentException("Less than three points in point set.")
  }

  /**
   * This method generates a Delaunay triangulation from the specified point set.
   *
   */
  fun triangulate() {
    mutableTriangles.clear()

    // In order for the in circumcircle test to not consider the vertices of
    // the super triangle we have to start out with a big triangle
    // containing the whole point set. We have to scale the super triangle
    // to be very large. Otherwise the triangulation is not convex.
    var maxOfAnyCoordinate = 0.0
    for (vector in points)
      maxOfAnyCoordinate = max(max(vector.x, vector.y), maxOfAnyCoordinate)

    maxOfAnyCoordinate *= 16.0
    val p1 = Vector2d(0.0, 3.0 * maxOfAnyCoordinate)
    val p2 = Vector2d(3.0 * maxOfAnyCoordinate, 0.0)
    val p3 = Vector2d(-3.0 * maxOfAnyCoordinate, -3.0 * maxOfAnyCoordinate)
    val superTriangle = Triangle2d(p1, p2, p3)
    mutableTriangles.add(superTriangle)
    for (point in points) {
      val triangle = mutableTriangles.findContainingTriangle(point)
      if (triangle == null) {
        // If no containing triangle exists, then the vertex is not
        // inside a triangle (this can also happen due to numerical
        // errors) and lies on an edge. In order to find this edge we
        // search all edges of the triangle soup and select the one
        // which is nearest to the point we try to add. This edge is
        // removed and four new edges are added.
        val edge = mutableTriangles.findNearestEdge(point)
            ?: throw IllegalStateException()
        val first = mutableTriangles.findOneTriangleSharing(edge)
            ?: throw IllegalStateException()
        val second = mutableTriangles.findNeighbour(first, edge)
            ?: throw IllegalStateException()
        val firstNoneEdgeVertex = first.getNoneEdgeVertex(edge)
            ?: throw IllegalStateException()
        val secondNoneEdgeVertex = second.getNoneEdgeVertex(edge)
            ?: throw IllegalStateException()
        mutableTriangles.remove(first)
        mutableTriangles.remove(second)
        val triangle1 = Triangle2d(edge.a, firstNoneEdgeVertex, point)
        val triangle2 = Triangle2d(edge.b, firstNoneEdgeVertex, point)
        val triangle3 = Triangle2d(edge.a, secondNoneEdgeVertex, point)
        val triangle4 = Triangle2d(edge.b, secondNoneEdgeVertex, point)
        mutableTriangles.add(triangle1)
        mutableTriangles.add(triangle2)
        mutableTriangles.add(triangle3)
        mutableTriangles.add(triangle4)
        legalizeEdge(triangle1, Edge2d(edge.a, firstNoneEdgeVertex), point)
        legalizeEdge(triangle2, Edge2d(edge.b, firstNoneEdgeVertex), point)
        legalizeEdge(triangle3, Edge2d(edge.a, secondNoneEdgeVertex), point)
        legalizeEdge(triangle4, Edge2d(edge.b, secondNoneEdgeVertex), point)
      } else {
        // The vertex is inside a triangle.
        val a = triangle.a
        val b = triangle.b
        val c = triangle.c
        mutableTriangles.remove(triangle)
        val first = Triangle2d(a, b, point)
        val second = Triangle2d(b, c, point)
        val third = Triangle2d(c, a, point)
        mutableTriangles.add(first)
        mutableTriangles.add(second)
        mutableTriangles.add(third)
        legalizeEdge(first, Edge2d(a, b), point)
        legalizeEdge(second, Edge2d(b, c), point)
        legalizeEdge(third, Edge2d(c, a), point)
      }
    }
    // Remove all triangles that contain vertices of the super triangle.
    mutableTriangles.removeTrianglesUsing(superTriangle.a)
    mutableTriangles.removeTrianglesUsing(superTriangle.b)
    mutableTriangles.removeTrianglesUsing(superTriangle.c)
  }

  /**
   * This method legalizes edges by recursively flipping all illegal edges.
   *
   * @param triangle The triangle
   * @param edge The edge to be legalized
   * @param newVertex The new vertex
   */
  private fun legalizeEdge(triangle: Triangle2d, edge: Edge2d, newVertex: Vector2d) {
    val neighbourTriangle = mutableTriangles.findNeighbour(triangle, edge)
    // If the triangle has a neighbor, then legalize the edge
    if (neighbourTriangle != null) {
      if (neighbourTriangle.isPointInCircumcircle(newVertex)) {
        mutableTriangles.remove(triangle)
        mutableTriangles.remove(neighbourTriangle)
        val noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge)
            ?: throw IllegalStateException()
        val firstTriangle = Triangle2d(noneEdgeVertex, edge.a, newVertex)
        val secondTriangle = Triangle2d(noneEdgeVertex, edge.b, newVertex)
        mutableTriangles.add(firstTriangle)
        mutableTriangles.add(secondTriangle)
        legalizeEdge(firstTriangle, Edge2d(noneEdgeVertex, edge.a), newVertex)
        legalizeEdge(secondTriangle, Edge2d(noneEdgeVertex, edge.b), newVertex)
      }
    }
  }
}


/**
 * Returns the triangle from this triangle soup that contains the specified
 * point or null if no triangle from the triangle soup contains the point.
 *
 * @param point The point
 * @return Returns the triangle from this triangle soup that contains the
 *         specified point or null
 */
private fun List<Triangle2d>.findContainingTriangle(point: Vector2d): Triangle2d? =
    firstOrNull { it.contains(point) }

/**
 * Returns the neighbor triangle of the specified triangle sharing the same
 * edge as specified. If no neighbor sharing the same edge exists null is
 * returned.
 *
 * @param triangle The triangle
 * @param edge The edge
 * @return The triangles neighbor triangle sharing the same edge or null if
 *         no triangle exists
 */
private fun List<Triangle2d>.findNeighbour(triangle: Triangle2d, edge: Edge2d): Triangle2d? =
    firstOrNull { it.isNeighbour(edge) && it != triangle }

/**
 * Returns one of the possible triangles sharing the specified edge. Based
 * on the ordering of the triangles in this triangle soup the returned
 * triangle may differ. To find the other triangle that shares this edge use
 * the [findNeighbour] method.
 *
 * @param edge The edge
 * @return Returns one triangle that shares the specified edge
 */
private fun List<Triangle2d>.findOneTriangleSharing(edge: Edge2d): Triangle2d? =
    firstOrNull { it.isNeighbour(edge) }

/**
 * Returns the edge from the triangle soup nearest to the specified point.
 *
 * @param point The point
 * @return The edge from the triangle soup nearest to the specified point
 */
private fun List<Triangle2d>.findNearestEdge(point: Vector2d): Edge2d? =
    asSequence().map { it.findNearestEdge(point) }.sorted().first().edge

/**
 * Removes all triangles from this triangle soup that contain the specified
 * vertex.
 *
 * @param vertex The vertex
 */
private fun MutableList<Triangle2d>.removeTrianglesUsing(vertex: Vector2d): Boolean =
    removeIf { it.hasVertex(vertex) }