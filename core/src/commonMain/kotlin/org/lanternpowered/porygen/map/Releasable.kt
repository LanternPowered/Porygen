/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Uses the [Releasable] and releases it when the [block] was executed.
 */
fun <T : Releasable, R> T.use(block: T.() -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  try {
    return block()
  } finally {
    release()
  }
}

interface Releasable {

  /**
   * Releases the [Releasable].
   */
  fun release()
}
