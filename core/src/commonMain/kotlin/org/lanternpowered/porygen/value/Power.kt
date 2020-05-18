/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.value

import kotlin.math.pow

data class Power(
    val source: Value,
    val exponent: Value
) : Value {

  override fun get(): Double = source.get().pow(exponent.get())
}
