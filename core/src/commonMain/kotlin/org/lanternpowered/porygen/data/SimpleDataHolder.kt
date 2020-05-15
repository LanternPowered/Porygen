/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.data

import org.lanternpowered.porygen.util.uncheckedCast

open class SimpleDataHolder : DataHolder {

  // Data values stored within this cell
  private val dataValues = mutableMapOf<DataKey<*>, Any?>()

  override fun <T> get(key: DataKey<T>): T? = this.dataValues[key].uncheckedCast()

  override fun <T> set(key: DataKey<T>, value: T) {
    this.dataValues[key] = value as Any?
  }

  override fun <T> remove(key: DataKey<T>): T? {
    return this.dataValues.remove(key).uncheckedCast()
  }
}
