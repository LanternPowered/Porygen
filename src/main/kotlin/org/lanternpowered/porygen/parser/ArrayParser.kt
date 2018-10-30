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

internal class ArrayParser<T>(private val componentType: TypeToken<T>) : PoryParser<Array<T>> {

    override fun parse(element: PoryElement, type: TypeToken<Array<T>>, ctx: PoryParserContext): Array<T> {
        return element.asArray().stream()
                .map { e -> e.asObj(this.componentType) }
                .toArray<T> { i -> java.lang.reflect.Array.newInstance(this.componentType.rawType, i).uncheckedCast<Array<T>>() }
    }
}

internal class ArrayParserFactory : PoryParserFactory {

    override fun create(pory: Pory, typeToken: TypeToken<*>): PoryParser<*>? {
        return if (!typeToken.isArray) {
            null
        } else {
            val componentType = checkNotNull(typeToken.componentType) { "Expected a array type token" }
            ArrayParser<Any?>(componentType.uncheckedCast())
        }
    }
}
