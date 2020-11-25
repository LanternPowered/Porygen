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

expect fun KClass<*>.isSubclassOf(type: KClass<*>): Boolean
expect fun KClass<*>.isSuperclassOf(type: KClass<*>): Boolean

expect val KClass<*>.superclasses: List<KClass<*>>
