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
package org.lanternpowered.porygen.parser.types.util.weighted

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.ParseException
import org.lanternpowered.porygen.parser.PoryElement
import org.lanternpowered.porygen.parser.PoryParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.util.RegexPatterns
import org.spongepowered.api.util.weighted.VariableAmount

/**
 * A parser for [VariableAmount].
 */
class VariableAmountParser : PoryParser<VariableAmount> {

    override fun parse(element: PoryElement, type: TypeToken<VariableAmount>, ctx: PoryParserContext): VariableAmount {
        if (element.isPrimitive) { // Handle primitives
            val value = element.asString()
            val matcher = RegexPatterns.DOUBLE_RANGE_PATTERN.matcher(value)
            // Check if the value matches the range pattern
            if (matcher.matches()) {
                val min = matcher.group(1).toDouble()
                val max = matcher.group(2).toDouble()
                return VariableAmount.range(min, max)
            }
            return VariableAmount.fixed(element.asDouble())
        } else if (element.isArray || element.isNull) {
            throw ParseException("Unsupported element type: " + element.javaClass.simpleName)
        }
        val obj = element.asObject()
        val base = obj.getAsDouble("base")
        if (base != null) {
            // Chance for the addition or variance
            val chance = obj.getAsDouble("chance")

            val addition = obj.getAsObj<VariableAmount>("addition")
            if (addition != null) {
                // Base and addition (and chance?)
                return if (chance == null)
                    VariableAmount.baseWithRandomAddition(base, addition)
                else
                    VariableAmount.baseWithOptionalAddition(base, addition, chance)
            }

            val variance = obj.getAsObj<VariableAmount>("variance")
            if (variance != null) {
                // Base and variance (and chance?)
                return if (chance == null)
                    VariableAmount.baseWithVariance(base, variance)
                else
                    VariableAmount.baseWithOptionalVariance(base, variance, chance)
            }
        }
        // Alternative for the range string pattern
        val min = obj.getAsDouble("min")
        val max = obj.getAsDouble("max")
        if (min != null && max != null) {
            return VariableAmount.range(min, max)
        }
        throw ParseException("Cannot parse: $obj")
    }
}
