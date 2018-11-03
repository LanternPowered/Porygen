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
package org.lanternpowered.porygen.parser

import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.api.util.DoubleRange
import org.lanternpowered.porygen.parser.types.*
import org.lanternpowered.porygen.settings.DataKeyValueMap
import org.lanternpowered.porygen.settings.DataManipulatorList
import org.lanternpowered.porygen.settings.ParentBasedBiomeGenerationSettings
import org.lanternpowered.porygen.parser.types.data.DataKeyValueMapParser
import org.lanternpowered.porygen.parser.types.data.DataManipulatorListParser
import org.lanternpowered.porygen.parser.types.populator.BigMushroomParser
import org.lanternpowered.porygen.parser.types.populator.BlockBlobParser
import org.lanternpowered.porygen.parser.types.populator.CactusParser
import org.lanternpowered.porygen.parser.types.populator.ChorusFlowerParser
import org.lanternpowered.porygen.parser.types.populator.DeadBushParser
import org.lanternpowered.porygen.parser.types.populator.DesertWellParser
import org.lanternpowered.porygen.parser.types.populator.DungeonParser
import org.lanternpowered.porygen.parser.types.populator.EndIslandParser
import org.lanternpowered.porygen.parser.types.populator.FossilParser
import org.lanternpowered.porygen.parser.types.populator.GlowstoneParser
import org.lanternpowered.porygen.parser.types.populator.IcePathParser
import org.lanternpowered.porygen.parser.types.populator.IceSpikeParser
import org.lanternpowered.porygen.parser.types.populator.LakeParser
import org.lanternpowered.porygen.parser.types.populator.NetherFireParser
import org.lanternpowered.porygen.parser.types.populator.PopulatorParser
import org.lanternpowered.porygen.parser.types.populator.PumpkinParser
import org.lanternpowered.porygen.parser.types.populator.ReedParser
import org.lanternpowered.porygen.parser.types.populator.VineParser
import org.lanternpowered.porygen.parser.types.populator.WaterLilyParser
import org.lanternpowered.porygen.parser.types.util.DoubleRangeParser
import org.lanternpowered.porygen.parser.types.util.IntRangeParser
import org.lanternpowered.porygen.parser.types.util.weighted.ChanceTableParser
import org.lanternpowered.porygen.parser.types.util.weighted.LootTableParser
import org.lanternpowered.porygen.parser.types.util.weighted.RandomObjectTableParser
import org.lanternpowered.porygen.parser.types.util.weighted.SeededVariableAmountParser
import org.lanternpowered.porygen.parser.types.util.weighted.TableEntryParser
import org.lanternpowered.porygen.parser.types.util.weighted.VariableAmountParser
import org.lanternpowered.porygen.parser.types.util.weighted.WeightedTableParser
import org.spongepowered.api.CatalogType
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.entity.EntityArchetype
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.util.weighted.ChanceTable
import org.spongepowered.api.util.weighted.LootTable
import org.spongepowered.api.util.weighted.RandomObjectTable
import org.spongepowered.api.util.weighted.SeededVariableAmount
import org.spongepowered.api.util.weighted.TableEntry
import org.spongepowered.api.util.weighted.VariableAmount
import org.spongepowered.api.util.weighted.WeightedTable
import org.spongepowered.api.world.biome.GroundCoverLayer
import org.spongepowered.api.world.gen.GenerationPopulator
import org.spongepowered.api.world.gen.Populator
import org.spongepowered.api.world.gen.populator.BigMushroom
import org.spongepowered.api.world.gen.populator.BlockBlob
import org.spongepowered.api.world.gen.populator.Cactus
import org.spongepowered.api.world.gen.populator.ChorusFlower
import org.spongepowered.api.world.gen.populator.DeadBush
import org.spongepowered.api.world.gen.populator.DesertWell
import org.spongepowered.api.world.gen.populator.Dungeon
import org.spongepowered.api.world.gen.populator.EndIsland
import org.spongepowered.api.world.gen.populator.Fossil
import org.spongepowered.api.world.gen.populator.Glowstone
import org.spongepowered.api.world.gen.populator.IcePath
import org.spongepowered.api.world.gen.populator.IceSpike
import org.spongepowered.api.world.gen.populator.Lake
import org.spongepowered.api.world.gen.populator.NetherFire
import org.spongepowered.api.world.gen.populator.Pumpkin
import org.spongepowered.api.world.gen.populator.Reed
import org.spongepowered.api.world.gen.populator.Vine
import org.spongepowered.api.world.gen.populator.WaterLily

