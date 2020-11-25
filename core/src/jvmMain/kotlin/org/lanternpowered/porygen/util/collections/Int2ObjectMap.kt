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

import java.util.function.IntFunction
import kotlin.reflect.full.createType

typealias FuInt2ObjectMap<V> = it.unimi.dsi.fastutil.ints.Int2ObjectMap<V>
typealias FuInt2ObjectOpenHashMap<V> = it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap<V>

actual interface Int2ObjectMap<V> : FuInt2ObjectMap<V>, MutableMap<Int, V> {
  actual operator fun set(key: Int, value: V)
}

actual class Int2ObjectOpenHashMap<V>(
  private val backing: FuInt2ObjectMap<V>
) : FuInt2ObjectMap<V> by backing, Int2ObjectMap<V> {
  actual constructor() : this(FuInt2ObjectOpenHashMap())
  actual override fun set(key: Int, value: V) {
    @Suppress("ReplacePutWithAssignment")
    backing.put(key, value)
    this::class.createType()
  }
  actual inline fun getOrPut(key: Int, crossinline defaultValue: () -> V): V =
    computeIfAbsent(key, IntFunction { defaultValue() })
  override fun getOrDefault(key: Int, defaultValue: V): V =
    backing.getOrDefault(key, defaultValue)
}
