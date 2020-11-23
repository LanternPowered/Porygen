/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName", "UNCHECKED_CAST")

package org.lanternpowered.porygen.graph.data

import org.lanternpowered.porygen.util.Color

/**
 * Creates a data type set by adding the given data type.
 */
operator fun <T> DataType<out T>.plus(type: DataType<out T>): DataTypeSet<T> =
  DataTypeSetImpl(setOf(this, type))

/**
 * Creates a data type set by adding the given data type.
 */
operator fun <T> DataTypeSet<out T>.plus(type: DataType<out T>): DataTypeSet<T> =
  DataTypeSetImpl((this as DataTypeSetImpl).set + type)

/**
 * Represents a set of [DataType]s.
 */
interface DataTypeSet<T> : Set<DataType<out T>>

/**
 * Represents a single data type.
 */
interface DataType<T> {

  /**
   * The name of the data type.
   */
  val name: String

  /**
   * The color of the data type. The color
   * can be `null` if undefined.
   */
  val color: Color?

  /**
   * All the supertypes of this data type.
   */
  val supertypes: Collection<DataType<in T>>

  /**
   * Whether this data type is a subtype
   * of the given data type.
   */
  fun isSubtypeOf(type: DataType<*>): Boolean

  /**
   * Whether this data type is a supertype
   * of the given data type.
   */
  fun isSupertypeOf(type: DataType<*>): Boolean
}

private class DataTypeSetImpl<T>(
  val set: Set<DataType<out T>>
) : DataTypeSet<T>, Set<DataType<out T>> by set

private class DataTypeImpl<T>(
  override val name: String,
  override val color: Color?,
  override val supertypes: Collection<DataType<in T>>
) : DataType<T> {
  override fun isSubtypeOf(type: DataType<*>): Boolean =
    type.isSupertypeOf(this)
  override fun isSupertypeOf(type: DataType<*>): Boolean {
    if (this == type)
      return true
    val impl = type as? DataTypeImpl<*> ?: return false
    if (impl.supertypes.contains(this) ||
      impl.supertypes.any { it.isSupertypeOf(this) }
    ) {
      return true
    }
    return false
  }
}
