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
package org.lanternpowered.porygen.parser.types;

import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.ParseException;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;

/**
 * A parser for {@link CatalogType}.
 */
@SuppressWarnings("unchecked")
public class CatalogTypeParser<T extends CatalogType> implements PoryParser<T> {

    @Override
    public T parse(PoryElement element, TypeToken<T> type, PoryParserContext ctx) {
        final Class<T> classType = (Class<T>) type.getRawType();
        if (classType.equals(CatalogType.class)) {
            throw new ParseException("Cannot parse base catalog type: " + classType.getSimpleName());
        }
        final String id = element.asString();
        return Sponge.getRegistry().getType(classType, id)
                .orElseThrow(() -> new ParseException("Cannot find a " + classType.getSimpleName() + " with id '" + id + "'"));
    }
}
