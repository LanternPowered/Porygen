/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node

import kotlin.reflect.KProperty

inline class PropertyId(val value: String)

/**
 * Represents a property of a node.
 */
interface Property<T> {

  /**
   * The node this property belongs to.
   */
  val node: Node

  /**
   * The id of the property.
   */
  val id: PropertyId

  /**
   * Sets the value of the property.
   */
  fun set(value: T)

  /**
   * Gets the value of the property.
   */
  fun get(): T

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)
}
