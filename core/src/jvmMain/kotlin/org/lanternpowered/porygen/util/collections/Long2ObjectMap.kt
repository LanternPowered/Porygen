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

actual typealias Long2ObjectMap<V> = it.unimi.dsi.fastutil.longs.Long2ObjectMap<V>

actual inline fun <V> long2ObjectMapOf(): Long2ObjectMap<V> = it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap()

actual inline fun <V> Long2ObjectMap<V>.putUnboxed(key: Long, value: V): V? = put(key, value)

actual inline fun <V> Long2ObjectMap<V>.getUnboxed(key: Long): V? = get(key)

actual inline fun <V> Long2ObjectMap<V>.getOrPutUnboxed(key: Long, crossinline fn: (key: Long) -> V): V = computeIfAbsent(key) { fn(it) }

actual inline fun <V> Long2ObjectMap<V>.removeUnboxed(key: Long): V? = remove(key)
