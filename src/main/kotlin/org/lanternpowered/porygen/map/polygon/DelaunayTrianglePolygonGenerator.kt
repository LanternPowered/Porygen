/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.polygon

import org.lanternpowered.porygen.delaunay.DelaunayTriangulator
import org.lanternpowered.porygen.math.geom.Polygond
import org.spongepowered.math.vector.Vector2d

/**
 * Generates [CellPolygon]s that are delaunay triangles.
 */
object DelaunayTrianglePolygonGenerator : CellPolygonGenerator {

  override fun generate(points: Collection<Vector2d>): Collection<CellPolygon> {
    val delaunayTriangulator = DelaunayTriangulator(points.toList())
    delaunayTriangulator.triangulate()

    return delaunayTriangulator.triangles
        .map { triangle ->
          // Use the centroid as center point to make sure
          // that it falls inside the constructed triangle
          val center = triangle.centroid
          val polygon = Polygond.newConvexPolygon(
              triangle.a, triangle.b, triangle.c)
          CellPolygon(center, polygon)
        }
  }
}
