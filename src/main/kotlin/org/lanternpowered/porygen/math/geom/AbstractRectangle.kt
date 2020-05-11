/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.math.geom

import org.lanternpowered.porygen.util.ToStringHelper

abstract class AbstractRectangle<T : Comparable<T>> internal constructor(
    override val min: T,
    override val max: T
) : AbstractShape(), Rectangle<T> {

  override fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean {
    var inside = 0
    // If more than 0, but less than 3 points
    // are inside, then there is an intersection
    if (contains(minX, minY))
      inside++
    if (contains(maxX, minY))
      inside++
    if (contains(maxX, maxY) && ++inside == 3)
      return false
    return if (contains(minX, maxY) && ++inside == 3) false else inside > 0
  }

  override fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double) =
      contains(minX, minY) && contains(maxX, maxY)

  override fun intersects(polygon: Polygond) = polygon.intersects(this)

  override fun toString(): String = ToStringHelper(this)
      .add("min", min)
      .add("max", max)
      .toString()

  override fun equals(other: Any?): Boolean {
    if (other == null || other.javaClass != javaClass)
      return false
    other as AbstractRectangle<*>
    return other.min == min && other.max == max
  }

  override fun hashCode(): Int = arrayOf<Any>(min, max).contentHashCode()
}
