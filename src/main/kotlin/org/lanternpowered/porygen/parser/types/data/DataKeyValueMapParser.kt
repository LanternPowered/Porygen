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
package org.lanternpowered.porygen.parser.types.data

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.PoryElement
import org.lanternpowered.porygen.parser.PoryParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.parser.PoryPrimitive
import org.lanternpowered.porygen.settings.DataKeyValueMap
import org.spongepowered.api.data.key.Key
import java.util.*

class DataKeyValueMapParser : PoryParser<DataKeyValueMap> {

    override fun parse(element: PoryElement, type: TypeToken<DataKeyValueMap>, ctx: PoryParserContext): DataKeyValueMap {
        val map = HashMap<Key<*>, Any>()
        if (element.isArray) {
            for (arrayElement in element.asArray()) {
                val obj = arrayElement.asObject()
                val key = obj.tryGetAs<Key<*>>("key")
                val value = obj.tryGetAs("value", key.elementToken)
                map[key] = value
            }
        } else {
            element.asObject().forEach { k, v ->
                val key = PoryPrimitive(k).asObj<Key<*>>()
                val value = v.asObj(key.elementToken)
                map[key] = value
            }
        }
        return DataKeyValueMap(map)
    }
}
