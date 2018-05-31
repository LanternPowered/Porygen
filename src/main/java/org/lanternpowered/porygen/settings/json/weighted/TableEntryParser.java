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
import org.spongepowered.api.util.weighted.EmptyObject;
import org.spongepowered.api.util.weighted.NestedTableEntry;
import org.spongepowered.api.util.weighted.RandomObjectTable;
import org.spongepowered.api.util.weighted.TableEntry;
import org.spongepowered.api.util.weighted.WeightedObject;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@SuppressWarnings("unchecked")
public class TableEntryParser<T> implements JsonDeserializer<TableEntry<T>> {

    private static final TypeVariable<?> objectTypeVariable = TableEntry.class.getTypeParameters()[0];

    @Override
    public TableEntry<T> deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        final TypeToken<?> typeToken = TypeToken.of(type);
        final TypeToken<T> objectType = (TypeToken<T>) typeToken.resolveType(objectTypeVariable);
        if (objectType.getRawType().equals(Object.class)) {
            throw new JsonParseException("Cannot parse as Object");
        }
        // Nested table entry
        if (element.getAsJsonObject().has("table")) {
            final JsonObject obj = element.getAsJsonObject();
            final RandomObjectTable<T> table = ctx.deserialize(obj.get("table"),
                    new TypeToken<RandomObjectTable<T>>() {}.where(new TypeParameter<T>() {}, objectType));
            final double weight = obj.get("weight").getAsDouble();
            return new NestedTableEntry<>(weight, table);
        }
        final JsonObject obj = element.getAsJsonObject();
        final double weight = obj.get("weight").getAsDouble();
        // Empty table entry
        if (obj.has("object")) {
            return new EmptyObject<>(weight);
        }
        final T object = ctx.deserialize(obj.get("object"), objectType);
        return new WeightedObject<>(object, weight);
    }
}
