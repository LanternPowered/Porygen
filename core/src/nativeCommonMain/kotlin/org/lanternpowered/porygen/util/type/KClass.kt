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

actual fun KClass<*>.isSubclassOf(base: KClass<*>): Boolean = TODO()
actual fun KClass<*>.isSuperclassOf(derived: KClass<*>): Boolean = TODO()

actual val KClass<*>.superclasses: List<KClass<*>> get() = TODO()
actual val KClass<*>.supertypes: List<KType> get() = TODO()

actual fun KClass<*>.createType(arguments: List<KTypeProjection>, nullable: Boolean): KType = TODO()
