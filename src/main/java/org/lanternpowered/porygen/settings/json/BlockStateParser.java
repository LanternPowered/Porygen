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

import com.google.common.base.Splitter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.trait.BlockTrait;

import java.lang.reflect.Type;
import java.util.Map;

public class BlockStateParser implements JsonDeserializer<BlockState> {

    @Override
    public BlockState deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        if (element.isJsonArray() || element.isJsonNull()) {
            throw new JsonParseException("Unsupported json type: " + element.getClass().getSimpleName());
        } else if (element.isJsonPrimitive()) {
            String value = element.getAsString();
            int index = value.indexOf('[');
            final String id = index == -1 ? value : value.substring(0, index);
            BlockState block = Sponge.getRegistry().getType(BlockType.class, id)
                    .orElseThrow(() -> new JsonParseException("Cannot find a BlockType with id '" + id + "'")).getDefaultState();
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
        final JsonObject obj = element.getAsJsonObject();
        final String id = obj.get("type").getAsString();
        BlockState block = Sponge.getRegistry().getType(BlockType.class, id)
                .orElseThrow(() -> new JsonParseException("Cannot find a BlockType with id '" + id + "'")).getDefaultState();
        final JsonElement propsElement = obj.get("properties");
        if (propsElement != null) {
            final JsonObject props = propsElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : props.entrySet()) {
                block = withTrait(block, entry.getKey(), entry.getValue().getAsString());
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
