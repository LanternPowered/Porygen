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

import org.lanternpowered.porygen.math.geom.Triangled
import org.lanternpowered.porygen.math.vector.Vec2d

/**
 * Represents a provider for the center point of a [Triangled].
 *
 * @property function The function which calculates the center point
 * @property alwaysConvexPolygons Whether the usage of the center point always results in convex
 *   polygons. This reduces the amount of calculations that need to be
 *   done if this is true.
 */
class TriangleCenterProvider(
  val function: (Triangled) -> Vec2d,
  val alwaysConvexPolygons: Boolean = false,
) {

  companion object {

    /**
     * A provider which uses the circumcenter of the triangle.
     */
    val Circumcenter = TriangleCenterProvider(Triangled::circumcenter, alwaysConvexPolygons = true)

    /**
     * A provider which uses the centroid of the triangle.
     */
    val Centroid = TriangleCenterProvider(Triangled::centroid)

    /**
     * A provider which uses the incenter of the triangle.
     */
    val Incenter = TriangleCenterProvider(Triangled::incenter)

    /**
     * Gets a provider which interpolates between the two given [TriangleCenterProvider]s.
     */
    fun lerp(
      from: TriangleCenterProvider,
      to: TriangleCenterProvider,
      fraction: Double,
    ): TriangleCenterProvider {
      check(fraction in 0.0..1.0)
      if (from === to || fraction == 0.0)
        return from
      if (fraction == 1.0)
        return to
      val function = { triangle: Triangled ->
        val fromPoint = from.function(triangle)
        val toPoint = from.function(triangle)
        fromPoint + ((toPoint - fromPoint) * fraction)
      }
      return TriangleCenterProvider(function,
        from.alwaysConvexPolygons && to.alwaysConvexPolygons)
    }
  }
}
