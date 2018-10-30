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
package org.lanternpowered.porygen.parser.types.populator

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.PoryObject
import org.lanternpowered.porygen.parser.PoryObjectParser
import org.lanternpowered.porygen.parser.PoryParserContext
import org.lanternpowered.porygen.settings.Spawner
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.util.weighted.LootTable
import org.spongepowered.api.util.weighted.VariableAmount
import org.spongepowered.api.world.gen.populator.Dungeon

class DungeonParser : PoryObjectParser<Dungeon> {

    override fun parse(obj: PoryObject, type: TypeToken<Dungeon>, ctx: PoryParserContext): Dungeon {
        val builder = Dungeon.builder()
        obj.getAsObj<VariableAmount>(PopulatorParserConstants.PER_CHUNK)?.run(builder::attempts)
        obj.getAsObj<LootTable<ItemStackSnapshot>>(PopulatorParserConstants.CHEST_LOOT)?.run(builder::possibleItems)
        obj.getAsObj<Spawner>(PopulatorParserConstants.SPAWNER)?.run { builder.mobSpawnerData(this.mobSpawnerData) }
        return builder.build()
    }
}
