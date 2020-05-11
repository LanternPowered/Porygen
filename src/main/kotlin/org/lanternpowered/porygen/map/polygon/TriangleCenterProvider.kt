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

import org.lanternpowered.porygen.math.geom.Triangle2d
import org.spongepowered.math.vector.Vector2d

/**
 * Represents a provider for the center point of a [Triangle2d].
 *
 * @property function The function which calculates the center point
 * @property alwaysConvexPolygons Whether the usage of the center point always results in convex
 *   polygons. This reduces the amount of calculations that need to be
 *   done if this is true.
 */
class TriangleCenterProvider(
    val function: (Triangle2d) -> Vector2d,
    val alwaysConvexPolygons: Boolean = false
) {

  companion object {
    val Circumcenter = TriangleCenterProvider(Triangle2d::circumcenter, alwaysConvexPolygons = true)
    val Centroid = TriangleCenterProvider(Triangle2d::centroid)
    val Incenter = TriangleCenterProvider(Triangle2d::incenter)
  }
}
