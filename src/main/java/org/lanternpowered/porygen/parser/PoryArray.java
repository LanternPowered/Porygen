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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class PoryArray extends PoryElement implements Iterable<PoryElement> {

    private final List<PoryElement> elements;

    /**
     * Constructs a new {@link PoryObject}.
     */
    public PoryArray() {
        this(new ArrayList<>(), 0);
    }

    /**
     * Constructs a new {@link PoryArray}.
     *
     * @param elements The elements
     */
    public PoryArray(List<PoryElement> elements) {
        this(elements, 0);
    }

    PoryArray(List<PoryElement> elements, int ignored) {
        this.elements = elements;
    }

    @Override
    public Iterator<PoryElement> iterator() {
        return this.elements.iterator();
    }

    /**
     * Adds the {@link PoryElement} to this {@link PoryArray}.
     *
     * @param element The element
     */
    public void add(PoryElement element) {
        this.elements.add(element);
    }

    /**
     * Gets the size of this {@link PoryArray}.
     *
     * @return The size
     */
    public int size() {
        return this.elements.size();
    }

    /**
     * Gets the {@link PoryElement} at the given index.
     *
     * @param i The index
     * @return The element
     */
    public PoryElement get(int i) {
        return this.elements.get(i);
    }

    /**
     * Parses all the elements within this {@link PoryArray}
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    public <T> List<T> mapTo(Type objectType) {
        final PoryParserContext ctx = PoryParserContext.current();
        return this.elements.stream().map(e -> ctx.<T>parse(e, objectType)).collect(Collectors.toList());
    }

    /**
     * Parses all the elements within this {@link PoryArray}
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    public <T> List<T> mapTo(TypeToken<T> objectType) {
        final PoryParserContext ctx = PoryParserContext.current();
        return this.elements.stream().map(e -> ctx.parse(e, objectType)).collect(Collectors.toList());
    }

    /**
     * Parses all the elements within this {@link PoryArray}
     * to the given object type.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The objects
     */
    public <T> List<T> mapTo(Class<T> objectType) {
        final PoryParserContext ctx = PoryParserContext.current();
        return this.elements.stream().map(e -> ctx.parse(e, objectType)).collect(Collectors.toList());
    }

    /**
     * Gets a {@link Stream} for this array.
     *
     * @return The stream
     */
    public Stream<PoryElement> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
