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
package org.lanternpowered.porygen.parser.types

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.ParseException
import org.lanternpowered.porygen.parser.PoryElement
import org.lanternpowered.porygen.parser.PoryParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.api.util.uncheckedCast
import org.spongepowered.api.CatalogType
import org.spongepowered.api.Sponge

/**
 * A parser for [CatalogType].
 */
class CatalogTypeParser<T : CatalogType> : PoryParser<T> {

    override fun parse(element: PoryElement, type: TypeToken<T>, ctx: PoryParserContext): T {
        val classType = type.rawType.uncheckedCast<Class<T>>()
        if (classType == CatalogType::class.java) {
            throw ParseException("Cannot parse base catalog type: ${classType.simpleName}")
        }
        val id = element.asString()
        return Sponge.getRegistry().getType(classType, id)
                .orElseThrow { ParseException("Cannot find a ${classType.simpleName} with id '$id'") }
    }
}
