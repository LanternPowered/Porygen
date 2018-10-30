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
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.streams.toList

class PoryArray internal constructor(private val elements: MutableList<PoryElement>, ignored: Int) : PoryElement(), Iterable<PoryElement> {

    /**
     * Constructs a new [PoryObject].
     */
    constructor() : this(ArrayList<PoryElement>(), 0)

    /**
     * Constructs a new [PoryArray].
     *
     * @param elements The elements
     */
    constructor(elements: MutableList<PoryElement>) : this(elements, 0)

    override fun iterator() = this.elements.iterator()

    /**
     * Adds the [PoryElement] to this [PoryArray].
     *
     * @param element The element
     */
    fun add(element: PoryElement) {
        this.elements.add(element)
    }

    /**
     * Gets the size of this [PoryArray].
     *
     * @return The size
     */
    val size = this.elements.size

    /**
     * Gets the [PoryElement] at the given index.
     *
     * @param i The index
     * @return The element
     */
    operator fun get(i: Int) = this.elements[i]

    /**
     * Parses all the elements within this [PoryArray]
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    fun <T> mapTo(objectType: Type): List<T> {
        val ctx = PoryParserContext.current()
        return this.elements.stream().map { e -> ctx.parse<T>(e, objectType) }.toList()
    }

    /**
     * Parses all the elements within this [PoryArray]
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    fun <T> mapTo(objectType: TypeToken<T>): List<T> {
        val ctx = PoryParserContext.current()
        return this.elements.stream().map { e -> ctx.parse(e, objectType) }.toList()
    }

    /**
     * Parses all the elements within this [PoryArray]
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    fun <T> mapTo(objectType: Class<T>): List<T> {
        val ctx = PoryParserContext.current()
        return this.elements.stream().map { e -> ctx.parse(e, objectType) }.toList()
    }

    /**
     * Gets a [Stream] for this array.
     *
     * @return The stream
     */
    fun stream(): Stream<PoryElement> = StreamSupport.stream(spliterator(), false)
}
