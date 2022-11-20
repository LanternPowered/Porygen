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

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.lanternpowered.porygen.math.vector.Vec2i

@Serializable
class Line2i private constructor(
  override val start: Vec2i,
  override val end: Vec2i,
  @Transient private val ignored: Unit = Unit,
) : AbstractLine2<Vec2i>() {

  override val center: Vec2i by lazy {
    val dStart = start.toDouble()
    val dEnd = end.toDouble()
    start + ((dEnd - dStart) / 2.0).floorToInt()
  }

  constructor(startX: Int, startY: Int, endX: Int, endY: Int) :
    this(Vec2i(startX, startY), Vec2i(endX, endY))

  override fun intersects(startX: Double, startY: Double, endX: Double, endY: Double) =
    Line2d.linesIntersect(
      p1X = start.x.toDouble(),
      p1Y = start.y.toDouble(),
      q1X = end.x.toDouble(),
      q1Y = end.y.toDouble(),
      p2X = startX,
      p2Y = startY,
      q2X = endX,
      q2Y = endY,
    )

  override fun toInt() = this
  override fun toDouble() = Line2d(start.toDouble(), end.toDouble())

  fun translate(offset: Vec2i): Line2i =
    Line2i(start + offset, end + offset)

  companion object {

    operator fun invoke(
      start: Vec2i,
      end: Vec2i,
    ): Line2i {
      val realStart: Vec2i
      val realEnd: Vec2i
      if (start < end) {
        realStart = start
        realEnd = end
      } else {
        realStart = end
        realEnd = start
      }
      return Line2i(realStart, realEnd, Unit)
    }

    operator fun invoke(
      startX: Int,
      startY: Int,
      endX: Int,
      endY: Int,
    ): Line2i = Line2i(Vec2i(startX, startY), Vec2i(endX, endY))

  }
}
