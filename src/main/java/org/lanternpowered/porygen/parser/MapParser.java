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
package org.lanternpowered.porygen.parser;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.TypeVariable;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
final class MapParser<K, V> implements PoryParser<Map<K, V>> {

    private static final TypeVariable<?> keyTypeVariable = Map.class.getTypeParameters()[0];
    private static final TypeVariable<?> valueTypeVariable = Map.class.getTypeParameters()[1];

    @Override
    public Map<K, V> parse(PoryElement element, TypeToken<Map<K, V>> type, PoryParserContext ctx) {
        final TypeToken<K> keyType = (TypeToken<K>) type.resolveType(keyTypeVariable);
        final TypeToken<V> valueType = (TypeToken<V>) type.resolveType(valueTypeVariable);
        final Map<K, V> map = new LinkedHashMap<>(); // Retain entry order, in case it's expected
        if (element.isArray()) {
            element.asArray().forEach(e -> {
                final PoryObject entry = e.asObject();
                final K key = entry.tryGetAs("key", keyType);
                final V value = entry.tryGetAs("value", valueType);
                map.put(key, value);
            });
        } else {
            element.asObject().forEach((k, v) -> {
                final K key = new PoryPrimitive(k).as(keyType);
                final V value = v.as(valueType);
                map.put(key, value);
            });
        }
        return map;
    }
}
