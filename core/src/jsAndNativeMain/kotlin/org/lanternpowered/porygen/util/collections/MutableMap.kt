/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("UNCHECKED_CAST")

package org.lanternpowered.porygen.util.collections

@PublishedApi
internal inline fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, crossinline function: () -> V): V {
  var value = get(key)
  if (value != null || containsKey(key))
    return value as V
  value = function()
  put(key, value)
  return value
}
