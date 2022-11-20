/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package org.lanternpowered.porygen.util.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType as createKType
import java.lang.Integer as BoxedInt
import java.lang.Short as BoxedShort
import java.lang.Long as BoxedLong
import java.lang.Byte as BoxedByte
import java.lang.Float as BoxedFloat
import java.lang.Double as BoxedDouble
import java.lang.Character as BoxedChar
import java.lang.Boolean as BoxedBoolean

actual fun KClass<*>.isSubclassOf(base: KClass<*>): Boolean =
  base.isSuperclassOf(this)

actual fun KClass<*>.isSuperclassOf(derived: KClass<*>): Boolean =
  java.toBoxed().isAssignableFrom(derived.java.toBoxed())

// This is a workaround some issues with primitive types, kotlin captures
// primitive types through typeOf() and genericTypeOf(), so isSuperclassOf
// will break when that happens, this fixes that. There's no guarantee that
// the actual types are compatible.

private fun Class<*>.toBoxed(): Class<*> {
  if (!isPrimitive)
    return this
  return when (this) {
    Byte::class.java -> BoxedByte::class.java
    Short::class.java -> BoxedShort::class.java
    Char::class.java -> BoxedChar::class.java
    Int::class.java -> BoxedInt::class.java
    Long::class.java -> BoxedLong::class.java
    Float::class.java -> BoxedFloat::class.java
    Double::class.java -> BoxedDouble::class.java
    Boolean::class.java -> BoxedBoolean::class.java
    else -> this
  }
}

actual val KClass<*>.superclasses: List<KClass<*>>
  get() = supertypes.mapNotNull { it.classifier as? KClass<*> }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val KClass<*>.supertypes: List<KType>
  get() = supertypes

actual fun KClass<*>.createType(arguments: List<KTypeProjection>?, nullable: Boolean): KType =
  createKType(nullable = nullable, arguments = arguments ?: getDefaultArgumentTypes())

private fun KClass<*>.getDefaultArgumentTypes(): List<KTypeProjection> {
  val size = this.typeParameters.size
  return (0 until size).map { KTypeProjection.STAR }
}
