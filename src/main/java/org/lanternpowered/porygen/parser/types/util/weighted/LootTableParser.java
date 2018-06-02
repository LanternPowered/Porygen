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
package org.lanternpowered.porygen.parser.types.util.weighted;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.spongepowered.api.util.weighted.LootTable;
import org.spongepowered.api.util.weighted.RandomObjectTable;

import java.lang.reflect.TypeVariable;
import java.util.List;

@SuppressWarnings("unchecked")
public class LootTableParser<T> implements PoryParser<LootTable<T>> {

    private static final TypeVariable<?> objectTypeVariable = LootTable.class.getTypeParameters()[0];

    @Override
    public LootTable<T> parse(PoryElement element, TypeToken<LootTable<T>> type, PoryParserContext ctx) {
        final TypeToken<T> objectType = (TypeToken<T>) type.resolveType(objectTypeVariable);
        final TypeToken<List<RandomObjectTable<T>>> poolType =
                new TypeToken<List<RandomObjectTable<T>>>() {}.where(new TypeParameter<T>() {}, objectType);
        final List<RandomObjectTable<T>> pool;
        if (element.isArray()) {
            pool = element.as(poolType);
        } else {
            pool = element.asObject().tryGetAs("pool", poolType);
        }
        final LootTable<T> lootTable = new LootTable<>();
        pool.forEach(lootTable::addTable);
        return lootTable;
    }
}
