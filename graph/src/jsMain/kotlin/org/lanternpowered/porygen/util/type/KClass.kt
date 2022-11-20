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
import kotlin.js.unsafeCast
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.typeOf

actual val KClass<*>.superclasses: List<KClass<*>>
  get() = getSuperclasses(this)

actual val KClass<*>.supertypes: List<KType>
  get() = getSuperclasses(this).map { it.createType() }

actual fun KClass<*>.isSubclassOf(base: KClass<*>): Boolean = base.isSuperclassOf(this)

actual fun KClass<*>.isSuperclassOf(derived: KClass<*>): Boolean {
  if (this == derived)
    return true
  val metadata = derived.js.asDynamic().`$metadata$`
    ?: return false
  var cache = metadata.isSuperclass.unsafeCast<MutableMap<KClass<*>, Boolean>?>()
  if (cache == null) {
    cache = HashMap()
    metadata.isSuperclass = cache
  }
  return cache.computeIfAbsent(this) { checkSuperclass(this, derived) }
}

private fun checkSuperclass(base: KClass<*>, derived: KClass<*>): Boolean {
  if (base == derived)
    return true
  for (inner in derived.superclasses) {
    if (checkSuperclass(base, inner))
      return true
  }
  return false
}

/**
 * Gets the superclasses of the given [KClass].
 */
private fun getSuperclasses(derived: KClass<*>): List<KClass<*>> {
  val js = derived.js.asDynamic()
  val metadata = js.`$metadata$`
    ?: return emptyList()
  val cached = metadata.`$superclasses$`.unsafeCast<List<KClass<*>>?>()
  if (cached != null)
    return cached
  val interfaces = metadata.interfaces
  val mutableSuperclasses = ArrayList<KClass<*>>()
  val superPrototype = js("Object").getPrototypeOf(js.prototype)
  val superclass = if (superPrototype != null && superPrototype != js("Object").prototype) {
    superPrototype.constructor.unsafeCast<JsClass<*>>().kotlin
  } else {
    Any::class
  }
  mutableSuperclasses += superclass
  for (itf in interfaces) {
    mutableSuperclasses += itf.unsafeCast<JsClass<*>>().kotlin
  }
  val superclasses = mutableSuperclasses.asUnmodifiableList()
  metadata.`$superclasses$` = superclasses
  return superclasses
}

object KTypeConstructor {
  val value = typeOf<Any>()::class.js.asDynamic().prototype.constructor
}

@Suppress("UNUSED_VARIABLE")
actual fun KClass<*>.createType(arguments: List<KTypeProjection>?, nullable: Boolean): KType {
  val constructor = KTypeConstructor.value
  val nonNullArguments = arguments ?: emptyList()
  val classifier = this
  return js("new constructor(classifier, nonNullArguments, nullable)").unsafeCast<KType>()
}
