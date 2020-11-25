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

import org.lanternpowered.porygen.util.collections.asUnmodifiableList
import org.lanternpowered.porygen.util.collections.computeIfAbsent
import kotlin.reflect.KClass

private val superclasses = HashMap<KClass<*>, List<KClass<*>>>()
private val isSuperclass = HashMap<IsSuperclassKey, Boolean>()

actual val KClass<*>.superclasses: List<KClass<*>>
  get() = getSuperclasses(this)

private data class IsSuperclassKey(
  private val superclass: KClass<*>,
  private val subclass: KClass<*>
)

actual fun KClass<*>.isSubclassOf(type: KClass<*>): Boolean = type.isSuperclassOf(this)

actual fun KClass<*>.isSuperclassOf(type: KClass<*>): Boolean {
  if (this == type)
    return true
  val key = IsSuperclassKey(this, type)
  return isSuperclass.computeIfAbsent(key) {
    checkSuperclass(this, type)
  }
}

private fun checkSuperclass(superclass: KClass<*>, subclass: KClass<*>): Boolean {
  if (superclass == subclass)
    return true
  for (inner in subclass.superclasses) {
    if (checkSuperclass(superclass, inner))
      return true
  }
  return false
}

/**
 * Gets the superclasses of the given [KClass] and caches them.
 */
private fun getSuperclasses(kClass: KClass<*>): List<KClass<*>> =
  superclasses.computeIfAbsent(kClass) { findSuperclasses(kClass) }

/**
 * Finds the superclasses of the given [KClass] through the js metadata.
 */
private fun findSuperclasses(kClass: KClass<*>): List<KClass<*>> {
  val metadata = kClass.js.asDynamic().`$metadata$`
    ?: return emptyList()
  val superclassObjs = metadata.interfaces
  val superclasses = ArrayList<KClass<*>>()
  for (superclassObj in superclassObjs) {
    val superclass = js("Kotlin").getKClass(superclassObj).unsafeCast<KClass<*>>()
    superclasses += superclass
  }
  return superclasses.asUnmodifiableList()
}
