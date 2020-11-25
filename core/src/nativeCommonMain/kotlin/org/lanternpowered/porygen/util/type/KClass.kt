/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.type

import kotlin.reflect.KClass

actual fun KClass<*>.isSubclassOf(type: KClass<*>): Boolean = TODO()
actual fun KClass<*>.isSuperclassOf(type: KClass<*>): Boolean = TODO()

actual val KClass<*>.superclasses: List<KClass<*>>
  get() = TODO()
