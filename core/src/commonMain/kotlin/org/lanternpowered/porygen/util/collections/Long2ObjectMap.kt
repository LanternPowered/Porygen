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

expect interface Long2ObjectMap<V> : MutableMap<Long, V>

expect fun <V> long2ObjectMapOf(): Long2ObjectMap<V>

expect fun <V> Long2ObjectMap<V>.putUnboxed(key: Long, value: V): V?

expect fun <V> Long2ObjectMap<V>.getUnboxed(key: Long): V?

expect fun <V> Long2ObjectMap<V>.removeUnboxed(key: Long): V?

expect inline fun <V> Long2ObjectMap<V>.getOrPutUnboxed(key: Long, crossinline fn: (key: Long) -> V): V
