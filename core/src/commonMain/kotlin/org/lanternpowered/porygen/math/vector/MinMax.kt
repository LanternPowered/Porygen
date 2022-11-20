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

fun min(first: Vec2d, second: Vec2d): Vec2d =
  Vec2d(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vec2d, second: Vec2d): Vec2d =
  Vec2d(max(first.x, second.x), max(first.y, second.y))

fun min(first: Vec2i, second: Vec2i): Vec2i =
  Vec2i(min(first.x, second.x), min(first.y, second.y))

fun max(first: Vec2i, second: Vec2i): Vec2i =
  Vec2i(max(first.x, second.x), max(first.y, second.y))
