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
import java.util.Optional
import java.util.function.BiConsumer

class OperatedPoryObject internal constructor(private val map: Map<String, OperatedPoryElement>) {

    /**
     * Gets a [Set] with all the entries of this [OperatedPoryObject].
     *
     * @return The entries
     */
    val entries = this.map.entries

    /**
     * Performs the given action for all the entries of this [OperatedPoryObject].
     *
     * @param action The action
     */
    fun forEach(action: BiConsumer<in String, in OperatedPoryElement>) {
        this.map.forEach(action)
    }

    /**
     * Gets whether the given key is present within this [OperatedPoryObject].
     *
     * @param key The key
     * @return Whether the key is present
     */
    fun has(key: String) = this.map.containsKey(key)

    /**
     * Attempts to get the [OperatedPoryElement] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The element
     */
    fun tryGet(key: String) = get(key) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAsObj(key: String, type: Type) = getAsObj<T>(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAsObj(key: String, type: TypeToken<T>) = getAsObj(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAsObj(key: String, type: Class<T>) = getAsObj(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Gets the [OperatedPoryElement] for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The element
     */
    operator fun get(key: String): OperatedPoryElement? = this.map[key]

    /**
     * Gets the object of type [T] for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The element if present, otherwise [Optional.empty]
     */
    fun <T> getAsObj(key: String, type: Type) = get(key)?.let { e -> OperatedObj<T>(e.element.asObj(type), e.operations) }

    /**
     * Gets the object of type [T] for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The object if present, otherwise [Optional.empty]
     */
    fun <T> getAsObj(key: String, type: TypeToken<T>) = get(key)?.let { e -> OperatedObj(e.element.asObj(type), e.operations) }

    /**
     * Gets the object of type [T] for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The object if present, otherwise [Optional.empty]
     */
    fun <T> getAsObj(key: String, type: Class<T>) = get(key)?.let { e -> OperatedObj(e.element.asObj(type), e.operations) }

    /**
     * Gets the int for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The operated int object
     */
    fun getAsInt(key: String) = get(key)?.let { e -> OperatedObj(e.element.asInt(), e.operations) }

    /**
     * Gets the int for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The operated double object
     */
    fun getAsDouble(key: String) = get(key)?.let { e -> OperatedObj(e.element.asDouble(), e.operations) }
}
