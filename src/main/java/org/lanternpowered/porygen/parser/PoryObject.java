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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

public final class PoryObject extends PoryElement {

    private final Map<String, PoryElement> map;

    /**
     * Constructs a new {@link PoryObject}.
     */
    public PoryObject() {
        this(new LinkedHashMap<>(), 0);
    }

    /**
     * Constructs a new {@link PoryObject}.
     *
     * @param map The values
     */
    public PoryObject(Map<String, PoryElement> map) {
        this(new LinkedHashMap<>(map), 0);
    }

    PoryObject(Map<String, PoryElement> map, int ignored) {
        this.map = map;
    }

    /**
     * Puts the {@link PoryElement} in this {@link PoryObject}.
     *
     * @param key The key
     * @param element The element
     */
    public void put(String key, PoryElement element) {
        checkNotNull(key, "key");
        checkNotNull(element, "element");
        this.map.put(key, element);
    }

    /**
     * Gets a {@link Set} with all the entries of this {@link PoryObject}.
     *
     * @return The entries
     */
    public Set<Map.Entry<String, PoryElement>> entries() {
        return this.map.entrySet();
    }

    /**
     * Performs the given action for all the entries of this {@link OperatedPoryObject}.
     *
     * @param action The action
     */
    public void forEach(BiConsumer<? super String, ? super PoryElement> action) {
        this.map.forEach(action);
    }

    /**
     * Gets whether the given key is present within this {@link PoryObject}.
     *
     * @param key The key
     * @return Whether the key is present
     */
    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    /**
     * Attempts to get the {@link PoryElement} for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The element
     */
    public PoryElement tryGet(String key) {
        return get(key).orElseThrow(() -> new IllegalStateException("Missing key: " + key));
    }

    /**
     * Attempts to get the object of type {@link T} for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    public <T> T tryGetAs(String key, Type type) {
        return this.<T>getAs(key, type).orElseThrow(() -> new IllegalStateException("Missing key: " + key));
    }

    /**
     * Attempts to get the object of type {@link T} for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    public <T> T tryGetAs(String key, TypeToken<T> type) {
        return getAs(key, type).orElseThrow(() -> new IllegalStateException("Missing key: " + key));
    }

    /**
     * Attempts to get the object of type {@link T} for the given key. A exception
     * will be thrown if the key isn't present.
     *
     * @param key The key
     * @return The object
     */
    public <T> T tryGetAs(String key, Class<T> type) {
        return getAs(key, type).orElseThrow(() -> new IllegalStateException("Missing key: " + key));
    }

    /**
     * Gets the {@link OperatedPoryElement} for the given key, or {@link Optional#empty()} if missing.
     *
     * @param key The key
     * @return The element
     */
    public Optional<PoryElement> get(String key) {
        checkNotNull(key, "key");
        return Optional.ofNullable(this.map.get(key));
    }

    /**
     * Gets the object of type {@link T} for the given key, or {@link Optional#empty()} if missing.
     *
     * @param key The key
     * @return The object if present, otherwise {@link Optional#empty()}
     */
    public <T> Optional<T> getAs(String key, Type type) {
        return get(key).map(e -> e.as(type));
    }

    /**
     * Gets the object of type {@link T} for the given key, or {@link Optional#empty()} if missing.
     *
     * @param key The key
     * @return The object if present, otherwise {@link Optional#empty()}
     */
    public <T> Optional<T> getAs(String key, TypeToken<T> type) {
        return get(key).map(e -> e.as(type));
    }

    /**
     * Gets the object of type {@link T} for the given key, or {@link Optional#empty()} if missing.
     *
     * @param key The key
     * @return The object if present, otherwise {@link Optional#empty()}
     */
    public <T> Optional<T> getAs(String key, Class<T> type) {
        return get(key).map(e -> e.as(type));
    }

    /**
     * Gets the {@link PoryObject} for the given key, or {@link Optional#empty()} if missing.
     *
     * @param key The key
     * @return The object if present, otherwise {@link Optional#empty()}
     */
    public Optional<PoryObject> getAsObject(String key) {
        return get(key).map(PoryElement::asObject);
    }

    /**
     * Gets the int value for the given key, or {@link OptionalInt#empty()} if missing.
     *
     * @param key The key
     * @return The int value if present, otherwise {@link OptionalInt#empty()}
     */
    public OptionalInt getAsInt(String key) {
        return get(key).map(e -> OptionalInt.of(e.asInt())).orElseGet(OptionalInt::empty);
    }

    /**
     * Gets the double value for the given key, or {@link OptionalDouble#empty()} if missing.
     *
     * @param key The key
     * @return The double value if present, otherwise {@link OptionalDouble#empty()}
     */
    public OptionalDouble getAsDouble(String key) {
        return get(key).map(e -> OptionalDouble.of(e.asDouble())).orElseGet(OptionalDouble::empty);
    }
}
