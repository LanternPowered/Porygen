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
package org.lanternpowered.porygen.settings.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.spongepowered.api.util.weighted.WeightedTable;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class WeightedTableParser<T> implements JsonDeserializer<WeightedTable<T>> {

    private static final TypeVariable<?> objectTypeVariable = WeightedTable.class.getTypeParameters()[0];

    @SuppressWarnings("unchecked")
    @Override
    public WeightedTable<T> deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        final TypeToken<?> typeToken = TypeToken.of(type);
        final TypeToken<?> objectType = typeToken.resolveType(objectTypeVariable);
        final Class<T> rawObjectType = (Class<T>) objectType.getRawType();
        if (rawObjectType.equals(Object.class)) {
            throw new JsonParseException("Cannot parse as Object");
        }
        final WeightedTable<T> table = new WeightedTable<>();
        if (element.isJsonArray()) { // Multiple weighted objects
            final JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                final JsonObject obj = array.get(i).getAsJsonObject();
                final double weight = obj.get("weight").getAsDouble();
                final T object = ctx.deserialize(obj.get("object"), objectType.getType());
                table.add(object, weight);
            }
        } else { // A single object, without a weight
            final T obj = ctx.deserialize(element, objectType.getType());
            table.add(obj, 100);
        }
        return table;
    }
}
