/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("NOTHING_TO_INLINE")

package org.lanternpowered.porygen.util.collections

actual interface Int2ObjectMap<V> : MutableMap<Int, V> {
  actual operator fun set(key: Int, value: V)
}

actual class Int2ObjectOpenHashMap<V>(
    private val backing: MutableMap<Int, V>
) : Int2ObjectMap<V>, MutableMap<Int, V> by backing {
  actual constructor() : this(HashMap())
  actual override fun set(key: Int, value: V) {
    backing[key] = value
  }
  actual inline fun getOrPut(key: Int, crossinline defaultValue: () -> V): V =
      computeIfAbsent(key, defaultValue)
}
