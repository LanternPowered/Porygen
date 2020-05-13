/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.math.vector

import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i
import org.lanternpowered.porygen.math.floorToInt
import kotlin.math.max
import kotlin.math.min

/**
 * Computes the 2D pseudo cross product Dot(Perp(this), vector)
 * of this and the given vector.
 *
 * @param vector The vector to be multiplied to the perpendicular vector of this
 * @return A new instance holding the result of the pseudo cross product
 */
fun Vector2d.cross(vector: Vector2d): Double =
    this.y * vector.x - this.x * vector.y

fun min(first: Vector2d, second: Vector2d): Vector2d =
    Vector2d(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vector2d, second: Vector2d): Vector2d =
    Vector2d(max(first.x, second.x), max(first.y, second.y))

fun min(first: Vector2i, second: Vector2i): Vector2i =
    Vector2i(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vector2i, second: Vector2i): Vector2i =
    Vector2i(max(first.x, second.x), max(first.y, second.y))

fun Vector2d.floorToInt(): Vector2i =
    Vector2i(floorToInt(x), floorToInt(y))
