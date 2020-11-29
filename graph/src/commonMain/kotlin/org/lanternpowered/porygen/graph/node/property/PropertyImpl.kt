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
import org.lanternpowered.porygen.util.type.GenericType

internal class PropertyImpl<T>(
  override val id: PropertyId,
  override val dataType: GenericType<T>,
  override var value: T,
  override val node: Node,
) : Property<T>
