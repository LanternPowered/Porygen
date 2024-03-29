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

import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.util.Named
import org.lanternpowered.porygen.util.type.GenericType
import kotlin.jvm.JvmInline
import kotlin.reflect.KProperty

/**
 * Represents the identifier of a property.
 */
@JvmInline
value class PropertyId(val value: String)

/**
 * Represents a property of a node.
 */
interface Property<T> : Named {

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
  val dataType: GenericType<T>

  /**
   * The value of the property.
   */
  var value: T

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }
}
