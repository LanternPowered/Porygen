/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.gen.polygon

import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.delaunay.DelaunayTriangulator
import org.lanternpowered.porygen.math.geom.Polygond
import org.lanternpowered.porygen.math.geom.Rectangled
import org.spongepowered.math.vector.Vector2d
import kotlin.math.atan2

class VoronoiPolygonGenerator : AbstractCellPolygonGenerator() {

  // The function to use to generate the center point of a triangle, the
  // circumcenter one is the one used for real voronoi diagrams
  var triangleCenterProvider = VoronoiTriangleCenterProvider.Circumcenter

  override fun generate(context: GeneratorContext, rectangle: Rectangled, points: Collection<Vector2d>): Collection<CellPolygon> {
    val centeredPolygons = mutableListOf<CellPolygon>()

    val delaunayTriangulator = DelaunayTriangulator(points.toList())
    delaunayTriangulator.triangulate()

    val triangles = delaunayTriangulator.triangles

    // Go through all the vertices, to find all the touching triangles,
    // for this triangles is each circumcenter a point of the polygon shape
    for (vertex in delaunayTriangulator.points) {
      val polygonVertices = ArrayList<VertexEntry>()
      for (triangle in triangles) {
        if (triangle.hasVertex(vertex)) {
          val point = this.triangleCenterProvider.function(triangle)
          val angle = atan2(point.x - vertex.x, point.y - vertex.y)
          polygonVertices.add(VertexEntry(point, angle))
        }
      }
      if (polygonVertices.size <= 2) // Not enough vertices
        continue
      // Create a polygon from vertices that are sorted clockwise+
      val sortedVertices = polygonVertices
          .asSequence().sorted().map { it.point }.toList()
      val polygon = if (this.triangleCenterProvider.alwaysConvexPolygons) {
        Polygond.newConvexPolygon(sortedVertices)
      } else {
        Polygond(sortedVertices)
      }
      centeredPolygons.add(CellPolygon(polygon.centroid, polygon))
    }

    return centeredPolygons
  }

  private class VertexEntry(val point: Vector2d, private val angle: Double) : Comparable<VertexEntry> {

    override fun compareTo(other: VertexEntry): Int =
        if (this.angle > other.angle) 1 else -1
  }
}
