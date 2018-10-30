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

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.PoryObject
import org.lanternpowered.porygen.parser.PoryObjectParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.parser.PoryPrimitive
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.util.Tuple
import org.spongepowered.api.util.weighted.SeededVariableAmount
import org.spongepowered.api.world.biome.GroundCoverLayer
import java.util.*
import java.util.function.DoublePredicate
import java.util.function.Function

/**
 * A parser for [GroundCoverLayer].
 */
class GroundCoverLayerParser : PoryObjectParser<GroundCoverLayer> {

    override fun parse(obj: PoryObject, type: TypeToken<GroundCoverLayer>, ctx: PoryParserContext): GroundCoverLayer {
        val depth = obj.getAsObj<SeededVariableAmount<Double>>("depth") ?: VARIABLE_AMOUNT_ONE

        val blockElement = obj.tryGet("block")
        var blockFunction: ((Double) -> BlockState)? = null
        if (blockElement.isObject) {
            val blockObj = blockElement.asObject()
            // Check if it's a block state that is represented
            if (!blockObj.has("type")) {
                val entries = ArrayList<Tuple<DoublePredicate, BlockState>>()
                blockObj.forEach { key, element ->
                    val condition = PoryPrimitive(key).asObj<DoublePredicate>()
                    val block = element.asObj<BlockState>()
                    entries.add(Tuple(condition, block))
                }
                blockFunction = blockFun@ { v ->
                    for (entry in entries) {
                        if (entry.first.test(v)) {
                            return@blockFun entry.second
                        }
                    }
                    BlockTypes.AIR.defaultState
                }
            }
        }
        if (blockFunction == null) {
            val block = blockElement.asObj<BlockState>()
            blockFunction = { block }
        }
        return GroundCoverLayer(Function(blockFunction), depth)
    }

    companion object {

        private val VARIABLE_AMOUNT_ONE = SeededVariableAmount.fixed<Double>(1.0)
    }
}
