/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.data

import org.lanternpowered.porygen.util.Color
import kotlin.reflect.KClass

inline fun <reified T : Any> DataTypeRegistry.createFromClass(
  name: String, noinline block: KClassDataTypeBuilder<T>.() -> Unit
): DataType<T> = createFromClass(T::class, name, block)

/**
 * Represents the registry of all the [DataType]s.
 */
interface DataTypeRegistry {

  fun <T> create(name: String, block: DataTypeBuilder<T>.() -> Unit): DataType<T>

  fun <T : Any> createFromClass(
    kClass: KClass<T>, name: String, block: KClassDataTypeBuilder<T>.() -> Unit
  ): DataType<T>

  operator fun <T : Any> get(kClass: KClass<T>): DataType<T>
}

interface DataTypeBuilder<T> {
  fun color(color: Color)
  fun supertype(type: DataType<in T>)
}

inline fun <reified T : Any> KClassDataTypeBuilder<out T>.supertype() = supertype(T::class)

interface KClassDataTypeBuilder<T : Any> : DataTypeBuilder<T> {
  fun supertype(type: KClass<in T>)
}
