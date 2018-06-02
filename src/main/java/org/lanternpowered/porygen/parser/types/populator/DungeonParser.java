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
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryObjectParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.lanternpowered.porygen.settings.Spawner;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.weighted.LootTable;
import org.spongepowered.api.util.weighted.VariableAmount;
import org.spongepowered.api.world.gen.populator.Dungeon;

public class DungeonParser implements PoryObjectParser<Dungeon> {

    @Override
    public Dungeon parse(PoryObject object, TypeToken<Dungeon> type, PoryParserContext ctx) {
        final Dungeon.Builder builder = Dungeon.builder();
        object.getAs(PopulatorParserConstants.PER_CHUNK, VariableAmount.class)
                .ifPresent(builder::attempts);
        object.getAs(PopulatorParserConstants.CHEST_LOOT, new TypeToken<LootTable<ItemStackSnapshot>>() {})
                .ifPresent(builder::possibleItems);
        object.getAs(PopulatorParserConstants.SPAWNER, Spawner.class)
                .ifPresent(spawner -> builder.mobSpawnerData(spawner.getMobSpawnerData()));
        return builder.build();
    }
}
