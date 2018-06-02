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
package org.lanternpowered.porygen.parser;

import org.lanternpowered.porygen.settings.DataKeyValueMap;
import org.lanternpowered.porygen.settings.DataManipulatorList;
import org.lanternpowered.porygen.settings.ParentBasedBiomeGenerationSettings;
import org.lanternpowered.porygen.parser.types.BlockStateParser;
import org.lanternpowered.porygen.parser.types.CatalogTypeParser;
import org.lanternpowered.porygen.parser.types.DoublePredicateParser;
import org.lanternpowered.porygen.parser.types.EntityArchetypeParser;
import org.lanternpowered.porygen.parser.types.GroundCoverLayerParser;
import org.lanternpowered.porygen.parser.types.ItemStackParser;
import org.lanternpowered.porygen.parser.types.ItemStackSnapshotParser;
import org.lanternpowered.porygen.parser.types.ParentBasedBiomeGenerationSettingsParser;
import org.lanternpowered.porygen.parser.types.data.DataKeyValueMapParser;
import org.lanternpowered.porygen.parser.types.data.DataManipulatorListParser;
import org.lanternpowered.porygen.parser.types.populator.BigMushroomParser;
import org.lanternpowered.porygen.parser.types.populator.BlockBlobParser;
import org.lanternpowered.porygen.parser.types.populator.CactusParser;
import org.lanternpowered.porygen.parser.types.populator.ChorusFlowerParser;
import org.lanternpowered.porygen.parser.types.populator.DeadBushParser;
import org.lanternpowered.porygen.parser.types.populator.DesertWellParser;
import org.lanternpowered.porygen.parser.types.populator.DungeonParser;
import org.lanternpowered.porygen.parser.types.populator.EndIslandParser;
import org.lanternpowered.porygen.parser.types.populator.FossilParser;
import org.lanternpowered.porygen.parser.types.populator.GlowstoneParser;
import org.lanternpowered.porygen.parser.types.populator.IcePathParser;
import org.lanternpowered.porygen.parser.types.populator.IceSpikeParser;
import org.lanternpowered.porygen.parser.types.populator.LakeParser;
import org.lanternpowered.porygen.parser.types.populator.NetherFireParser;
import org.lanternpowered.porygen.parser.types.populator.PopulatorParser;
import org.lanternpowered.porygen.parser.types.populator.PumpkinParser;
import org.lanternpowered.porygen.parser.types.populator.ReedParser;
import org.lanternpowered.porygen.parser.types.populator.VineParser;
import org.lanternpowered.porygen.parser.types.populator.WaterLilyParser;
import org.lanternpowered.porygen.parser.types.util.RangedParser;
import org.lanternpowered.porygen.parser.types.util.RangeiParser;
import org.lanternpowered.porygen.parser.types.util.weighted.ChanceTableParser;
import org.lanternpowered.porygen.parser.types.util.weighted.LootTableParser;
import org.lanternpowered.porygen.parser.types.util.weighted.RandomObjectTableParser;
import org.lanternpowered.porygen.parser.types.util.weighted.SeededVariableAmountParser;
import org.lanternpowered.porygen.parser.types.util.weighted.TableEntryParser;
import org.lanternpowered.porygen.parser.types.util.weighted.VariableAmountParser;
import org.lanternpowered.porygen.parser.types.util.weighted.WeightedTableParser;
import org.lanternpowered.porygen.util.Ranged;
import org.lanternpowered.porygen.util.Rangei;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.weighted.ChanceTable;
import org.spongepowered.api.util.weighted.LootTable;
import org.spongepowered.api.util.weighted.RandomObjectTable;
import org.spongepowered.api.util.weighted.SeededVariableAmount;
import org.spongepowered.api.util.weighted.TableEntry;
import org.spongepowered.api.util.weighted.VariableAmount;
import org.spongepowered.api.util.weighted.WeightedTable;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.populator.BigMushroom;
import org.spongepowered.api.world.gen.populator.BlockBlob;
import org.spongepowered.api.world.gen.populator.Cactus;
import org.spongepowered.api.world.gen.populator.ChorusFlower;
import org.spongepowered.api.world.gen.populator.DeadBush;
import org.spongepowered.api.world.gen.populator.DesertWell;
import org.spongepowered.api.world.gen.populator.Dungeon;
import org.spongepowered.api.world.gen.populator.EndIsland;
import org.spongepowered.api.world.gen.populator.Fossil;
import org.spongepowered.api.world.gen.populator.Glowstone;
import org.spongepowered.api.world.gen.populator.IcePath;
import org.spongepowered.api.world.gen.populator.IceSpike;
import org.spongepowered.api.world.gen.populator.Lake;
import org.spongepowered.api.world.gen.populator.NetherFire;
import org.spongepowered.api.world.gen.populator.Pumpkin;
import org.spongepowered.api.world.gen.populator.Reed;
import org.spongepowered.api.world.gen.populator.Vine;
import org.spongepowered.api.world.gen.populator.WaterLily;

