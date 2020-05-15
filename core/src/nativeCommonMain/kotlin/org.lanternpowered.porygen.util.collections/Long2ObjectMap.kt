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

actual interface Long2ObjectMap<V> : MutableMap<Long, V>

class Long2ObjectHashMap<V>(backing: MutableMap<Long, V> = mutableMapOf()) : Long2ObjectMap<V>, MutableMap<Long, V> by backing

actual inline fun <V> long2ObjectMapOf(): Long2ObjectMap<V> = Long2ObjectHashMap()

actual inline fun <V> Long2ObjectMap<V>.putUnboxed(key: Long, value: V): V? = put(key, value)

actual inline fun <V> Long2ObjectMap<V>.getUnboxed(key: Long): V? = get(key)

actual inline fun <V> Long2ObjectMap<V>.removeUnboxed(key: Long): V? = remove(key)

actual inline fun <V> Long2ObjectMap<V>.getOrPutUnboxed(key: Long, crossinline fn: (key: Long) -> V): V = computeIfAbsent(key, fn)
