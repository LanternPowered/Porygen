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

import org.lanternpowered.porygen.math.vector.Vec2i

class Line2i(start: Vec2i, end: Vec2i) : AbstractLine2<Vec2i>(start, end) {

  override val center: Vec2i by lazy {
    val dStart = start.toDouble()
    val dEnd = end.toDouble()
    start + ((dEnd - dStart) / 2.0).floorToInt()
  }

  constructor(startX: Int, startY: Int, endX: Int, endY: Int) : this(Vec2i(startX, startY), Vec2i(endX, endY))

  override fun intersects(startX: Double, startY: Double, endX: Double, endY: Double) = Line2d.linesIntersect(
      start.x.toDouble(), start.y.toDouble(), end.x.toDouble(), end.y.toDouble(), startX, startY, endX, endY)

  override fun toInt() = this
  override fun toDouble() = Line2d(start.toDouble(), end.toDouble())

  fun translate(offset: Vec2i): Line2i =
      Line2i(start + offset, end + offset)
}
