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
package org.lanternpowered.porygen.settings.json.weighted;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.lanternpowered.porygen.settings.json.JsonDeserializationContext;
import org.lanternpowered.porygen.settings.json.JsonDeserializer;
import org.spongepowered.api.util.weighted.RandomObjectTable;
import org.spongepowered.api.util.weighted.TableEntry;
import org.spongepowered.api.util.weighted.VariableAmount;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractRandomObjectTableParser<T, O extends RandomObjectTable<T>> implements JsonDeserializer<O> {

    private static final TypeVariable<?> objectTypeVariable = RandomObjectTable.class.getTypeParameters()[0];

    @Override
    public O deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        final TypeToken<?> typeToken = TypeToken.of(type);
        final TypeToken<T> objectType = (TypeToken<T>) typeToken.resolveType(objectTypeVariable);
        if (objectType.getRawType().equals(Object.class)) {
            throw new JsonParseException("Cannot parse as Object");
        }
        final JsonObject obj = element.getAsJsonObject();
        final List<TableEntry<T>> tableEntries = ctx.deserialize(obj.get("entries"),
                new TypeToken<List<TableEntry<T>>>() {}.where(new TypeParameter<T>() {}, objectType));
        final O table = constructTable();
        table.addAll(tableEntries);
        if (obj.has("rolls")) {
            table.setRolls(ctx.deserialize(obj.get("rolls"), VariableAmount.class));
        }
        return table;
    }

    protected abstract O constructTable();
}
