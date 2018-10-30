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
import org.lanternpowered.porygen.util.uncheckedCast
import java.util.*

internal class EnumParser<T : Enum<*>>(private val enumClass: Class<T>) : PoryParser<T> {

    private val mappings = HashMap<String, T>()

    init {
        for (value in this.enumClass.enumConstants) {
            this.mappings[value.name.toLowerCase()] = value
        }
    }

    override fun parse(element: PoryElement, type: TypeToken<T>, ctx: PoryParserContext): T {
        val value = element.asString()
        return this.mappings[value.toLowerCase()]
                ?: throw ParseException("No enum value of type '${this.enumClass.name}' with name: $value")
    }
}

internal class EnumParserFactory : PoryParserFactory {

    override fun create(pory: Pory, typeToken: TypeToken<*>): PoryParser<*>? {
        if (!typeToken.isSubtypeOf(Enum::class.java)) {
            return null
        }
        return EnumParser<Enum<*>>(typeToken.rawType.uncheckedCast())
    }
}
