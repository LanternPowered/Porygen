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

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

public interface JsonDeserializationContext extends com.google.gson.JsonDeserializationContext {

    <T> void ifPresent(JsonObject obj, String key, TypeToken<T> objectType, Consumer<T> consumer);

    default <T> void ifPresent(JsonObject obj, String key, Class<T> objectType, Consumer<T> consumer) {
        ifPresent(obj, key, TypeToken.of(objectType), consumer);
    }

    <T> void ifPresent(String key, TypeToken<T> objectType, Consumer<T> consumer);

    default <T> void ifPresent(String key, Class<T> objectType, Consumer<T> consumer) {
        ifPresent(key, TypeToken.of(objectType), consumer);
    }

    void ifIntPresent(JsonObject obj, String key, IntConsumer consumer);

    void ifIntPresent(String key, IntConsumer consumer);

    void ifDoublePresent(JsonObject obj, String key, DoubleConsumer consumer);

    void ifDoublePresent(String key, DoubleConsumer consumer);

    default <T> T deserialize(JsonElement element, TypeToken<T> typeToken) {
        return deserialize(element, typeToken.getType());
    }
}
