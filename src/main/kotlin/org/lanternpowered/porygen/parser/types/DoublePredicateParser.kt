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
package org.lanternpowered.porygen.parser.types

import com.google.common.base.Joiner
import com.google.common.reflect.TypeToken
import org.apache.commons.lang3.StringUtils
import org.lanternpowered.porygen.parser.ParseException
import org.lanternpowered.porygen.parser.PoryElement
import org.lanternpowered.porygen.parser.PoryParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.util.RegexPatterns
import java.util.*
import java.util.function.DoublePredicate
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.streams.toList

typealias MatchOperatorProvider = (Double) -> DoublePredicate
typealias JoinOperatorProvider = (DoublePredicate, DoublePredicate) -> DoublePredicate

/**
 * Parses a [DoublePredicate].
 *
 * Examples:
 *
 * This example will match any number between 10 and 20 (inclusive)
 * >= 10 and <= 20
 *
 * This example will match any number between 10 and 20 (inclusive), except 15
 * (>= 10 and <= 20) and not = 15
 *
 * This example will match 7 or 9
 * = 9 or = 7
 */
class DoublePredicateParser : PoryParser<DoublePredicate> {

    /**
     * All the match operators that are supported.
     */
    private val matchOperatorProviders = HashMap<String, MatchOperatorProvider>()

    /**
     * All the join operators that are supported.
     */
    private val joinOperatorProviders = HashMap<String, JoinOperatorProvider>()

    /**
     * The pattern to match a single [DoublePredicate] operation.
     */
    private val matchOperatorPattern: Pattern

    /**
     * The pattern to match a single join operation.
     */
    private val joinOperatorPattern: Pattern

    /**
     * The pattern to match content between parentheses.
     */
    private val parenthesesPattern = Pattern.compile("\\([^(]*\\)")

    init {
        // Create the operators
        this.matchOperatorProviders[">"] = { value -> DoublePredicate { v -> v > value } }
        this.matchOperatorProviders["<"] = { value -> DoublePredicate { v -> v < value } }
        this.matchOperatorProviders["<="] = { value -> DoublePredicate { v -> v <= value } }
        this.matchOperatorProviders[">="] = { value -> DoublePredicate { v -> v >= value } }
        this.matchOperatorProviders["="] = { value -> DoublePredicate { v -> v == value } }
        this.matchOperatorProviders["!="] = { value -> DoublePredicate { v -> v != value } }
        this.matchOperatorProviders["<>"] = { value -> DoublePredicate { v -> v != value } }

        this.joinOperatorProviders["and"] = { obj, other -> obj.and(other) }
        this.joinOperatorProviders["or"] = { obj, other -> obj.or(other) }
        this.joinOperatorProviders["and not"] = { a, b -> a.and(b.negate()) }
        this.joinOperatorProviders["or not"] = { a, b -> a.or(b.negate()) }

        // Create the pattern based on the registered operators
        var operators = Joiner.on('|').join(this.matchOperatorProviders.keys)
        this.matchOperatorPattern = Pattern.compile("(?:($operators)\\s*(${RegexPatterns.DOUBLE_PATTERN})|@([0-9]+))")

        operators = Joiner.on('|').join(this.joinOperatorProviders.keys.stream()
                .sorted { a, b -> b.length - a.length }
                .map { s -> s.replace(" ", "\\s+") }
                .toList())
        this.joinOperatorPattern = Pattern.compile("\\s+($operators)\\s+")
    }

    override fun parse(element: PoryElement, type: TypeToken<DoublePredicate>, ctx: PoryParserContext): DoublePredicate {
        var conditions = element.asString()
        if (conditions.equals("true", ignoreCase = true)) {
            return DoublePredicate { true }
        } else if (conditions.equals("false", ignoreCase = true)) {
            return DoublePredicate { false }
        }

        // Parsed sub conditions
        val parsedConditions = ArrayList<DoublePredicate>()

        var matcher: Matcher
        while (true) {
            matcher = this.parenthesesPattern.matcher(conditions)
            if (!matcher.find()) break
            // Get the condition without the parentheses
            val condition = conditions.substring(matcher.start() + 1, matcher.end() - 1)
            val index = parsedConditions.size
            parsedConditions.add(parse(condition, parsedConditions))
            // Replace the section with the index
            conditions = matcher.replaceFirst("@$index")
        }

        return parse(conditions, parsedConditions)
    }

    private fun parse(condition: String, parsedConditions: List<DoublePredicate>): DoublePredicate {
        var conditionValue = condition
        var parsedCondition: DoublePredicate? = null
        while (true) {
            var joinOperatorProvider: JoinOperatorProvider? = null
            if (parsedCondition != null) {
                val joinMatcher = this.joinOperatorPattern.matcher(conditionValue)
                if (!joinMatcher.find()) {
                    throw IllegalArgumentException("Invalid join operator: $conditionValue")
                }
                conditionValue = conditionValue.substring(joinMatcher.end())
                if (conditionValue.trim { it <= ' ' }.isEmpty()) {
                    throw IllegalArgumentException("Expected another condition")
                }
                val operator = StringUtils.normalizeSpace(joinMatcher.group(1))
                joinOperatorProvider = this.joinOperatorProviders[operator]
            }
            val matchMatcher = this.matchOperatorPattern.matcher(conditionValue)
            if (!matchMatcher.find()) {
                throw IllegalArgumentException("Invalid condition: $conditionValue")
            }
            conditionValue = conditionValue.substring(matchMatcher.end())
            // Get the operator
            val operator = matchMatcher.group(1)
            val nextParsedCondition = if (operator == null) {
                // References a other parsed condition
                parsedConditions[Integer.parseInt(matchMatcher.group(3))]
            } else {
                val value = java.lang.Double.parseDouble(matchMatcher.group(2))
                val matchOperatorProvider = this.matchOperatorProviders[operator] ?: throw ParseException("Unknown operator: $operator")
                matchOperatorProvider(value)
            }
            parsedCondition = joinOperatorProvider?.invoke(parsedCondition!!, nextParsedCondition) ?: nextParsedCondition
            if (conditionValue.trim { it <= ' ' }.isEmpty()) {
                break
            }
        }
        return parsedCondition!!
    }
}
