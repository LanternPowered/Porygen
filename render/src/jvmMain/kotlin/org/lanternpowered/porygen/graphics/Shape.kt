/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graphics

import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.math.geom.Polygond
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.geom.Rectanglei
import java.awt.Polygon

/**
 * Converts this [Polygond] into a drawable [Polygon].
 *
 * @return The drawable polygon
 */
fun Polygond.toDrawable(): Polygon {
  val pointsX = IntArray(vertices.size)
  val pointsY = IntArray(vertices.size)
  for (i in pointsX.indices) {
    val vertex = vertices[i]
    pointsX[i] = vertex.x.toInt()
    pointsY[i] = vertex.y.toInt()
  }
  return Polygon(pointsX, pointsY, pointsX.size)
}

/**
 * Converts this [Rectangled] into a drawable [Polygon].
 *
 * @return The drawable polygon
 */
fun Rectangled.toDrawable(): Polygon {
  val pointsX = IntArray(4)
  val pointsY = IntArray(4)
  pointsX[0] = floorToInt(min.x)
  pointsY[0] = floorToInt(min.y)
  pointsX[1] = floorToInt(max.x)
  pointsY[1] = floorToInt(min.y)
  pointsX[2] = floorToInt(max.x)
  pointsY[2] = floorToInt(max.y)
  pointsX[3] = floorToInt(min.x)
  pointsY[3] = floorToInt(max.y)
  return Polygon(pointsX, pointsY, 4)
}

/**
 * Converts this [Rectangled] into a drawable [Polygon].
 *
 * @return The drawable polygon
 */
fun Rectanglei.toDrawable(): Polygon {
  val pointsX = IntArray(4)
  val pointsY = IntArray(4)
  pointsX[0] = min.x
  pointsY[0] = min.y
  pointsX[1] = max.x
  pointsY[1] = min.y
  pointsX[2] = max.x
  pointsY[2] = max.y
  pointsX[3] = min.x
  pointsY[3] = max.y
  return Polygon(pointsX, pointsY, 4)
}
