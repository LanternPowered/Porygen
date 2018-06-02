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
import org.lanternpowered.porygen.parser.ParseException;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryObjectParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.spongepowered.api.util.weighted.RandomObjectTable;
import org.spongepowered.api.util.weighted.TableEntry;
import org.spongepowered.api.util.weighted.VariableAmount;

import java.lang.reflect.TypeVariable;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractRandomObjectTableParser<T, O extends RandomObjectTable<T>> implements PoryObjectParser<O> {

    private static final TypeVariable<?> objectTypeVariable = RandomObjectTable.class.getTypeParameters()[0];

    @Override
    public O parse(PoryObject object, TypeToken<O> type, PoryParserContext ctx) {
        final TypeToken<T> objectType = (TypeToken<T>) type.resolveType(objectTypeVariable);
        if (objectType.getRawType().equals(Object.class)) {
            throw new ParseException("Cannot parse as Object");
        }
        final List<TableEntry<T>> tableEntries = object.tryGetAs("entries",
                new TypeToken<List<TableEntry<T>>>() {}.where(new TypeParameter<T>() {}, objectType));
        final O table = constructTable();
        table.addAll(tableEntries);
        object.getAs("rolls", VariableAmount.class)
                .ifPresent(table::setRolls);
        return table;
    }

    protected abstract O constructTable();
}
