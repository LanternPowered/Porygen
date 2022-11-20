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

import org.lanternpowered.porygen.util.ToStringHelper

/**
 * Represents a key that can be used to attach data to a [DataHolder].
 *
 * @param T The data value type
 */
class DataKey<T>(val name: String) {

  override fun toString(): String =
      ToStringHelper(this).add("name", name).toString()
}
