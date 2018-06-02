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

public abstract class PoryElement {

    public boolean isObject() {
        return this instanceof PoryObject;
    }

    public boolean isArray() {
        return this instanceof PoryArray;
    }

    public boolean isPrimitive() {
        return this instanceof PoryPrimitive;
    }

    public boolean isNull() {
        return this instanceof PoryNull;
    }

    /**
     * Gets this element as a {@link PoryObject}.
     *
     * @return The object
     */
    public PoryObject asObject() {
        return (PoryObject) this;
    }

    /**
     * Gets this element as a {@link PoryArray}.
     *
     * @return The array
     */
    public PoryArray asArray() {
        return (PoryArray) this;
    }

    /**
     * Gets this element as a {@link PoryPrimitive}.
     *
     * @return The primitive
     */
    public PoryPrimitive asPrimitive() {
        return (PoryPrimitive) this;
    }

    /**
     * Gets this element as a {@link Number} value.
     *
     * @return The number
     */
    public Number asNumber() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to number");
    }

    /**
     * Gets this element as a int value.
     *
     * @return The int
     */
    public int asInt() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to int");
    }

    /**
     * Gets this element as a double value.
     *
     * @return The double
     */
    public double asDouble() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to double");
    }

    /**
     * Gets this element as a float value.
     *
     * @return The float
     */
    public float asFloat() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to float");
    }

    /**
     * Gets this element as a long value.
     *
     * @return The long
     */
    public long asLong() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to long");
    }

    /**
     * Gets this element as a string value.
     *
     * @return The string
     */
    public String asString() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to string");
    }

    /**
     * Gets this element as a short value.
     *
     * @return The short
     */
    public short asShort() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to short");
    }

    /**
     * Gets this element as a byte value.
     *
     * @return The byte
     */
    public byte asByte() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to byte");
    }

    /**
     * Gets this element as a boolean value.
     *
     * @return The boolean
     */
    public boolean asBoolean() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to boolean");
    }

    /**
     * Gets this element as a char value.
     *
     * @return The char
     */
    public char asChar() {
        throw new ParseException("Cannot convert " + getClass().getSimpleName() + " to char");
    }

    /**
     * Parses this element as the given {@link Type}
     * through the current {@link PoryParserContext}.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The parsed object
     */
    public <T> T as(Type objectType) {
        return PoryParserContext.current().parse(this, objectType);
    }

    /**
     * Parses this element as the given {@link Class}
     * through the current {@link PoryParserContext}.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The parsed object
     */
    public <T> T as(Class<T> objectType) {
        return PoryParserContext.current().parse(this, objectType);
    }

    /**
     * Parses this element as the given {@link TypeToken}
     * through the current {@link PoryParserContext}.
     *
     * @param objectType The object type
     * @param <T> The object type
     * @return The parsed object
     */
    public <T> T as(TypeToken<T> objectType) {
        return PoryParserContext.current().parse(this, objectType);
    }
}
