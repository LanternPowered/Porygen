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

fun <K, V> Map<K, V>.asUnmodifiableMap(): Map<K, V> =
  if (this is UnmodifiableMap<*,*>) this else UnmodifiableMap(this)

private class UnmodifiableMap<K, V>(
  private val delegate: Map<K, V>
) : Map<K, V> by delegate {
  override val entries: Set<Map.Entry<K, V>> =
    delegate.entries.asUnmodifiableSet()
  override val keys: Set<K> =
    delegate.keys.asUnmodifiableSet()
  override val values: Collection<V> =
    delegate.values.asUnmodifiableCollection()
}
