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
package org.lanternpowered.porygen.settings

import java.util.HashMap

enum class ListOperation {
    /**
     * Overrides all the content.
     */
    OVERRIDE {
        override fun <T> merge(a: MutableList<T>, b: MutableList<T>) {
            a.clear()
            a.addAll(b)
        }
    },
    /**
     * Adds new content.
     */
    ADD {
        override fun <T> merge(a: MutableList<T>, b: MutableList<T>) {
            a.addAll(b)
        }
    };

    abstract fun <T> merge(a: MutableList<T>, b: MutableList<T>)

    companion object {

        /**
         * Parses the [ListOperation] from the given operations collection.
         *
         * @param operations The operations
         * @return The list operation
         */
        fun parse(operations: Collection<String>): ListOperation {
            for (operation in operations) {
                val listOperation = byId[operation.toLowerCase()]
                if (listOperation != null) {
                    return listOperation
                }
            }
            return OVERRIDE
        }

        private val byId = HashMap<String, ListOperation>()

        init {
            for (operation in values()) {
                byId[operation.name.toLowerCase()] = operation
            }
        }
    }
}
