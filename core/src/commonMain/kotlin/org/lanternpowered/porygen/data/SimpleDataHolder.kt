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

import org.lanternpowered.porygen.util.unsafeCast

open class SimpleDataHolder : DataHolder {

  // Data values stored within this cell
  private val dataValues = HashMap<DataKey<*>, Any?>()

  override fun <T> get(key: DataKey<T>): T? = dataValues[key].unsafeCast()

  override fun <T> set(key: DataKey<T>, value: T) {
    dataValues[key] = value as Any?
  }

  override fun <T> remove(key: DataKey<T>): T? = dataValues.remove(key).unsafeCast()
}
