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
import kotlin.reflect.full.createType as createType0

actual fun KClass<*>.isSubclassOf(base: KClass<*>): Boolean =
  base.isSuperclassOf(this)

actual fun KClass<*>.isSuperclassOf(derived: KClass<*>): Boolean =
  java.isAssignableFrom(derived.java)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val KClass<*>.superclasses: List<KClass<*>>
  get() = supertypes.mapNotNull { it.classifier as? KClass<*> }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val KClass<*>.supertypes: List<KType>
  get() = supertypes

actual fun KClass<*>.createType(arguments: List<KTypeProjection>, nullable: Boolean): KType =
  createType0(nullable = nullable)
