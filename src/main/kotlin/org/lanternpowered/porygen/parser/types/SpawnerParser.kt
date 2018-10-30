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
import org.lanternpowered.porygen.settings.Spawner
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.MobSpawnerData
import org.spongepowered.api.entity.EntityArchetype
import org.spongepowered.api.util.weighted.NestedTableEntry
import org.spongepowered.api.util.weighted.RandomObjectTable
import org.spongepowered.api.util.weighted.WeightedTable

class SpawnerParser : PoryObjectParser<Spawner> {

    override fun parse(obj: PoryObject, type: TypeToken<Spawner>, ctx: PoryParserContext): Spawner {
        val spawnerData = Sponge.getDataManager().getManipulatorBuilder(MobSpawnerData::class.java).get().create()
        obj.getAsObj<RandomObjectTable<EntityArchetype>>("possible-entities")?.let { table ->
            // The key expects a weighted table, but their are also
            // other table types, so wrap around them in that case
            val weightedTable = table as? WeightedTable<EntityArchetype>
                    ?: WeightedTable<EntityArchetype>().apply { add(NestedTableEntry(100.0, table)) }
            spawnerData.set(Keys.SPAWNER_ENTITIES, weightedTable)
        }
        obj.getAsInt("spawn-range")?.let { spawnerData.set(Keys.SPAWNER_SPAWN_RANGE, it.toShort()) }
        obj.getAsInt("required-player-range")?.let { spawnerData.set(Keys.SPAWNER_REQUIRED_PLAYER_RANGE, it.toShort()) }
        obj.getAsInt("max-nearby-entities")?.let { spawnerData.set(Keys.SPAWNER_MAXIMUM_NEARBY_ENTITIES, it.toShort()) }
        obj.getAsObj<IntRange>("spawn-delay")?.let {
            spawnerData.set(Keys.SPAWNER_MAXIMUM_DELAY, it.endInclusive.toShort())
            spawnerData.set(Keys.SPAWNER_MINIMUM_DELAY, it.start.toShort())
        }
        return Spawner(spawnerData)
    }
}
