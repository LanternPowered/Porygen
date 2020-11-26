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
import kotlin.reflect.KTypeProjection

expect fun KClass<*>.isSubclassOf(base: KClass<*>): Boolean
expect fun KClass<*>.isSuperclassOf(derived: KClass<*>): Boolean

expect fun KClass<*>.createType(
  arguments: List<KTypeProjection> = emptyList(),
  nullable: Boolean = false
): KType

expect val KClass<*>.superclasses: List<KClass<*>>
expect val KClass<*>.supertypes: List<KType>
