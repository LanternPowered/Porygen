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
import org.lanternpowered.porygen.math.vector.Vec2d
import kotlin.math.max
import kotlin.math.min

@Serializable
class Line2d private constructor(
  override val start: Vec2d,
  override val end: Vec2d,
  @Transient private val ignored: Unit = Unit,
) : AbstractLine2<Vec2d>() {

  override val center: Vec2d by lazy { start + ((end - start) / 2.0) }

  override fun intersects(startX: Double, startY: Double, endX: Double, endY: Double) =
    linesIntersect(start.x, start.y, end.x, end.y, startX, startY, endX, endY)

  override fun toInt() = Line2i(start.toInt(), end.toInt())
  override fun toDouble() = this

  companion object {

    operator fun invoke(
      start: Vec2d,
      end: Vec2d,
    ): Line2d {
      val realStart: Vec2d
      val realEnd: Vec2d
      if (start < end) {
        realStart = start
        realEnd = end
      } else {
        realStart = end
        realEnd = start
      }
      return Line2d(realStart, realEnd, Unit)
    }

    operator fun invoke(
      startX: Double,
      startY: Double,
      endX: Double,
      endY: Double,
    ): Line2d = Line2d(Vec2d(startX, startY), Vec2d(endX, endY))

    // https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/

    /**
     * Checks whether the two lines intersect.
     */
    fun linesIntersect(
      p1X: Double, p1Y: Double, q1X: Double, q1Y: Double,
      p2X: Double, p2Y: Double, q2X: Double, q2Y: Double
    ): Boolean {

      // Find the four orientations needed for general and
      // special cases
      val o1 = orientation(p1X, p1Y, q1X, q1Y, p2X, p2Y)
      val o2 = orientation(p1X, p1Y, q1X, q1Y, q2X, q2Y)
      val o3 = orientation(p2X, p2Y, q2X, q2Y, p1X, p1Y)
      val o4 = orientation(p2X, p2Y, q2X, q2Y, q1X, q1Y)

      // General case
      if (o1 != o2 && o3 != o4)
        return true

      // Special Cases
      // p1, q1 and p2 are colinear and p2 lies on segment p1q1
      if (o1 == 0 && onSegment(p1X, p1Y, p2X, p2Y, q1X, q1Y))
        return true

      // p1, q1 and q2 are colinear and q2 lies on segment p1q1
      if (o2 == 0 && onSegment(p1X, p1Y, q2X, q2Y, q1X, q1Y))
        return true

      // p2, q2 and p1 are colinear and p1 lies on segment p2q2
      if (o3 == 0 && onSegment(p2X, p2Y, p1X, p1Y, q2X, q2Y))
        return true

      // p2, q2 and q1 are colinear and q1 lies on segment p2q2
      if (o4 == 0 && onSegment(p2X, p2Y, q1X, q1Y, q2X, q2Y))
        return true

      // Doesn't fall in any of the above cases
      return false
    }

    /**
     * Finds the orientation of the ordered (p, q, r)
     *
     * 0 -> p, q and r are colinear
     * 1 -> Clockwise
     * 2 -> Counterclockwise
     */
    private fun orientation(
      pX: Double, pY: Double,
      qX: Double, qY: Double,
      rX: Double, rY: Double,
    ): Int {
      // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
      // for details of below formula.
      val v = (qY - pY) * (rX - qX) - (qX - pX) * (rY - qY)
      return when {
        v == 0.0 -> 0 // colinear
        v > 0 -> 1 // CW
        else -> 2 // CCW
      }
    }

    /**
     * Given the three colinear points (p, q, r), the function checks if
     * point q lies on line segment 'pr'
     */
    private fun onSegment(
      pX: Double, pY: Double,
      qX: Double, qY: Double,
      rX: Double, rY: Double
    ): Boolean = qX <= max(pX, rX) && qX >= min(pX, rX) && qY <= max(pY, rY) && qY >= min(pY, rY)
  }
}
