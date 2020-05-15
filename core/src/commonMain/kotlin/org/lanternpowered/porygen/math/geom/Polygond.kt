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

import org.lanternpowered.porygen.math.vector.Vector2d

/**
 * Represents a polygon.
 */
class Polygond : AbstractShape {

  /**
   * Gets a [List] with all the vertices
   * in this [Polygond].
   *
   * The vertices should be sorted clockwise.
   *
   * @return The vertices
   */
  val vertices: List<Vector2d>

  // Whether the polygon is convex, -1 means not yet computed
  private var isConvexState = -1

  /**
   * The centroid of this polygon.
   */
  val centroid: Vector2d
    get() {
      var x = 0.0
      var y = 0.0
      for (vertex in vertices) {
        x += vertex.x
        y += vertex.y
      }
      return Vector2d(
          x / vertices.size.toDouble(),
          y / vertices.size.toDouble())
    }

  /**
   * Whether this polygon is a convex polygon.
   */
  val isConvex: Boolean
    get() {
      // Lazily compute convex
      if (isConvexState != -1)
        return isConvexState > 0
      val isConvex = computeConvexState()
      isConvexState = if (isConvex) 1 else 0
      return isConvex
    }

  private fun computeConvexState(): Boolean {
    // https://stackoverflow.com/questions/471962/how-do-i-efficiently-determine-if-a-polygon-is-convex-non-convex-or-complex
    if (vertices.size < 4)
      return true
    var sign = false
    val n = vertices.size
    for (i in 0 until n) {
      val a = vertices[i]
      val b = vertices[(i + 1) % n]
      val c = vertices[(i + 2) % n]

      val dx1 = c.x - b.x
      val dy1 = c.y - b.y
      val dx2 = a.x - b.x
      val dy2 = a.y - b.y

      val sign1 = dx1 * dy2 - dy1 * dx2 > 0
      if (i == 0) {
        sign = sign1
      } else if (sign != sign1)
        return false
    }
    return true
  }

  private constructor(vertices: List<Vector2d>) {
    check(vertices.size >= 3) { "There must be at least 3 vertices." }
    this.vertices = vertices
  }

  /**
   * Constructs a [Polygond] from the given vertices.
   *
   * The polygon vertices should be sorted clockwise.
   *
   * @param vertices The vertices
   */
  constructor(vararg vertices: Vector2d) {
    check(vertices.size >= 3) { "There must be at least 3 vertices." }
    this.vertices = vertices.asList()
  }

  /**
   * Constructs a [Polygond] from the given vertices.
   *
   * The polygon vertices should be sorted clockwise.
   *
   * @param vertices The vertices
   */
  constructor(vertices: Iterable<Vector2d>) : this(vertices.toList())

  override fun contains(x: Double, y: Double): Boolean {
    // https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
    var i = 0
    var j = vertices.size - 1
    var result = false
    while (i < vertices.size) {
      val vi = vertices[i]
      val vj = vertices[j]
      if (vi.y > y != vj.y > y && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + vi.x)
        result = !result
      j = i++
    }
    return result
  }

