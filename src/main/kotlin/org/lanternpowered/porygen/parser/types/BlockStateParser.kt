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

import com.google.common.base.Splitter
import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.*
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.block.BlockType

class BlockStateParser : PoryParser<BlockState> {

    override fun parse(element: PoryElement, type: TypeToken<BlockState>, ctx: PoryParserContext): BlockState {
        if (element.isArray || element.isNull) {
            throw ParseException("Unsupported element type: " + element.javaClass.simpleName)
        } else if (element.isPrimitive) {
            var value = element.asString()
            var index = value.indexOf('[')
            val id = if (index == -1) value else value.substring(0, index)
            var block = Sponge.getRegistry().getType(BlockType::class.java, id)
                    .orElseThrow { ParseException("Cannot find a BlockType with id '$id'") }.defaultState
            if (index != -1) {
                value = value.substring(index + 1, value.indexOf(']'))
                val entries = Splitter.on(',').split(value)
                for (entry in entries) {
                    index = entry.indexOf('=')
                    val trait = entry.substring(0, index)
                    val traitValue = entry.substring(index + 1)
                    block = withTrait(block, trait, traitValue)
                }
            }
            return block
        }
        val obj = element.asObject()
        val id = obj.tryGet("type").asString()
        var block = Sponge.getRegistry().getType(BlockType::class.java, id)
                .orElseThrow { ParseException("Cannot find a BlockType with id '$id'") }.defaultState
        val props = obj["properties"]
        if (props != null) {
            props as PoryObject
            for (entry in props.entries()) {
                block = withTrait(block, entry.key, entry.value.asString())
            }
        }
        return block
    }

    private fun withTrait(blockState: BlockState, trait: String, value: String): BlockState {
        var block = blockState
        val blockTrait = block.getTrait(trait).orElse(null)
        if (blockTrait != null) {
            val blockTraitValue = blockTrait.parseValue(value).orElse(null)
            if (blockTraitValue != null) {
                block = block.withTrait(blockTrait, blockTraitValue).get()
            }
        }
        return block
    }
}
