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
import org.lanternpowered.porygen.api.util.uncheckedCast
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass

/**
 * A builder to create [Pory]s.
 */
class PoryBuilder {

    internal val parserFactories = ArrayList<PoryParserFactory>()

    /**
     * Registers a [PoryParser] for the given [Class].
     *
     * @param type The type token
     * @param parser The parser
     * @param T The object type to be parsed
     * @return This object, for chaining
     */
    fun <T : Any> registerParser(type: KClass<T>, parser: PoryParser<T>): PoryBuilder {
        return registerParser(TypeToken.of(type.java), parser)
    }

    fun <T : Any> registerParser(type: KClass<T>, fn: (element: PoryElement, type: TypeToken<T>, ctx: PoryParserContext) -> T): PoryBuilder {
        return registerParser(TypeToken.of(type.java), PoryParser(fn))
    }

    fun <T : Any> registerParser(type: KClass<T>, fn: (element: PoryElement) -> T): PoryBuilder {
        return registerParser(TypeToken.of(type.java), PoryParser(fn))
    }

    inline fun <reified T : Any> registerParser(crossinline fn: (element: PoryElement, type: TypeToken<T>, ctx: PoryParserContext) -> T): PoryBuilder {
        return registerParser(TypeToken.of(T::class.java), PoryParser(fn))
    }

    inline fun <reified T : Any> registerParser(crossinline fn: (element: PoryElement) -> T): PoryBuilder {
        return registerParser(TypeToken.of(T::class.java), PoryParser(fn))
    }

    /**
     * Registers a [PoryParser] for the given [Class].
     *
     * @param type The type token
     * @param parser The parser
     * @param T The object type to be parsed
     * @return This object, for chaining
     */
    fun <T> registerParser(type: Type, parser: PoryParser<T>): PoryBuilder {
        return registerParser(TypeToken.of(type).uncheckedCast<TypeToken<T>>(), parser)
    }

    /**
     * Registers a [PoryParser] for the given [TypeToken].
     *
     * @param type The type token
     * @param parser The parser
     * @param T The object type to be parsed
     * @return This object, for chaining
     */
    fun <T> registerParser(type: TypeToken<T>, parser: PoryParser<T>): PoryBuilder {
        return registerFactory(SingleParserFactory(type, parser))
    }

    /**
     * Registers a [PoryParserFactory].
     *
     * @param factory The parser factory
     * @return This object, for chaining
     */
    fun registerFactory(factory: PoryParserFactory) = apply {
        this.parserFactories.add(factory)
    }

    /**
     * Registers a [PoryParserFactory].
     *
     * @param factory The parser factory
     * @return This object, for chaining
     */
    fun registerFactory(factory: (pory: Pory, typeToken: TypeToken<*>) -> PoryParser<*>?) = apply {
        this.parserFactories.add(PoryParserFactory(factory))
    }

    /**
     * Builds the [Pory] instance.
     *
     * @return The pory
     */
    fun build() = Pory(this)

    private class SingleParserFactory constructor(
            private val type: TypeToken<*>,
            private val parser: PoryParser<*>
    ) : PoryParserFactory {

        override fun create(pory: Pory, typeToken: TypeToken<*>): PoryParser<*>? {
            return if (!typeToken.isSubtypeOf(this.type)) {
                null
            } else {
                this.parser.uncheckedCast<PoryParser<*>>()
            }
        }
    }
}
