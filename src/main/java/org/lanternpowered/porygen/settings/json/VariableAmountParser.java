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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.lanternpowered.porygen.util.RegexPatterns;
import org.spongepowered.api.util.weighted.VariableAmount;

import java.lang.reflect.Type;
import java.util.regex.Matcher;

/**
 * A parser for {@link VariableAmount}.
 */
public final class VariableAmountParser implements JsonDeserializer<VariableAmount> {

    @Override
    public VariableAmount deserialize(
            JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        if (element.isJsonPrimitive()) { // Handle primitives
            final String value = element.getAsString();
            final Matcher matcher = RegexPatterns.DOUBLE_RANGE_PATTERN.matcher(value);
            // Check if the value matches the range pattern
            if (matcher.matches()) {
                final double min = Double.parseDouble(matcher.group(1));
                final double max = Double.parseDouble(matcher.group(2));
                return VariableAmount.range(min, max);
            }
            return VariableAmount.fixed(element.getAsDouble());
        } else if (element.isJsonArray() || element.isJsonNull()) {
            throw new JsonParseException("Unsupported json type: " + element.getClass().getSimpleName());
        }
        final JsonObject obj = element.getAsJsonObject();
        final JsonElement baseElement = obj.get("base");
        if (baseElement != null) {
            final double base = baseElement.getAsDouble();

            // Chance for the addition or variance
            final JsonElement chanceElement = obj.get("chance");

            final JsonElement additionElement = obj.get("addition");
            if (additionElement != null) {
                // Base and addition
                final VariableAmount addition = ctx.deserialize(additionElement, VariableAmount.class);
                // And chance?
                if (chanceElement != null) {
                    return VariableAmount.baseWithOptionalAddition(base, addition, chanceElement.getAsDouble());
                }
                return VariableAmount.baseWithRandomAddition(base, addition);
            }

            final JsonElement varianceElement = obj.get("variance");
            if (varianceElement != null) {
                // Base and variance
                final VariableAmount variance = ctx.deserialize(varianceElement, VariableAmount.class);
                // And chance?
                if (chanceElement != null) {
                    return VariableAmount.baseWithOptionalVariance(base, variance, chanceElement.getAsDouble());
                }
                return VariableAmount.baseWithVariance(base, variance);
            }
        }
        // Alternative for the range string pattern
        final JsonElement minElement = obj.get("min");
        final JsonElement maxElement = obj.get("max");
        if (minElement != null && maxElement != null) {
            final double min = minElement.getAsDouble();
            final double max = maxElement.getAsDouble();
            return VariableAmount.range(min, max);
        }
        throw new JsonParseException("Cannot parse: " + obj);
    }
}
