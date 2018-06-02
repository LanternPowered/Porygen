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

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.Map;

final class OperatedPoryObjectParser implements PoryObjectParser<OperatedPoryObject> {

    @Override
    public OperatedPoryObject parse(PoryObject object, TypeToken<OperatedPoryObject> type, PoryParserContext ctx) {
        // Preserve the map entry order
        final Map<String, OperatedPoryElement> map = new LinkedHashMap<>();
        object.forEach((parsedKey, value) -> {
            final ImmutableList.Builder<String> operations = ImmutableList.builder();
            int startIndex;
            while ((startIndex = parsedKey.indexOf('@')) != -1) {
                int endIndex = parsedKey.indexOf(' ', startIndex);
                if (endIndex == -1) {
                    endIndex = parsedKey.length();
                }
                operations.add(parsedKey.substring(startIndex + 1, endIndex));
                parsedKey = (parsedKey.substring(0, startIndex) + parsedKey.substring(endIndex)).trim();
            }
            map.put(parsedKey, new OperatedPoryElement(value, operations.build()));
        });
        return new OperatedPoryObject(map);
    }
}
