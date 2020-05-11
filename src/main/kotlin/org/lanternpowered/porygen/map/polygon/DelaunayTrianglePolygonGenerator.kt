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
import org.lanternpowered.porygen.points.PointsGenerator
import kotlin.random.Random

/**
 * Generates [CellPolygon]s that are delaunay triangles.
 */
class DelaunayTrianglePolygonGenerator(
    private val pointsGenerator: PointsGenerator
) : CellPolygonGenerator {

  override fun generate(random: Random): Collection<CellPolygon> {
    val points = this.pointsGenerator.generate(random)

    val delaunayTriangulator = DelaunayTriangulator(points)
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
