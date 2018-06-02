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
import org.lanternpowered.porygen.parser.PoryObject;
import org.lanternpowered.porygen.parser.PoryObjectParser;
import org.lanternpowered.porygen.parser.PoryParserContext;
import org.lanternpowered.porygen.settings.Spawner;
import org.lanternpowered.porygen.util.Rangei;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.MobSpawnerData;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.util.weighted.NestedTableEntry;
import org.spongepowered.api.util.weighted.RandomObjectTable;
import org.spongepowered.api.util.weighted.WeightedTable;

public class SpawnerParser implements PoryObjectParser<Spawner> {

    @Override
    public Spawner parse(PoryObject object, TypeToken<Spawner> type, PoryParserContext ctx) {
        final MobSpawnerData spawnerData = Sponge.getDataManager()
                .getManipulatorBuilder(MobSpawnerData.class).get().create();
        object.getAs("possible-entities", new TypeToken<RandomObjectTable<EntityArchetype>>() {}).ifPresent(table -> {
            // The key expects a weighted table, but their are also
            // other table types, so wrap around them in that case
            final WeightedTable<EntityArchetype> weightedTable;
            if (table instanceof WeightedTable) {
                weightedTable = (WeightedTable<EntityArchetype>) table;
            } else {
                weightedTable = new WeightedTable<>();
                weightedTable.add(new NestedTableEntry<>(100, table));
            }
            spawnerData.set(Keys.SPAWNER_ENTITIES, weightedTable);
        });
        object.getAsInt("spawn-range").ifPresent(value -> spawnerData.set(Keys.SPAWNER_SPAWN_RANGE, (short) value));
        object.getAsInt("required-player-range").ifPresent(value -> spawnerData.set(Keys.SPAWNER_REQUIRED_PLAYER_RANGE, (short) value));
        object.getAsInt("max-nearby-entities").ifPresent(value -> spawnerData.set(Keys.SPAWNER_MAXIMUM_NEARBY_ENTITIES, (short) value));
        object.getAs("spawn-delay", Rangei.class).ifPresent(value -> {
            spawnerData.set(Keys.SPAWNER_MAXIMUM_DELAY, (short) value.getMax());
            spawnerData.set(Keys.SPAWNER_MINIMUM_DELAY, (short) value.getMin());
        });
        return new Spawner(spawnerData);
    }
}
