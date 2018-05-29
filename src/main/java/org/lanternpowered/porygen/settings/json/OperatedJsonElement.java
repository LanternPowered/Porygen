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
package org.lanternpowered.porygen.settings.json;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Parses a operation element from a {@link JsonObject}.
 * <p>For example: "@add my-key", 'add' is here a operation.
 */
public final class OperatedJsonElement {

    /**
     * Gets a {@link OperatedJsonElement} with the
     * given key from the target {@link JsonObject}.
     *
     * @param obj The json object
     * @param key The key
     * @return The operated json element, or null if not found
     */
    @Nullable
    public static OperatedJsonElement get(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        if (element != null) {
            return new OperatedJsonElement(element, key, ImmutableList.of());
        }
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            if (!entry.getKey().contains(key)) {
                continue;
            }
            String parsedKey = entry.getKey();
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
            if (key.equals(parsedKey)) {
                return new OperatedJsonElement(entry.getValue(), key, operations.build());
            }
        }
        return null;
    }

    private final JsonElement element;
    private final String key;
    private final List<String> operations;

    private OperatedJsonElement(JsonElement element, String key, List<String> operations) {
        this.operations = operations;
        this.element = element;
        this.key = key;
    }

    /**
     * Gets the key.
     *
     * @return The key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the {@link JsonElement}.
     *
     * @return The json element
     */
    public JsonElement getElement() {
        return this.element;
    }

    /**
     * Gets the operations that are
     * applied to the element key.
     *
     * @return The operations
     */
    public List<String> getOperations() {
        return this.operations;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", this.key)
                .add("element", this.element)
                .add("operations", Iterables.toString(this.operations))
                .toString();
    }
}
