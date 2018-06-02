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
package org.lanternpowered.porygen.parser.types;

import com.google.common.base.Splitter;
import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.ParseException;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.trait.BlockTrait;

import java.util.Map;
import java.util.Optional;

public class BlockStateParser implements PoryParser<BlockState> {

    @Override
    public BlockState parse(PoryElement element, TypeToken<BlockState> type, PoryParserContext ctx) {
        if (element.isArray() || element.isNull()) {
            throw new ParseException("Unsupported element type: " + element.getClass().getSimpleName());
        } else if (element.isPrimitive()) {
            String value = element.asString();
            int index = value.indexOf('[');
            final String id = index == -1 ? value : value.substring(0, index);
            BlockState block = Sponge.getRegistry().getType(BlockType.class, id)
                    .orElseThrow(() -> new ParseException("Cannot find a BlockType with id '" + id + "'")).getDefaultState();
            if (index != -1) {
                value = value.substring(index + 1, value.indexOf(']'));
                final Iterable<String> entries = Splitter.on(',').split(value);
                for (String entry : entries) {
                    index = entry.indexOf('=');
                    final String trait = entry.substring(0, index);
                    final String traitValue = entry.substring(index + 1);
                    block = withTrait(block, trait, traitValue);
                }
            }
            return block;
        }
        final PoryObject obj = element.asObject();
        final String id = obj.tryGet("type").asString();
        BlockState block = Sponge.getRegistry().getType(BlockType.class, id)
                .orElseThrow(() -> new ParseException("Cannot find a BlockType with id '" + id + "'")).getDefaultState();
        final Optional<PoryElement> propsElement = obj.get("properties");
        if (propsElement.isPresent()) {
            final PoryObject props = propsElement.get().asObject();
            for (Map.Entry<String, PoryElement> entry : props.entries()) {
                block = withTrait(block, entry.getKey(), entry.getValue().asString());
            }
        }
        return block;
    }

    private static BlockState withTrait(BlockState block, String trait, String value) {
        final BlockTrait<?> blockTrait = block.getTrait(trait).orElse(null);
        if (blockTrait != null) {
            final Object blockTraitValue = blockTrait.parseValue(value).orElse(null);
            if (blockTraitValue != null) {
                block = block.withTrait(blockTrait, blockTraitValue).get();
            }
        }
        return block;
    }
}
