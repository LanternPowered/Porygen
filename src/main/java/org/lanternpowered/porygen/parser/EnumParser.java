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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
final class EnumParser<T extends Enum<T>> implements PoryParser<T> {

    private final Map<Class<?>, Map<String, T>> mappings = new ConcurrentHashMap<>();

    @Override
    public T parse(PoryElement element, TypeToken<T> type, PoryParserContext ctx) {
        final Class<T> enumClass = (Class<T>) type.getRawType();
        final Map<String, T> mappings = this.mappings.computeIfAbsent(enumClass, enumClass1 -> {
            final Map<String, T> map = new HashMap<>();
            for (T value : enumClass.getEnumConstants()) {
                map.put(value.name().toLowerCase(), value);
            }
            return map;
        });
        final T value = mappings.get(element.asString().toLowerCase());
        if (value == null) {
            throw new ParseException("No enum value of type '" + enumClass.getName() + "' with name: " + element.asString());
        }
        return value;
    }
}
