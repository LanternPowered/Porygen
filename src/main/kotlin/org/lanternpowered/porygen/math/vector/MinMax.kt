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

import kotlin.math.min
import kotlin.math.max

fun min(first: Vector2d, second: Vector2d): Vector2d =
    Vector2d(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vector2d, second: Vector2d): Vector2d =
    Vector2d(max(first.x, second.x), max(first.y, second.y))

fun min(first: Vector2i, second: Vector2i): Vector2i =
    Vector2i(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vector2i, second: Vector2i): Vector2i =
    Vector2i(max(first.x, second.x), max(first.y, second.y))
