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
import java.util.*

internal class MapParser : PoryParser<Map<*, *>> {

    override fun parse(element: PoryElement, type: TypeToken<Map<*, *>>, ctx: PoryParserContext): Map<*, *> {
        val keyType = type.resolveType(keyTypeVariable)
        val valueType = type.resolveType(valueTypeVariable)
        val map = LinkedHashMap<Any?, Any?>() // Retain entry order, in case it's expected
        if (element.isArray) {
            element.asArray().forEach { e ->
                val entry = e.asObject()
                val key = entry.tryGetAsObj("key", keyType)
                val value = entry.tryGetAsObj("value", valueType)
                map[key] = value
            }
        } else {
            element.asObject().forEach { k, v ->
                val key = PoryPrimitive(k).asObj(keyType)
                val value = v.asObj(valueType)
                map[key] = value
            }
        }
        return map
    }

    companion object {

        private val keyTypeVariable = Map::class.java.typeParameters[0]
        private val valueTypeVariable = Map::class.java.typeParameters[1]
    }
}
