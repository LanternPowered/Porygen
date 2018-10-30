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
package org.lanternpowered.porygen.parser

/**
 * Represents a primitive [PoryPrimitive].
 */
class PoryPrimitive internal constructor(value: Any) : PoryElement() {

    private val value: Any = (value as? Char)?.toString() ?: value

    /**
     * Constructs a new [PoryPrimitive]
     * from the given string.
     *
     * @param value The string
     */
    constructor(value: String) : this(value as Any)

    /**
     * Constructs a new [PoryPrimitive]
     * from the given number.
     *
     * @param value The number
     */
    constructor(value: Number) : this(value as Any)

    /**
     * Constructs a new [PoryPrimitive]
     * from the given boolean.
     *
     * @param value The boolean
     */
    constructor(value: Boolean) : this(value as Any)

    /**
     * Constructs a new [PoryPrimitive]
     * from the given character.
     *
     * @param value The character
     */
    constructor(value: Char) : this(value as Any)

    override fun asInt() = asNumber().toInt()
    override fun asDouble() = asNumber().toDouble()
    override fun asFloat() = asNumber().toFloat()
    override fun asLong() = asNumber().toLong()
    override fun asShort() = asNumber().toShort()
    override fun asByte() = asNumber().toByte()

    override fun asString() =  this.value.toString()

    override fun asBoolean(): Boolean = if (this.value is String) this.value.toBoolean() else this.value as Boolean
    override fun asNumber(): Number = if (this.value is String) this.value.toDouble() else this.value as Number
}
