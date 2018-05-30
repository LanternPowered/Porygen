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
package org.lanternpowered.porygen.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ListOperation {
    /**
     * Overrides all the content.
     */
    OVERRIDE {
        @Override
        public <T> void merge(List<T> a, List<T> b) {
            a.clear();
            a.addAll(b);
        }
    },
    /**
     * Adds new content.
     */
    ADD {
        @Override
        public <T> void merge(List<T> a, List<T> b) {
            a.addAll(b);
        }
    },
    ;

    public abstract <T> void merge(List<T> a, List<T> b);

    /**
     * Parses the {@link ListOperation} from the given operations collection.
     *
     * @param operations The operations
     * @return The list operation
     */
    public static ListOperation parse(Collection<String> operations) {
        for (String operation : operations) {
            final ListOperation listOperation = byId.get(operation.toLowerCase());
            if (listOperation != null) {
                return listOperation;
            }
        }
        return OVERRIDE;
    }

    private static final Map<String, ListOperation> byId = new HashMap<>();

    static {
        for (ListOperation operation : values()) {
            byId.put(operation.name().toLowerCase(), operation);
        }
    }
}
