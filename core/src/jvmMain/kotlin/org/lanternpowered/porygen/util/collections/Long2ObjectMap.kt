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

import java.util.function.LongFunction

typealias FuLong2ObjectMap<V> = it.unimi.dsi.fastutil.longs.Long2ObjectMap<V>
typealias FuLong2ObjectOpenHashMap<V> = it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap<V>

actual interface Long2ObjectMap<V> : FuLong2ObjectMap<V>, MutableMap<Long, V> {
  actual operator fun set(key: Long, value: V)
}

actual class Long2ObjectOpenHashMap<V>(
  private val backing: FuLong2ObjectMap<V>
) : FuLong2ObjectMap<V> by backing, Long2ObjectMap<V> {
  actual constructor() : this(FuLong2ObjectOpenHashMap())
  actual override fun set(key: Long, value: V) {
    @Suppress("ReplacePutWithAssignment")
    backing.put(key, value)
  }
  actual inline fun getOrPut(key: Long, crossinline defaultValue: () -> V): V =
    computeIfAbsent(key, LongFunction { defaultValue() })
  override fun getOrDefault(key: Long, defaultValue: V): V =
    backing.getOrDefault(key, defaultValue)
}
