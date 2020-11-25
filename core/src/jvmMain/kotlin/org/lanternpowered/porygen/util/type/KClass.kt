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
import kotlin.reflect.KType

actual fun KClass<*>.isSubclassOf(type: KClass<*>): Boolean =
  type.isSuperclassOf(this)

actual fun KClass<*>.isSuperclassOf(type: KClass<*>): Boolean =
  java.isAssignableFrom(type.java)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val KClass<*>.superclasses: List<KClass<*>>
  get() = supertypes.mapNotNull { it.classifier as? KClass<*> }
