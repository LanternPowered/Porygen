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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

public interface JsonDeserializer<D> extends com.google.gson.JsonDeserializer<D> {

    @Override
    default D deserialize(JsonElement jsonElement, Type type, com.google.gson.JsonDeserializationContext ctx) throws JsonParseException {
        final com.google.gson.JsonDeserializationContext ctx1 = ctx;
        ctx = new JsonDeserializationContext() {

            <T> void ifPresent(JsonObject obj, String key, Type objectType, Consumer<T> consumer) {
                final JsonElement element = obj.get(key);
                if (element != null) {
                    final T object = ctx1.deserialize(element, objectType);
                    consumer.accept(object);
                }
            }

            @Override
            public <T> void ifPresent(JsonObject obj, String key, Class<T> objectType, Consumer<T> consumer) {
                ifPresent(jsonElement.getAsJsonObject(), key, (Type) objectType, consumer);
            }

            @Override
            public <T> void ifPresent(JsonObject obj, String key, TypeToken<T> objectType, Consumer<T> consumer) {
                ifPresent(jsonElement.getAsJsonObject(), key, objectType.getType(), consumer);
            }

            @Override
            public <T> void ifPresent(String key, TypeToken<T> objectType, Consumer<T> consumer) {
                ifPresent(jsonElement.getAsJsonObject(), key, objectType, consumer);
            }

            @Override
            public <T> void ifPresent(String key, Class<T> objectType, Consumer<T> consumer) {
                ifPresent(jsonElement.getAsJsonObject(), key, objectType, consumer);
            }

            @Override
            public void ifIntPresent(JsonObject obj, String key, IntConsumer consumer) {
                final JsonElement element = obj.get(key);
                if (element != null) {
                    consumer.accept(element.getAsInt());
                }
            }

            @Override
            public void ifIntPresent(String key, IntConsumer consumer) {
                ifIntPresent(jsonElement.getAsJsonObject(), key, consumer);
            }

            @Override
            public void ifDoublePresent(JsonObject obj, String key, DoubleConsumer consumer) {
                final JsonElement element = obj.get(key);
                if (element != null) {
                    consumer.accept(element.getAsDouble());
                }
            }

            @Override
            public void ifDoublePresent(String key, DoubleConsumer consumer) {
                ifDoublePresent(jsonElement.getAsJsonObject(), key, consumer);
            }

            @Override
            public <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
                return ctx1.deserialize(jsonElement, type);
            }
        };
        return deserialize(jsonElement, type, (JsonDeserializationContext) ctx);
    }

    D deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException;

    default void check(boolean check, String message) {
        if (!check) {
            throw new JsonParseException(message);
        }
    }
}
