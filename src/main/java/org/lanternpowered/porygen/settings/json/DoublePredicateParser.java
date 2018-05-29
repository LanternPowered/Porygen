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

import com.google.common.base.Joiner;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.StringUtils;
import org.lanternpowered.porygen.util.RegexPatterns;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoublePredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses a {@link DoublePredicate}.
 * <p>Examples:
 * </p>
 * <p>
 * This example will match any number between 10 and 20 (inclusive)
 * >= 10 and <= 20
 * </p>
 * <p>
 * This example will match any number between 10 and 20 (inclusive), except 15
 * (>= 10 and <= 20) and not = 15
 * </p>
 * <p>
 * This example will match 7 or 9
 * = 9 or = 7
 * </p>
 */
public class DoublePredicateParser implements JsonDeserializer<DoublePredicate> {

    /**
     * All the match operators that are supported.
     */
    private final Map<String, MatchOperatorProvider> matchOperatorProviders = new HashMap<>();

    /**
     * All the join operators that are supported.
     */
    private final Map<String, JoinOperatorProvider> joinOperatorProviders = new HashMap<>();

    /**
     * The pattern to match a single {@link DoublePredicate} operation.
     */
    private final Pattern matchOperatorPattern;

    /**
     * The pattern to match a single join operation.
     */
    private final Pattern joinOperatorPattern;

    /**
     * The pattern to match content between parentheses.
     */
    private final Pattern parenthesesPattern = Pattern.compile("\\([^(]*\\)");

    {
        // Create the operators
        this.matchOperatorProviders.put(">", value -> (v -> v > value));
        this.matchOperatorProviders.put("<", value -> (v -> v < value));
        this.matchOperatorProviders.put("<=", value -> (v -> v <= value));
        this.matchOperatorProviders.put(">=", value -> (v -> v >= value));
        this.matchOperatorProviders.put("=", value -> (v -> v == value));
        this.matchOperatorProviders.put("!=", value -> (v -> v != value));
        this.matchOperatorProviders.put("<>", value -> (v -> v != value));

        this.joinOperatorProviders.put("and", DoublePredicate::and);
        this.joinOperatorProviders.put("or", DoublePredicate::or);
        this.joinOperatorProviders.put("and not", (a, b) -> a.and(b.negate()));
        this.joinOperatorProviders.put("or not", (a, b) -> a.or(b.negate()));

        // Create the pattern based on the registered operators
        String operators = Joiner.on('|').join(this.matchOperatorProviders.keySet());
        this.matchOperatorPattern = Pattern.compile("(?:(" + operators + ")\\s*(" + RegexPatterns.DOUBLE_PATTERN + ")|@([0-9]+))");

        operators = Joiner.on('|').join(this.joinOperatorProviders.keySet().stream()
                .sorted((a, b) -> b.length() - a.length())
                .map(s -> s.replace(" ", "\\s+"))
                .collect(Collectors.toList()));
        this.joinOperatorPattern = Pattern.compile("\\s+(" + operators + ")\\s+");
    }

    @Override
    public DoublePredicate deserialize(
            JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        String conditions = element.getAsString();
        if (conditions.equalsIgnoreCase("true")) {
            return v -> true;
        } else if (conditions.equalsIgnoreCase("false")) {
            return v -> false;
        }

        // Parsed sub conditions
        final List<DoublePredicate> parsedConditions = new ArrayList<>();

        Matcher matcher;
        while ((matcher = this.parenthesesPattern.matcher(conditions)).find()) {
            // Get the condition without the parentheses
            final String condition = conditions.substring(matcher.start() + 1, matcher.end() - 1);
            final int index = parsedConditions.size();
            parsedConditions.add(parse(condition, parsedConditions));
            // Replace the section with the index
            conditions = matcher.replaceFirst("@" + index);
        }

        return parse(conditions, parsedConditions);
    }

    private DoublePredicate parse(String condition, List<DoublePredicate> parsedConditions) {
        DoublePredicate parsedCondition = null;
        while (true) {
            JoinOperatorProvider joinOperatorProvider = null;
            if (parsedCondition != null) {
                final Matcher joinMatcher = this.joinOperatorPattern.matcher(condition);
                if (!joinMatcher.find()) {
                    throw new IllegalArgumentException("Invalid join operator: " + condition);
                }
                condition = condition.substring(joinMatcher.end());
                if (condition.trim().isEmpty()) {
                    throw new IllegalArgumentException("Expected another condition");
                }
                final String operator = StringUtils.normalizeSpace(joinMatcher.group(1));
                joinOperatorProvider = this.joinOperatorProviders.get(operator);
            }
            final Matcher matchMatcher = this.matchOperatorPattern.matcher(condition);
            if (!matchMatcher.find()) {
                throw new IllegalArgumentException("Invalid condition: " + condition);
            }
            condition = condition.substring(matchMatcher.end());
            // Get the operator
            final String operator = matchMatcher.group(1);
            final DoublePredicate parsedCondition1;
            if (operator == null) {
                // References a other parsed condition
                parsedCondition1 = parsedConditions.get(Integer.parseInt(matchMatcher.group(3)));
            } else {
                final double value = Double.parseDouble(matchMatcher.group(2));
                final MatchOperatorProvider matchOperatorProvider = this.matchOperatorProviders.get(operator);
                parsedCondition1 = matchOperatorProvider.get(value);
            }
            if (joinOperatorProvider == null) {
                parsedCondition = parsedCondition1;
            } else {
                parsedCondition = joinOperatorProvider.join(parsedCondition, parsedCondition1);
            }
            if (condition.trim().isEmpty()) {
                break;
            }
        }
        return parsedCondition;
    }

    @FunctionalInterface
    private interface MatchOperatorProvider {

        DoublePredicate get(double value);
    }

    @FunctionalInterface
    private interface JoinOperatorProvider {

        DoublePredicate join(DoublePredicate a, DoublePredicate b);
    }
}
