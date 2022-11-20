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

abstract class AbstractLine2<P : Comparable<P>> internal constructor() : Line2<P> {

  override fun toString(): String = ToStringHelper(this)
    .add("start", start)
    .add("end", end)
    .toString()

  override fun equals(other: Any?): Boolean {
    if (other == null || other::class != this::class)
      return false
    other as AbstractLine2<*>
    return other.start == start && other.end == end
  }

  override fun hashCode() = arrayOf<Any>(start, end).contentHashCode()
}
