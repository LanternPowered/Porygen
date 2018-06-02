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
package org.lanternpowered.porygen.parser.types.populator;

import com.google.common.reflect.TypeToken;
import org.lanternpowered.porygen.parser.PoryElement;
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.spongepowered.api.util.weighted.VariableAmount;
import org.spongepowered.api.util.weighted.WeightedTable;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.populator.BigMushroom;

public class BigMushroomParser implements PoryParser<BigMushroom> {

    @Override
    public BigMushroom parse(PoryElement element, TypeToken<BigMushroom> type, PoryParserContext ctx) {
        final BigMushroom.Builder builder = BigMushroom.builder();
        final PoryObject obj = element.asObject();
        obj.getAs(PopulatorParserConstants.PER_CHUNK, VariableAmount.class)
                .ifPresent(builder::mushroomsPerChunk);
        obj.getAs(PopulatorParserConstants.WEIGHTED_OBJECT, new TypeToken<WeightedTable<PopulatorObject>>() {})
                .ifPresent(builder::types);
        return builder.build();
    }
}
