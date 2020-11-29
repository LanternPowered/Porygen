/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.porygen.util.type

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.typeOf

/**
 * Instantiates a [GenericType] for the given type [T].
 */
inline fun <reified T> genericTypeOf(): GenericType<T> =
  GenericType(typeOf<T>())

/**
 * Gets the [GenericType] as a not nullable version.
 */
fun <T : Any> GenericType<T?>.notNullable(): GenericType<T> {
  val kClass = classifier as? KClass<*> ?: throw UnsupportedOperationException()
  return GenericType(kClass.createType(arguments, nullable = false))
}

/**
 * Gets the [GenericType] as a nullable version.
 */
fun <T : Any> GenericType<T>.nullable(): GenericType<T?> {
  val kClass = classifier as? KClass<*> ?: throw UnsupportedOperationException()
  return GenericType(kClass.createType(arguments, nullable = true))
}

/**
 * Instantiates a [GenericType] for the given [KClass].
 */
fun <T : Any> GenericType(kClass: KClass<T>): GenericType<T> =
  GenericType(kClass.createType(nullable = false)) // TODO: Type parameters?

/**
 * Represents a [KType] with a generic type parameter
 * representing the type.
 *
 * @property T The generic type of the [KType]
 * @property kType The backing [KType]
 */
data class GenericType<T>(val kType: KType) {

  // TODO: Make this an inline class when JvmName can be applied to functions
  //   that have inline class parameters

  /**
   * The declaration of the classifier used in this type.
   */
  val classifier: KClassifier?
    get() = kType.classifier

  /**
   * The type arguments of the generic type.
   */
  val arguments: List<KTypeProjection>
    get() = kType.arguments

  /**
   * Whether this type is nullable.
   */
  val isNullable: Boolean
    get() = kType.isMarkedNullable
}
