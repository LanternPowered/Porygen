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

import com.google.common.base.Preconditions.checkState
import com.google.common.primitives.Primitives
import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.typesafe.config.ConfigList
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueType
import org.lanternpowered.porygen.api.util.uncheckedCast
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors
import java.util.stream.StreamSupport

/**
 * A parsing system inspired by gson, but more flexible in
 * the way of functional programming.
 */
class Pory internal constructor(builder: PoryBuilder) {

    // Parser order matters
    private val parserFactories: List<PoryParserFactory>
    // Cached parsers
    private val parsers = ConcurrentHashMap<TypeToken<*>, PoryParser<*>?>()
    // The context that is used within parsers
    private val context = object : PoryParserContext {
        override fun <T> parse(element: PoryElement, objectType: Type): T {
            return parse0(element, TypeToken.of(objectType).uncheckedCast())
        }

        override fun <T> parse(element: PoryElement, objectType: Class<T>): T {
            return parse0(element, TypeToken.of(objectType))
        }

        override fun <T> parse(element: PoryElement, objectType: TypeToken<T>): T {
            return parse0(element, objectType)
        }
    }

    init {
        val copy = PoryBuilder()
        copy.parserFactories.addAll(builder.parserFactories)
        // Add the default parsers
        copy.registerParser(OperatedPoryObject::class.java, OperatedPoryObjectParser())
        copy.registerParser { element -> element.asBoolean() }
        copy.registerParser { element -> element.asByte() }
        copy.registerParser { element -> element.asChar() }
        copy.registerParser { element -> element.asDouble() }
        copy.registerParser { element -> element.asFloat() }
        copy.registerParser { element -> element.asInt() }
        copy.registerParser { element -> element.asLong() }
        copy.registerParser { element -> element.asShort() }
        copy.registerParser { element -> element.asString() }
        copy.registerParser { element -> element.asNumber() }
        copy.registerParser(Optional::class, OptionalParser())
        copy.registerParser(List::class, ListParser())
        copy.registerParser(Set::class, SetParser())
        copy.registerParser(Map::class, MapParser())
        copy.registerFactory(EnumParserFactory())
        copy.registerFactory(ArrayParserFactory())
        this.parserFactories = copy.parserFactories
    }

    fun <T> tryGetParser(objectType: TypeToken<T>): PoryParser<T> {
        return getParser(objectType) ?: throw IllegalStateException("Unable to find a parser for the object type: $objectType")
    }

    fun <T> getParser(objectType: TypeToken<T>): PoryParser<T>? {
        var type = objectType
        // Wrap primitive types
        if (type.isPrimitive) {
            type = TypeToken.of(Primitives.wrap(type.rawType).uncheckedCast<Class<T>>())
        }
        val parser = this.parsers.computeIfAbsent(type) {
            for (factory in this.parserFactories) {
                val factoryParser = factory.create(this, type)
                if (factoryParser != null) {
                    return@computeIfAbsent factoryParser
                }
            }
            null
        }
        return parser.uncheckedCast()
    }

    fun <T> parse(element: PoryElement, objectType: Type): T? {
        return parse(element, TypeToken.of(objectType).uncheckedCast<TypeToken<T>>())
    }

    fun <T> parse(element: PoryElement, objectType: Class<T>): T? {
        return parse(element, TypeToken.of(objectType))
    }

    fun <T> parse(element: PoryElement, objectType: TypeToken<T>): T? {
        val lastContext = currentContext.get()
        try {
            // Update the current context if it changed
            if (this.context !== lastContext) {
                currentContext.set(this.context)
            }
            return parse0(element, objectType)
        } finally {
            if (this.context !== lastContext) { // Reset back to the previous context
                currentContext.set(lastContext)
            }
        }
    }

    private fun <T> parse0(element: PoryElement, objectType: TypeToken<T>): T {
        TODO()
    }

    companion object {

        /**
         * The current [PoryParserContext] stack.
         */
        private val currentContext = ThreadLocal<PoryParserContext>()

        /**
         * Gets the current [PoryParserContext], or
         * throws [IllegalStateException] if not present.
         *
         * @return The current context
         */
        internal fun currentContext(): PoryParserContext {
            val context = currentContext.get()
            checkState(context != null, "There is not active context")
            return context
        }

        /**
         * Converts the given [JsonElement] into a [PoryElement].
         *
         * @param element The json element
         * @return The pory element
         */
        fun fromJson(element: JsonElement): PoryElement {
            when {
                element.isJsonPrimitive -> {
                    val primitive = element.asJsonPrimitive
                    when {
                        primitive.isString -> return PoryPrimitive(primitive.asString)
                        primitive.isBoolean -> return PoryPrimitive(primitive.asBoolean)
                        primitive.isNumber -> return PoryPrimitive(primitive.asNumber)
                    }
                }
                element.isJsonArray -> // Guava isn't up to date with sponge?
                    return PoryArray(StreamSupport.stream(element.asJsonArray.spliterator(), false)
                            .map { fromJson(it) }
                            .collect(Collectors.toList()))
                element.isJsonObject -> {
                    val obj = PoryObject()
                    element.asJsonObject.entrySet().forEach { e -> obj.put(e.key, fromJson(e.value)) }
                    return obj
                }
                element.isJsonNull -> return PoryNull
            }
            throw IllegalStateException()
        }

        /**
         * Converts the given [ConfigValue] into a [PoryElement].
         *
         * @param configValue The config value
         * @return The pory element
         */
        fun fromConfigValue(configValue: ConfigValue): PoryElement {
            val valueType = configValue.valueType()
            return when (valueType) {
                ConfigValueType.OBJECT -> {
                    val configObject = configValue as ConfigObject
                    val obj = PoryObject()
                    configObject.forEach { key, value -> obj.put(key, fromConfigValue(value)) }
                    obj
                }
                ConfigValueType.LIST -> {
                    val configList = configValue as ConfigList
                    PoryArray(configList.stream()
                            .map { fromConfigValue(it) }
                            .collect(Collectors.toList()))
                }
                ConfigValueType.NULL -> PoryNull
                else -> PoryPrimitive(configValue.unwrapped())
            }
        }
    }
}
