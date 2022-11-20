/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.collections

actual interface Long2ObjectMap<V> : MutableMap<Long, V> {
  actual operator fun set(key: Long, value: V)
}

actual class Long2ObjectOpenHashMap<V>(
    private val backing: MutableMap<Long, V>
) : Long2ObjectMap<V>, MutableMap<Long, V> by backing {
  actual constructor() : this(HashMap())
  actual override fun set(key: Long, value: V) {
    backing[key] = value
  }
  actual inline fun getOrPut(key: Long, crossinline defaultValue: () -> V): V =
    computeIfAbsent(key, defaultValue)
}
