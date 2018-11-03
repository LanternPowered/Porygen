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
package org.lanternpowered.porygen.api.data

/**
 * Represents something that can hold data.
 */
interface DataHolder {

    /**
     * Gets the data attached for the given key.
     *
     * @param key The key
     * @param <T> The object type
     * @return The value if present, otherwise null
     */
    operator fun <T> get(key: DataKey<T>): T?

    /**
     * Sets the data attached for the given key.
     *
     * @param key The key
     * @param value The value
     * @param T The value type
     */
    operator fun <T> set(key: DataKey<T>, value: T)

    /**
     * Removes the data attached for the given key.
     *
     * @param key The key
     * @param T The value type
     * @return The removed value, or null if not present
     */
    fun <T> remove(key: DataKey<T>): T?
}
