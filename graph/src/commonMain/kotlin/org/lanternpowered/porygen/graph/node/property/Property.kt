/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node.property

import org.lanternpowered.porygen.graph.data.DataType
import org.lanternpowered.porygen.graph.node.Node
import kotlin.reflect.KProperty

/**
 * Represents the identifier of a property.
 */
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
   * The data type of the property.
   */
  val dataType: DataType<T>

  /**
   * The value of the property.
   */
  var value: T

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }
}