import java.util.function.DoublePredicate

object GlobalPory {

    val PORY = PoryBuilder()
            .registerParser(BlockState::class.java, BlockStateParser())
            .registerParser(CatalogType::class.java, CatalogTypeParser<CatalogType>())
            .registerParser(DoublePredicate::class.java, DoublePredicateParser())
            .registerParser(GroundCoverLayer::class.java, GroundCoverLayerParser())
            .registerParser(GenerationPopulator::class.java, GenerationPopulatorParser())
            .registerParser(ParentBasedBiomeGenerationSettings::class.java, ParentBasedBiomeGenerationSettingsParser())
            .registerParser(ItemStack::class.java, ItemStackParser())
            .registerParser(ItemStackSnapshot::class.java, ItemStackSnapshotParser())
            .registerParser(DataKeyValueMap::class.java, DataKeyValueMapParser())
            .registerParser(DataManipulatorList::class.java, DataManipulatorListParser())
            .registerParser(EntityArchetype::class.java, EntityArchetypeParser())
            ///////////////////////////////
            /// Weighted Objects/Values ///
            ///////////////////////////////
            .registerParser(ChanceTable::class.java, ChanceTableParser<Any>())
            .registerParser(WeightedTable::class.java, WeightedTableParser<Any>())
            .registerParser(LootTable::class.java, LootTableParser<Any>())
            .registerParser(RandomObjectTable::class.java, RandomObjectTableParser<Any>())
            .registerParser(TableEntry::class.java, TableEntryParser<Any>())
            .registerParser(SeededVariableAmount::class.java, SeededVariableAmountParser<Any>())
            .registerParser(VariableAmount::class.java, VariableAmountParser())
            /////////////////////////
            /// Populator parsers ///
            /////////////////////////
            .registerParser(BigMushroom::class.java, BigMushroomParser())
            .registerParser(BlockBlob::class.java, BlockBlobParser())
            .registerParser(Cactus::class.java, CactusParser())
            .registerParser(ChorusFlower::class.java, ChorusFlowerParser())
            .registerParser(DeadBush::class.java, DeadBushParser())
            .registerParser(DesertWell::class.java, DesertWellParser())
            .registerParser(Dungeon::class.java, DungeonParser())
            .registerParser(EndIsland::class.java, EndIslandParser())
            .registerParser(Fossil::class.java, FossilParser())
            .registerParser(Glowstone::class.java, GlowstoneParser())
            .registerParser(IcePath::class.java, IcePathParser())
            .registerParser(IceSpike::class.java, IceSpikeParser())
            .registerParser(Lake::class.java, LakeParser())
            .registerParser(NetherFire::class.java, NetherFireParser())
            .registerParser(Pumpkin::class.java, PumpkinParser())
            .registerParser(Reed::class.java, ReedParser())
            .registerParser(Vine::class.java, VineParser())
            .registerParser(WaterLily::class.java, WaterLilyParser())
            .registerParser(Populator::class.java, PopulatorParser())
            ///////////////////////////////
            /// Utility Objects parsers ///
            ///////////////////////////////
            .registerParser(object : TypeToken<DoubleRange>() {}, DoubleRangeParser())
            .registerParser(IntRange::class.java, IntRangeParser())
            .build()
}
