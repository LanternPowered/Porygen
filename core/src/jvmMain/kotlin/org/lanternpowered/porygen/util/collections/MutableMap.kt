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

actual inline fun <K, V> MutableMap<K, V>.getOrPutUnboxed(key: K, noinline fn: (key: K) -> V): V = computeIfAbsent(key, fn)