import java.util.function.DoublePredicate;

public final class GlobalPory {

    public static final Pory PORY = new Pory()
            .registerParser(BlockState.class, new BlockStateParser())
            .registerParser(CatalogType.class, new CatalogTypeParser<>())
            .registerParser(DoublePredicate.class, new DoublePredicateParser())
            .registerParser(GroundCoverLayer.class, new GroundCoverLayerParser())
            .registerParser(ParentBasedBiomeGenerationSettings.class, new ParentBasedBiomeGenerationSettingsParser())
            .registerParser(ItemStack.class, new ItemStackParser())
            .registerParser(ItemStackSnapshot.class, new ItemStackSnapshotParser())
            .registerParser(DataKeyValueMap.class, new DataKeyValueMapParser())
            .registerParser(DataManipulatorList.class, new DataManipulatorListParser())
            .registerParser(EntityArchetype.class, new EntityArchetypeParser())
            ///////////////////////////////
            /// Weighted Objects/Values ///
            ///////////////////////////////
            .registerParser(ChanceTable.class, new ChanceTableParser<>())
            .registerParser(WeightedTable.class, new WeightedTableParser<>())
            .registerParser(LootTable.class, new LootTableParser<>())
            .registerParser(RandomObjectTable.class, new RandomObjectTableParser<>())
            .registerParser(TableEntry.class, new TableEntryParser<>())
            .registerParser(SeededVariableAmount.class, new SeededVariableAmountParser<>())
            .registerParser(VariableAmount.class, new VariableAmountParser())
            /////////////////////////
            /// Populator parsers ///
            /////////////////////////
            .registerParser(BigMushroom.class, new BigMushroomParser())
            .registerParser(BlockBlob.class, new BlockBlobParser())
            .registerParser(Cactus.class, new CactusParser())
            .registerParser(ChorusFlower.class, new ChorusFlowerParser())
            .registerParser(DeadBush.class, new DeadBushParser())
            .registerParser(DesertWell.class, new DesertWellParser())
            .registerParser(Dungeon.class, new DungeonParser())
            .registerParser(EndIsland.class, new EndIslandParser())
            .registerParser(Fossil.class, new FossilParser())
            .registerParser(Glowstone.class, new GlowstoneParser())
            .registerParser(IcePath.class, new IcePathParser())
            .registerParser(IceSpike.class, new IceSpikeParser())
            .registerParser(Lake.class, new LakeParser())
            .registerParser(NetherFire.class, new NetherFireParser())
            .registerParser(Pumpkin.class, new PumpkinParser())
            .registerParser(Reed.class, new ReedParser())
            .registerParser(Vine.class, new VineParser())
            .registerParser(WaterLily.class, new WaterLilyParser())
            .registerParser(Populator.class, new PopulatorParser())
            ///////////////////////////////
            /// Utility Objects parsers ///
            ///////////////////////////////
            .registerParser(Ranged.class, new RangedParser())
            .registerParser(Rangei.class, new RangeiParser());
}
