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

import org.lanternpowered.porygen.math.geom.Polygond
import org.lanternpowered.porygen.points.PointsGenerator
import org.spongepowered.math.vector.Vector2d

/**
 * Represents a [Polygond] with its center point [Vector2d].
 *
 * @property center The center of the polygon. This should be a
 *   point that was generated by a [PointsGenerator].
 * @property polygon The polygon
 */
data class CellPolygon(
    val center: Vector2d,
    val polygon: Polygond
) {

  fun mul(scale: Vector2d): CellPolygon {
    val center = this.center.mul(scale)
    val vertices = this.polygon.vertices
        .map { it.mul(scale) }
    val polygon = Polygond(vertices) // TODO: Persist convex state
    return CellPolygon(center, polygon)
  }
}
