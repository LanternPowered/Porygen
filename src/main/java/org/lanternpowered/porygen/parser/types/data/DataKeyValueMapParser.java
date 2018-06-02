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
package org.lanternpowered.porygen.parser.types.data;

import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.lanternpowered.porygen.parser.PoryPrimitive;
import org.lanternpowered.porygen.settings.DataKeyValueMap;
import org.spongepowered.api.data.key.Key;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DataKeyValueMapParser implements PoryParser<DataKeyValueMap> {

    @Override
    public DataKeyValueMap parse(PoryElement element, TypeToken<DataKeyValueMap> type, PoryParserContext ctx) {
        final Map<Key, Object> map = new HashMap<>();
        if (element.isArray()) {
            for (PoryElement arrayElement : element.asArray()) {
                final PoryObject obj = arrayElement.asObject();
                final Key<?> key = obj.tryGetAs("key", Key.class);
                final Object value = obj.tryGetAs("value", key.getElementToken());
                map.put(key, value);
            }
        } else {
            element.asObject().forEach((k, v) -> {
                final Key<?> key = new PoryPrimitive(k).as(Key.class);
                final Object value = v.as(key.getElementToken());
                map.put(key, value);
            });
        }
        return new DataKeyValueMap(map);
    }
}
