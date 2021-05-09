/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")

package org.lanternpowered.porygen.util.collections

inline fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, crossinline fn: () -> V): V {
  var value = get(key)
  if (value != null || containsKey(key))
    return value as V
  value = fn()
  put(key, value)
  return value
}
