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

import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryObjectParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.lanternpowered.porygen.parser.PoryPrimitive;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.util.weighted.SeededVariableAmount;
import org.spongepowered.api.world.biome.GroundCoverLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.Function;

/**
 * A parser for {@link GroundCoverLayer}.
 */
public class GroundCoverLayerParser implements PoryObjectParser<GroundCoverLayer> {

    private static final SeededVariableAmount<Double> VARIABLE_AMOUNT_ONE = SeededVariableAmount.fixed(1);

    @Override
    public GroundCoverLayer parse(PoryObject object, TypeToken<GroundCoverLayer> type, PoryParserContext ctx) {
        final SeededVariableAmount<Double> depth = object.getAs("depth",
                new TypeToken<SeededVariableAmount<Double>>() {}).orElse(VARIABLE_AMOUNT_ONE);

        final PoryElement blockElement = object.tryGet("block");
        Function<Double, BlockState> blockFunction = null;
        if (blockElement.isObject()) {
            final PoryObject blockObj = blockElement.asObject();
            // Check if it's a block state that is represented
            if (!blockObj.has("type")) {
                final List<Tuple<DoublePredicate, BlockState>> entries = new ArrayList<>();
                blockObj.forEach((key, element) -> {
                    final DoublePredicate condition = new PoryPrimitive(key).as(DoublePredicate.class);
                    final BlockState block = element.as(BlockState.class);
                    entries.add(new Tuple<>(condition, block));
                });
                blockFunction = v -> {
                    for (Tuple<DoublePredicate, BlockState> entry : entries) {
                        if (entry.getFirst().test(v)) {
                            return entry.getSecond();
                        }
                    }
                    return BlockTypes.AIR.getDefaultState();
                };
            }
        }
        if (blockFunction == null) {
            final BlockState block = blockElement.as(BlockState.class);
            blockFunction = v -> block;
        }

        return new GroundCoverLayer(blockFunction, depth);
    }
}
