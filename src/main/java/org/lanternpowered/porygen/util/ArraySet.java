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
/*
 * Copyright (c) 2007 by L. Paul Chew.
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package org.lanternpowered.porygen.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An ArrayList implementation of Set. An ArraySet is good for small sets; it
 * has less overhead than a HashSet or a TreeSet.
 *
 * @author Paul Chew
 *
 * Created November, December 2007.  For use in Delaunay/Voronoi code.
 */
public class ArraySet<E> extends AbstractSet<E> {

    private ArrayList<E> items; // Items of the set

    /**
     * Create an empty set (default initial capacity is 3).
     */
    public ArraySet() {
        this(3);
    }

    /**
     * Create an empty set with the specified initial capacity.
     *
     * @param initialCapacity The initial capacity
     */
    public ArraySet(int initialCapacity) {
        this.items = new ArrayList<>(initialCapacity);
    }

    /**
     * Create a set containing the items of the collection. Any duplicate
     * items are discarded.
     *
     * @param collection The source for the items of the small set
     */
    public ArraySet (Collection<? extends E> collection) {
        this.items = new ArrayList<>(collection.size());
        collection.stream().filter(item -> !this.items.contains(item)).forEach(this.items::add);
    }

    /**
     * Get the item at the specified index.
     *
     * @param index The index where the item is located
     * @return The item at the specified index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    public E get(int index) throws IndexOutOfBoundsException {
        return this.items.get(index);
    }

    /**
     * True if any member of the collection is also in the ArraySet.
     *
     * @param collection The Collection to check
     * @return True if any member of collection appears
     */
    public boolean containsAny(Collection<?> collection) {
        for (Object item : collection) {
            if (contains(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E item) {
        return !this.items.contains(item) && this.items.add(item);
    }

    @Override
    public Iterator<E> iterator() {
        return this.items.iterator();
    }

    @Override
    public int size() {
        return this.items.size();
    }
}
