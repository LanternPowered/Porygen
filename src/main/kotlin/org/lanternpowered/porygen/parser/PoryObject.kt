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
import java.util.*
import java.util.function.BiConsumer

class PoryObject private constructor(private val map: MutableMap<String, PoryElement>, ignored: Int) : PoryElement() {

    /**
     * Constructs a new [PoryObject].
     */
    constructor() : this(LinkedHashMap<String, PoryElement>(), 0)

    /**
     * Constructs a new [PoryObject].
     *
     * @param map The values
     */
    constructor(map: Map<String, PoryElement>) : this(LinkedHashMap<String, PoryElement>(map), 0)

    /**
     * Puts the [PoryElement] in this [PoryObject].
     *
     * @param key The key
     * @param element The element
     */
    fun put(key: String, element: PoryElement) {
        this.map[key] = element
    }

    /**
     * Gets a [Set] with all the entries of this [PoryObject].
     *
     * @return The entries
     */
    fun entries(): Set<Map.Entry<String, PoryElement>> {
        return this.map.entries
    }

    /**
     * Performs the given action for all the entries of this [OperatedPoryObject].
     *
     * @param action The action
     */
    fun forEach(action: BiConsumer<in String, in PoryElement>) {
        this.map.forEach(action)
    }

    /**
     * Performs the given action for all the entries of this [OperatedPoryObject].
     *
     * @param action The action
     */
    fun forEach(action: (String, PoryElement) -> Unit) {
        this.map.forEach(action)
    }

    /**
     * Gets whether the given key is present within this [PoryObject].
     *
     * @param key The key
     * @return Whether the key is present
     */
    fun has(key: String) = this.map.containsKey(key)

    /**
     * Attempts to get the [PoryElement] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The element
     */
    fun tryGet(key: String): PoryElement = get(key) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAs(key: String, type: Type): T = getAsObj<T>(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAs(key: String, type: TypeToken<T>): T = getAsObj(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    fun <T> tryGetAs(key: String, type: Class<T>): T = getAsObj(key, type) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Attempts to get the object of type [T] for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    inline fun <reified T> tryGetAs(key: String): T = getAsObj(key, object : TypeToken<T>() {}) ?: throw IllegalStateException("Missing key: $key")

    /**
     * Gets the [OperatedPoryElement] for the given key, or [Optional.empty] if missing.
     *
     * @param key The key
     * @return The element
     */
    operator fun get(key: String): PoryElement? = this.map[key]

    /**
     * Gets the object of type [T] for the given key, or null if missing.
     *
     * @param key The key
     * @return The object if present, otherwise null
     */
    fun <T> getAsObj(key: String, type: Type): T? = get(key)?.asObj(type)

    /**
     * Gets the object of type [T] for the given key, or null if missing.
     *
     * @param key The key
     * @return The object if present, otherwise null
     */
    fun <T> getAsObj(key: String, type: TypeToken<T>) = get(key)?.asObj(type)

    /**
     * Gets the object of type [T] for the given key, or null if missing.
     *
     * @param key The key
     * @return The object if present, otherwise null
     */
    fun <T> getAsObj(key: String, type: Class<T>) = get(key)?.asObj(type)

    /**
     * Gets the object of type [T] for the given key, or null if missing.
     *
     * @param key The key
     * @return The object if present, otherwise null
     */
    inline fun <reified T> getAsObj(key: String) = getAsObj(key, object : TypeToken<T>() {})

    /**
     * Gets the [PoryObject] for the given key, or null if missing.
     *
     * @param key The key
     * @return The object if present, otherwise null
     */
    fun getAsObject(key: String) = get(key)?.asObject()

    /**
     * Gets the int value for the given key, or null if missing.
     *
     * @param key The key
     * @return The int value if present, otherwise null
     */
    fun getAsInt(key: String) = get(key)?.asInt()

    /**
     * Gets the double value for the given key, or null if missing.
     *
     * @param key The key
     * @return The double value if present, otherwise null
     */
    fun getAsDouble(key: String) = get(key)?.asDouble()
}
