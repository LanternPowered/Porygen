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

actual fun <K, V> MutableMap<K, V>.getOrPutUnboxed(key: K, fn: (key: K) -> V): V = computeIfAbsent(key, fn)

inline fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, crossinline fn: (key: K) -> V): V {
  var value = get(key)
  if (value != null)
    return value
  value = fn(key)
  put(key, value)
  return value
}