  override fun contains(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean {
    // Non convex polygons need special handling
    if (!isConvex && trueIntersection(minX, minY, maxX, maxY))
      return false
    // Just check if the 4 corners are inside this polygon
    return contains(minX, minY) && contains(minX, maxY) && contains(maxX, minY) && contains(maxX, maxY)
  }

  internal fun trueIntersection(shape: Shape): Boolean {
    return when (shape) {
      is Rectangled -> trueIntersection(shape)
      is Rectanglei -> trueIntersection(shape)
      is Polygond -> trueIntersection(shape)
      else -> throw IllegalStateException()
    }
  }

  private fun trueIntersection(rectangle: Rectanglei): Boolean {
    val min = rectangle.min
    val max = rectangle.max
    return trueIntersection(min.x.toDouble(), min.y.toDouble(), max.x.toDouble(), max.y.toDouble())
  }

  private fun trueIntersection(rectangle: Rectangled): Boolean {
    val min = rectangle.min
    val max = rectangle.max
    return trueIntersection(min.x, min.y, max.x, max.y)
  }

  private fun trueIntersection(polygon: Polygond): Boolean {
    var i = 0
    var j = vertices.size - 1
    while (i < vertices.size) {
      val vi = vertices[i]
      val vj = vertices[j]
      var k = 0
      var l = vertices.size - 1
      while (k < vertices.size) {
        val vk = polygon.vertices[k]
        val vl = polygon.vertices[l]
        if (trueIntersection(vk.x, vk.y, vl.x, vl.y, vi.x, vi.y, vj.x, vj.y))
          return true
        l = k++
      }
      j = i++
    }
    return false
  }

  /**
   * Similar to [intersects], but doesn't see parallel lines as an intersection, this
   * method will be used by contains methods. So, lines really have to cross in order
   * to have an intersection, and lines can not be just on top of each other.
   *
   * @param minX The minimum x value
   * @param minY The minimum y value
   * @param maxX The maximum x value
   * @param maxY The maximum y value
   * @return Whether there is a true intersection
   */
  private fun trueIntersection(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean {
    var i = 0
    var j = vertices.size - 1
    while (i < vertices.size) {
      val vi = vertices[i]
      val vj = vertices[j]
      if (trueIntersection(minX, minY, maxX, minY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (trueIntersection(maxX, minY, maxX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (trueIntersection(minX, minY, minX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (trueIntersection(minX, maxY, maxX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      j = i++
    }
    return false
  }

  override fun intersects(minX: Double, minY: Double, maxX: Double, maxY: Double): Boolean {
    var i = 0
    var j = vertices.size - 1
    while (i < vertices.size) {
      val vi = vertices[i]
      val vj = vertices[j]
      if (Line2d.linesIntersect(minX, minY, maxX, minY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (Line2d.linesIntersect(maxX, minY, maxX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (Line2d.linesIntersect(minX, minY, minX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      if (Line2d.linesIntersect(minX, maxY, maxX, maxY, vi.x, vi.y, vj.x, vj.y))
        return true
      j = i++
    }
    return false
  }

  override fun intersects(polygon: Polygond): Boolean {
    var i = 0
    var j = vertices.size - 1
    while (i < vertices.size) {
      val vi = vertices[i]
      val vj = vertices[j]
      var k = 0
      var l = vertices.size - 1
      while (k < vertices.size) {
        val vk = polygon.vertices[k]
        val vl = polygon.vertices[l]
        if (Line2d.linesIntersect(vk.x, vk.y, vl.x, vl.y, vi.x, vi.y, vj.x, vj.y))
          return true
        l = k++
      }
      j = i++
    }
    return false
  }

  /**
   * Multiplies all the vertices of this polygon
   * with the given [vector] and returns a new
   * [Polygond].
   */
  fun scale(vector: Vector2d): Polygond {
    val vertices = vertices.map { vertex -> vertex * vector }
    val polygon = Polygond(vertices)
    polygon.isConvexState = this.isConvexState
    return polygon
  }

  /**
   * Translates all the vertices of this polygon
   * with the given [vector] and returns a new
   * [Polygond].
   */
  fun translate(vector: Vector2d): Polygond {
    val vertices = vertices.map { vertex -> vertex + vector }
    val polygon = Polygond(vertices)
    polygon.isConvexState = this.isConvexState
    return polygon
  }

  override fun toString(): String =
      this::class.simpleName + vertices.toTypedArray().contentToString()

  companion object {

    /**
     * Creates a [Polygond] from the given vertices
     * of which is known that it is a convex polygon.
     *
     * The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     * @return The polygon
     */
    fun newConvexPolygon(vararg vertices: Vector2d): Polygond {
      val polygon = Polygond(vertices.asList())
      polygon.isConvexState = 1
      return polygon
    }

    /**
     * Creates a [Polygond] from the given vertices
     * of which is known that it is a convex polygon.
     *
     * The polygon vertices should be sorted clockwise.
     *
     * @param vertices The vertices
     * @return The polygon
     */
    fun newConvexPolygon(vertices: Iterable<Vector2d>): Polygond {
      val polygon = Polygond(vertices)
      polygon.isConvexState = 1
      return polygon
    }

    private fun trueIntersection(
        x1: Double, y1: Double,
        x2: Double, y2: Double,
        x3: Double, y3: Double,
        x4: Double, y4: Double
    ): Boolean {
      // Fail fast if possible
      if (x1 == x3 && y1 == y3 || x2 == x4 && y2 == y4)
        return false
      // If they don't intersect, well that's the end
      if (!Line2d.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4))
        return false

      // https://stackoverflow.com/questions/17692922/check-is-a-point-x-y-is-between-two-points-drawn-on-a-straight-line

      // C: start point line 1
      // B: end point line 1
      // A: a point from line 2
      // C---A-------B

      // |CB|
      val dx1 = x2 - x1
      val dy1 = y2 - y1
      // square distance
      val d1 = dx1 * dx1 + dy1 * dy1

      // first point 3, start point from line 2

      // |CA|
      var dx2 = x3 - x1
      var dy2 = x3 - x1
      // square distance
      var d2 = dx2 * dx2 + dy2 * dy2

      // |AB|
      var dx3 = x2 - x3
      var dy3 = y2 - y3
      // square distance
      var d3 = dx3 * dx3 + dy3 * dy3

      // point a is on the line, true intersection is not possible
      if (d2 + d3 == d1)
        return false

      // then point 4, end point from line 2

      // |CA|
      dx2 = x4 - x1
      dy2 = y4 - y1
      // square distance
      d2 = dx2 * dx2 + dy2 * dy2

      // |AB|
      dx3 = x2 - x4
      dy3 = y2 - y4
      // square distance
      d3 = dx3 * dx3 + dy3 * dy3

      // check point a is on the line, true intersection is not possible
      return d2 + d3 != d1
    }
  }
}
