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
import org.lanternpowered.porygen.math.vector.max
import org.lanternpowered.porygen.math.vector.min
import org.lanternpowered.porygen.math.vector.Vec2i
import kotlin.math.max
import kotlin.math.min

/**
 * Represents a rectangle.
 */
@Serializable
class Rectanglei private constructor(
  override val min: Vec2i,
  override val max: Vec2i,
  @Transient private val ignored: Unit = Unit,
) : AbstractRectangle<Vec2i>() {

  override val size: Vec2i by lazy { max - min }

  constructor(min: Vec2i, max: Vec2i) :
    this(
      min = min(min, max),
      max = max(min, max),
      ignored = Unit,
    )

  constructor(minX: Int, minY: Int, maxX: Int, maxY: Int) :
    this(
      min = Vec2i(min(minX, maxX), min(minY, maxY)),
      max = Vec2i(max(minX, maxX), max(minY, maxY)),
    )

  override fun contains(x: Double, y: Double): Boolean =
    x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun contains(x: Int, y: Int): Boolean =
    x <= max.x && x >= min.x && y <= max.y && y >= min.y

  override fun toInt() = this

  override fun toDouble(): Rectangled =
    Rectangled(min.x.toDouble(), min.y.toDouble(), max.x.toDouble(), max.y.toDouble())

  fun translate(offset: Vec2i): Rectanglei =
    Rectanglei(min + offset, max + offset)
}
