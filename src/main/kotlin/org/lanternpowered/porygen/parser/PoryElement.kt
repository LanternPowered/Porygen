/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen.parser

import com.google.common.reflect.TypeToken

import java.lang.reflect.Type

abstract class PoryElement {

    val isObject get() = this is PoryObject

    val isArray get() = this is PoryArray

    val isPrimitive get() = this is PoryPrimitive

    val isNull get() = this is PoryNull

    /**
     * Gets this element as a [PoryObject].
     *
     * @return The object
     */
    fun asObject() = this as PoryObject

    /**
     * Gets this element as a [PoryArray].
     *
     * @return The array
     */
    fun asArray() = this as PoryArray

    /**
     * Gets this element as a [PoryPrimitive].
     *
     * @return The primitive
     */
    fun asPrimitive() = this as PoryPrimitive

    /**
     * Gets this element as a [Number] value.
     *
     * @return The number
     */
    open fun asNumber(): Number = throw ParseException("Cannot convert ${javaClass.simpleName} to number")

    /**
     * Gets this element as a int value.
     *
     * @return The int
     */
    open fun asInt(): Int = throw ParseException("Cannot convert ${javaClass.simpleName} to int")

    /**
     * Gets this element as a double value.
     *
     * @return The double
     */
    open fun asDouble(): Double = throw ParseException("Cannot convert ${javaClass.simpleName} to double")

    /**
     * Gets this element as a float value.
     *
     * @return The float
     */
    open fun asFloat(): Float = throw ParseException("Cannot convert ${javaClass.simpleName} to float")

    /**
     * Gets this element as a long value.
     *
     * @return The long
     */
    open fun asLong(): Long = throw ParseException("Cannot convert ${javaClass.simpleName} to long")

    /**
     * Gets this element as a string value.
     *
     * @return The string
     */
    open fun asString(): String = throw ParseException("Cannot convert ${javaClass.simpleName} to string")

    /**
     * Gets this element as a short value.
     *
     * @return The short
     */
    open fun asShort(): Short = throw ParseException("Cannot convert ${javaClass.simpleName} to short")

    /**
     * Gets this element as a byte value.
     *
     * @return The byte
     */
    open fun asByte(): Byte = throw ParseException("Cannot convert ${javaClass.simpleName} to byte")

    /**
     * Gets this element as a boolean value.
     *
     * @return The boolean
     */
    open fun asBoolean(): Boolean = throw ParseException("Cannot convert ${javaClass.simpleName} to boolean")

    /**
     * Gets this element as a char value.
     *
     * @return The char
     */
    fun asChar(): Char = throw ParseException("Cannot convert ${javaClass.simpleName} to char")

    /**
     * Parses this element as the given [Type]
     * through the current [PoryParserContext].
     *
     * @param objectType The object type
     * @param T The object type
     * @return The parsed object
     */
    fun <T> asObj(objectType: Type): T = PoryParserContext.current().parse(this, objectType)

    /**
     * Parses this element as the given [Class]
     * through the current [PoryParserContext].
     *
     * @param objectType The object type class
     * @param T The object type
     * @return The parsed object
     */
    fun <T> asObj(objectType: Class<T>): T = PoryParserContext.current().parse(this, objectType)

    /**
     * Parses this element as the given [TypeToken]
     * through the current [PoryParserContext].
     *
     * @param objectType The object type token
     * @param T The object type
     * @return The parsed object
     */
    fun <T> asObj(objectType: TypeToken<T>): T = PoryParserContext.current().parse(this, objectType)

    /**
     * Parses this element as the given type [T]
     * through the current [PoryParserContext].
     *
     * @param T The object type
     * @return The parsed object
     */
    inline fun <reified T> asObj() = asObj(object : TypeToken<T>() {})
}
