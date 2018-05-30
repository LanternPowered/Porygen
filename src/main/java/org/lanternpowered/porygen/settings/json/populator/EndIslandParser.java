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
package org.lanternpowered.porygen.settings.json.populator;

import static org.lanternpowered.porygen.settings.json.populator.PopulatorParserConstants.BLOCK;
import static org.lanternpowered.porygen.settings.json.populator.PopulatorParserConstants.EXCLUSION_RADIUS;
import static org.lanternpowered.porygen.settings.json.populator.PopulatorParserConstants.RADIUS_DECREMENT;
import static org.lanternpowered.porygen.settings.json.populator.PopulatorParserConstants.STARTING_RADIUS;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.lanternpowered.porygen.settings.json.JsonDeserializationContext;
import org.lanternpowered.porygen.settings.json.JsonDeserializer;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.weighted.VariableAmount;
import org.spongepowered.api.world.gen.populator.EndIsland;

import java.lang.reflect.Type;

public class EndIslandParser implements JsonDeserializer<EndIsland> {

    @Override
    public EndIsland deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        final EndIsland.Builder builder = EndIsland.builder();
        ctx.ifPresent(BLOCK, BlockState.class, builder::islandBlock);
        ctx.ifPresent(STARTING_RADIUS, VariableAmount.class, builder::startingRadius);
        ctx.ifPresent(RADIUS_DECREMENT, VariableAmount.class, builder::radiusDecrement);
        ctx.ifIntPresent(EXCLUSION_RADIUS, builder::exclusionRadius);
        return builder.build();
    }
}
