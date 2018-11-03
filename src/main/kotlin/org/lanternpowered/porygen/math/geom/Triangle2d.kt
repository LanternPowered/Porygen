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

import org.lanternpowered.porygen.delaunay.Edge2d
import org.lanternpowered.porygen.delaunay.EdgeDistancePack
import org.lanternpowered.porygen.math.vector.cross
import org.spongepowered.math.vector.Vector2d
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * 2D triangle class implementation.
 *
 * @author Johannes Diemke
 */
data class Triangle2d(
    val a: Vector2d,
    val b: Vector2d,
    val c: Vector2d
) : AbstractShape() {

  private val polygon by lazy {
    Polygond.newConvexPolygon(this.a, this.b, this.c)
  }

  /**
   * The circumcenter of this triangle.
   */
  val circumcenter: Vector2d by lazy { getCircumcenter0() }

  /**
   * The centroid of this triangle.
   */
  val centroid: Vector2d by lazy { getCentroid0() }

  /**
   * The incenter of this triangle.
   */
  val incenter: Vector2d by lazy { getIncenter0() }

  private fun getCircumcenter0(): Vector2d {
    val abCenter = a.add(b).mul(0.5)
    val bcCenter = b.add(c).mul(0.5)

    val abSlope = -1.0 / ((b.y - a.y) / (b.x - a.x))
    val bcSlope = -1.0 / ((c.y - b.y) / (c.x - b.x))

    val bAB = abCenter.y - abSlope * abCenter.x
    val bBC = bcCenter.y - bcSlope * bcCenter.x

    val x = (bAB - bBC) / (bcSlope - abSlope)
    return Vector2d(x, abSlope * x + bAB)
  }

  private fun getCentroid0(): Vector2d {
    return Vector2d(
        (a.x + b.x + c.x) / 3.0,
        (a.y + b.y + c.y) / 3.0)
  }

  private fun getIncenter0(): Vector2d {
    // https://www.mathopenref.com/coordincenter.html
    var dx = b.x - c.x
    var dy = b.y - c.y
    val da = sqrt(dx * dx + dy * dy)

    dx = c.x - a.x
    dy = c.y - a.y
    val db = sqrt(dx * dx + dy * dy)

    dx = a.x - b.x
    dy = a.y - b.y
    val dc = sqrt(dx * dx + dy * dy)

    val dp = da + db + dc
    val ox = (da * a.x + db * b.x + dc * c.x) / dp
    val oy = (da * a.y + db * b.y + dc * c.y) / dp

    return Vector2d(ox, oy)
  }

  /**
   * Converts this triangle to a [Polygond].
   */
  fun toPolygon(): Polygond = this.polygon

  /**
   * Tests if a given point lies in the circumcircle of this triangle. Let the
   * triangle ABC appear in counterclockwise (CCW) order. Then when det &gt;
   * 0, the point lies inside the circumcircle through the three points a, b
   * and c. If instead det &lt; 0, the point lies outside the circumcircle.
   * When det = 0, the four points are cocircular. If the triangle is oriented
   * clockwise (CW) the result is reversed. See Real-Time Collision Detection,
   * chap. 3, p. 34.
   *
   * @param point The point to be tested
   * @return Returns true if the point lies inside the circumcircle through
   *         the three points a, b, and c of the triangle
   */
  fun isPointInCircumcircle(point: Vector2d): Boolean {
    val a11 = a.x - point.x
    val a21 = b.x - point.x
    val a31 = c.x - point.x
    val a12 = a.y - point.y
    val a22 = b.y - point.y
    val a32 = c.y - point.y
    val a13 = (a.x - point.x) * (a.x - point.x) + (a.y - point.y) * (a.y - point.y)
    val a23 = (b.x - point.x) * (b.x - point.x) + (b.y - point.y) * (b.y - point.y)
    val a33 = (c.x - point.x) * (c.x - point.x) + (c.y - point.y) * (c.y - point.y)
    val det = a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33 - a11 * a23 * a32
    return if (isOrientedCCW()) det > 0.0 else det < 0.0
  }

  /**
   * Test if this triangle is oriented counterclockwise (CCW). Let A, B and C
   * be three 2D points. If det > 0, C lies to the left of the directed
   * line AB. Equivalently the triangle ABC is oriented counterclockwise. When
   * det < 0, C lies to the right of the directed line AB, and the triangle
   * ABC is oriented clockwise. When det = 0, the three points are colinear.
   * See Real-Time Collision Detection, chap. 3, p. 32
   *
   * @return Returns true iff the triangle ABC is oriented counterclockwise (CCW)
   */
  fun isOrientedCCW(): Boolean {
    val a11 = a.x - c.x
    val a21 = b.x - c.x
    val a12 = a.y - c.y
    val a22 = b.y - c.y
    val det = a11 * a22 - a12 * a21
    return det > 0.0
  }

  /**
   * Returns true if the given vertex is one of the vertices describing this
   * triangle.
   *
   * @param vertex The vertex to be tested
   * @return Returns true if the Vertex is one of the vertices describing this triangle
   */
  fun hasVertex(vertex: Vector2d): Boolean =
      a == vertex || b == vertex || c == vertex

  /**
   * Returns the vertex of this triangle that is not part of the given edge.
   *
   * @param edge The edge
   * @return The vertex of this triangle that is not part of the edge
   */
  fun getNoneEdgeVertex(edge: Edge2d): Vector2d? {
    return when {
      a != edge.a && a != edge.b -> a
      b != edge.a && b != edge.b -> b
      c != edge.a && c != edge.b -> c
      else -> null
    }
  }

  /**
   * Returns an [EdgeDistancePack] containing the edge and its distance nearest
   * to the specified point.
   *
   * @param point The point the nearest edge is queried for
   * @return The edge of this triangle that is nearest to the specified point
   */
  fun findNearestEdge(point: Vector2d): EdgeDistancePack {
    fun distanceSquared(edge: Edge2d) = computeClosestPoint(edge, point).sub(point).lengthSquared()

    val ab = Edge2d(a, b)
    val bc = Edge2d(b, c)
    val ca = Edge2d(c, a)

    val abDistance = distanceSquared(ab)
    val bcDistance = distanceSquared(bc)
    val caDistance = distanceSquared(ca)

    return when {
      abDistance < bcDistance && abDistance < caDistance -> EdgeDistancePack(ab, sqrt(abDistance))
      bcDistance < caDistance -> EdgeDistancePack(bc, sqrt(bcDistance))
      else -> EdgeDistancePack(ca, sqrt(caDistance))
    }
  }

  /**
   * Computes the closest point on the given edge to the specified point.
   *
   * @param edge The edge on which we search the closest point to the specified point
   * @param point The point to which we search the closest point on the edge
   * @return The closest point on the given edge to the specified point
   */
  private fun computeClosestPoint(edge: Edge2d, point: Vector2d): Vector2d {
    val ab = edge.b.sub(edge.a)
    var t = point.sub(edge.a).dot(ab) / ab.dot(ab)
    if (t < 0.0) {
      t = 0.0
    } else if (t > 1.0) {
      t = 1.0
    }
    return edge.a.add(ab.mul(t))
  }

  /**
   * Returns true if this triangle contains the given edge.
   *
   * @param edge The edge to be tested
   * @return Returns true if this triangle contains the edge
   */
  fun isNeighbour(edge: Edge2d): Boolean =
      (a == edge.a || b == edge.a || c == edge.a) && (a == edge.b || b == edge.b || c == edge.b)

  override fun intersects(rectangle: Rectangled): Boolean =
      this.polygon.intersects(rectangle)

  override fun intersects(rectangle: Rectanglei): Boolean =
      this.polygon.intersects(rectangle)

  override fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean =
      this.polygon.intersects(Rectangled(Vector2d(minX, minY), Vector2d(maxX, maxY)))

  override fun intersects(polygon: Polygond): Boolean =
      this.polygon.intersects(polygon)

  override fun contains(rectangle: Rectangled): Boolean =
      this.polygon.contains(rectangle)

  override fun contains(rectangle: Rectanglei): Boolean =
      this.polygon.contains(rectangle)

  override fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean =
      this.polygon.contains(Rectangled(Vector2d(minX, minY), Vector2d(maxX, maxY)))

  override fun contains(x: Double, y: Double): Boolean = contains(Vector2d(x, y))

  override fun contains(point: Vector2d): Boolean {
    // See Real-Time Collision Detection, chap. 5, p. 206.
    val pab = point.sub(a).cross(b.sub(a))
    val pbc = point.sub(b).cross(c.sub(b))
    val signPab = sign(pab)
    if (signPab != sign(pbc))
      return false
    val pca = point.sub(c).cross(a.sub(c))
    return signPab == sign(pca)
  }
}
