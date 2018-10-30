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
package org.lanternpowered.porygen.parser.types.util.weighted

import com.google.common.reflect.TypeParameter
import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.PoryElement
import org.lanternpowered.porygen.parser.PoryParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.util.uncheckedCast
import org.spongepowered.api.util.weighted.LootTable
import org.spongepowered.api.util.weighted.RandomObjectTable

class LootTableParser<T> : PoryParser<LootTable<T>> {

    override fun parse(element: PoryElement, type: TypeToken<LootTable<T>>, ctx: PoryParserContext): LootTable<T> {
        val objectType = type.resolveType(objectTypeVariable).uncheckedCast<TypeToken<T>>()
        val poolType = object : TypeToken<List<RandomObjectTable<T>>>() {}
                .where(object : TypeParameter<T>() {}, objectType)
        val pool = if (element.isArray) {
            element.asObj(poolType)
        } else {
            element.asObject().tryGetAs("pool", poolType)
        }
        val lootTable = LootTable<T>()
        pool.forEach(lootTable::addTable)
        return lootTable
    }

    companion object {

        private val objectTypeVariable = LootTable::class.java.typeParameters[0]
    }
}
