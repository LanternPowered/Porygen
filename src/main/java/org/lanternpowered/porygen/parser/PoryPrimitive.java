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

/**
 * Represents a primitive {@link PoryPrimitive}.
 */
public final class PoryPrimitive extends PoryElement {

    private final Object value;

    /**
     * Constructs a new {@link PoryPrimitive}
     * from the given string.
     *
     * @param value The string
     */
    public PoryPrimitive(String value) {
        this((Object) value);
    }

    /**
     * Constructs a new {@link PoryPrimitive}
     * from the given number.
     *
     * @param value The number
     */
    public PoryPrimitive(Number value) {
        this((Object) value);
    }

    /**
     * Constructs a new {@link PoryPrimitive}
     * from the given boolean.
     *
     * @param value The boolean
     */
    public PoryPrimitive(Boolean value) {
        this((Object) value);
    }

    /**
     * Constructs a new {@link PoryPrimitive}
     * from the given character.
     *
     * @param value The character
     */
    public PoryPrimitive(Character value) {
        this((Object) value);
    }

    PoryPrimitive(Object value) {
        checkNotNull(value, "value");
        if (value instanceof Character) {
            value = value.toString();
        }
        this.value = value;
    }

    @Override
    public int asInt() {
        return asNumber().intValue();
    }

    @Override
    public double asDouble() {
        return asNumber().doubleValue();
    }

    @Override
    public float asFloat() {
        return asNumber().floatValue();
    }

    @Override
    public long asLong() {
        return asNumber().longValue();
    }

    @Override
    public String asString() {
        return this.value.toString();
    }

    @Override
    public short asShort() {
        return asNumber().shortValue();
    }

    @Override
    public byte asByte() {
        return asNumber().byteValue();
    }

    @Override
    public boolean asBoolean() {
        return this.value instanceof String ? Boolean.parseBoolean((String) this.value) : (Boolean) this.value;
    }

    @Override
    public Number asNumber() {
        return this.value instanceof String ? Double.parseDouble((String) this.value) : (Number) this.value;
    }
}
