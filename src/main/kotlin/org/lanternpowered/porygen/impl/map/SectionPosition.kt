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

import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.util.pair.unpackIntPairFirst
import org.lanternpowered.porygen.util.pair.unpackIntPairSecond

inline class SectionPosition(val packed: Long) {

  constructor(x: Int, y: Int) : this(packIntPair(x, y))

  val x: Int get() = unpackIntPairFirst(this.packed)
  val y: Int get() = unpackIntPairSecond(this.packed)
}
