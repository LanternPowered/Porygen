/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.data.DataKey
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapElement

/**
 * Represents an element that is bound to a specific [MapViewImpl].
 */
abstract class MapElementView<D : CellMapElement> : CellMapElement {

  abstract val delegate: D
  abstract val view: MapViewImpl

  override val id: Long
    get() = delegate.id

  override val map: CellMap
    get() = delegate.map

  override fun <T> get(key: DataKey<T>): T? = delegate[key]

  override fun <T> set(key: DataKey<T>, value: T) {
    delegate[key] = value
  }

  override fun <T> remove(key: DataKey<T>): T? = delegate.remove(key)
}
