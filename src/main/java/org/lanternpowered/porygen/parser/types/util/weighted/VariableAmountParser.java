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
package org.lanternpowered.porygen.parser.types.util.weighted;

import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.ParseException;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.lanternpowered.porygen.util.RegexPatterns;
import org.spongepowered.api.util.weighted.VariableAmount;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.regex.Matcher;

/**
 * A parser for {@link VariableAmount}.
 */
public final class VariableAmountParser implements PoryParser<VariableAmount> {

    @Override
    public VariableAmount parse(PoryElement element, TypeToken<VariableAmount> type, PoryParserContext ctx) {
        if (element.isPrimitive()) { // Handle primitives
            final String value = element.asString();
            final Matcher matcher = RegexPatterns.DOUBLE_RANGE_PATTERN.matcher(value);
            // Check if the value matches the range pattern
            if (matcher.matches()) {
                final double min = Double.parseDouble(matcher.group(1));
                final double max = Double.parseDouble(matcher.group(2));
                return VariableAmount.range(min, max);
            }
            return VariableAmount.fixed(element.asDouble());
        } else if (element.isArray() || element.isNull()) {
            throw new ParseException("Unsupported element type: " + element.getClass().getSimpleName());
        }
        final PoryObject obj = element.asObject();
        final OptionalDouble optBase = obj.getAsDouble("base");
        if (optBase.isPresent()) {
            final double base = optBase.getAsDouble();

            // Chance for the addition or variance
            final OptionalDouble chance = obj.getAsDouble("chance");

            final Optional<VariableAmount> addition = obj.getAs("addition", VariableAmount.class);
            if (addition.isPresent()) {
                // Base and addition (and chance?)
                return !chance.isPresent() ? VariableAmount.baseWithRandomAddition(base, addition.get()) :
                        VariableAmount.baseWithOptionalAddition(base, addition.get(), chance.getAsDouble());
            }

            final Optional<VariableAmount> variance = obj.getAs("variance", VariableAmount.class);
            if (variance.isPresent()) {
                // Base and variance (and chance?)
                return !chance.isPresent() ? VariableAmount.baseWithVariance(base, variance.get()) :
                        VariableAmount.baseWithOptionalVariance(base, variance.get(), chance.getAsDouble());
            }
        }
        // Alternative for the range string pattern
        final OptionalDouble min = obj.getAsDouble("min");
        final OptionalDouble max = obj.getAsDouble("max");
        if (min.isPresent() && max.isPresent()) {
            return VariableAmount.range(min.getAsDouble(), max.getAsDouble());
        }
        throw new ParseException("Cannot parse: " + obj);
    }
}
