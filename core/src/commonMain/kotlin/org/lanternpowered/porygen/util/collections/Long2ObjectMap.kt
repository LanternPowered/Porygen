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

expect interface Long2ObjectMap<V> : MutableMap<Long, V> {
  operator fun set(key: Long, value: V)
}

expect class Long2ObjectOpenHashMap<V>() : Long2ObjectMap<V> {
  override operator fun set(key: Long, value: V)
  inline fun getOrPut(key: Long, crossinline defaultValue: () -> V): V
}
