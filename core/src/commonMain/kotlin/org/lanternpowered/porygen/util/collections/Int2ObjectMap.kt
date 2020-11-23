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

expect interface Int2ObjectMap<V> : MutableMap<Int, V> {
  operator fun set(key: Int, value: V)
}

expect class Int2ObjectOpenHashMap<V>() : Int2ObjectMap<V> {
  override operator fun set(key: Int, value: V)
  inline fun getOrPut(key: Int, crossinline defaultValue: () -> V): V
}
