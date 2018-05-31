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
package org.lanternpowered.porygen.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lanternpowered.porygen.settings.json.BlockStateParser;
import org.lanternpowered.porygen.settings.json.CatalogTypeParser;
import org.lanternpowered.porygen.settings.json.DoublePredicateParser;
import org.lanternpowered.porygen.settings.json.GroundCoverLayerParser;
import org.lanternpowered.porygen.settings.json.ParentBasedBiomeGenerationSettingsParser;
import org.lanternpowered.porygen.settings.json.weighted.ChanceTableParser;
import org.lanternpowered.porygen.settings.json.weighted.LootTableParser;
import org.lanternpowered.porygen.settings.json.weighted.RandomObjectTableParser;
import org.lanternpowered.porygen.settings.json.weighted.SeededVariableAmountParser;
import org.lanternpowered.porygen.settings.json.weighted.TableEntryParser;
import org.lanternpowered.porygen.settings.json.weighted.VariableAmountParser;
import org.lanternpowered.porygen.settings.json.weighted.WeightedTableParser;
import org.lanternpowered.porygen.settings.json.populator.BigMushroomParser;
import org.lanternpowered.porygen.settings.json.populator.BlockBlobParser;
import org.lanternpowered.porygen.settings.json.populator.CactusParser;
import org.lanternpowered.porygen.settings.json.populator.ChorusFlowerParser;
import org.lanternpowered.porygen.settings.json.populator.DeadBushParser;
import org.lanternpowered.porygen.settings.json.populator.DesertWellParser;
import org.lanternpowered.porygen.settings.json.populator.EndIslandParser;
import org.lanternpowered.porygen.settings.json.populator.FossilParser;
import org.lanternpowered.porygen.settings.json.populator.GlowstoneParser;
import org.lanternpowered.porygen.settings.json.populator.IceSpikeParser;
import org.lanternpowered.porygen.settings.json.populator.NetherFireParser;
import org.lanternpowered.porygen.settings.json.populator.PopulatorParser;
import org.lanternpowered.porygen.settings.json.populator.PumpkinParser;
import org.lanternpowered.porygen.settings.json.populator.ReedParser;
import org.lanternpowered.porygen.settings.json.populator.VineParser;
import org.lanternpowered.porygen.settings.json.populator.WaterLilyParser;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.block.BlockState;
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
import org.spongepowered.api.world.gen.populator.EndIsland;
import org.spongepowered.api.world.gen.populator.Fossil;
import org.spongepowered.api.world.gen.populator.Glowstone;
import org.spongepowered.api.world.gen.populator.IceSpike;
import org.spongepowered.api.world.gen.populator.NetherFire;
import org.spongepowered.api.world.gen.populator.Pumpkin;
import org.spongepowered.api.world.gen.populator.Reed;
import org.spongepowered.api.world.gen.populator.Vine;
import org.spongepowered.api.world.gen.populator.WaterLily;

import java.util.function.DoublePredicate;

public final class PorygenGeneratorSettings {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BlockState.class, new BlockStateParser())
            .registerTypeAdapter(CatalogType.class, new CatalogTypeParser<>())
            .registerTypeAdapter(DoublePredicate.class, new DoublePredicateParser())
            .registerTypeAdapter(GroundCoverLayer.class, new GroundCoverLayerParser())
            .registerTypeAdapter(ParentBasedBiomeGenerationSettings.class, new ParentBasedBiomeGenerationSettingsParser())
            ///////////////////////////////
            /// Weighted Objects/Values ///
            ///////////////////////////////
            .registerTypeAdapter(ChanceTable.class, new ChanceTableParser<>())
            .registerTypeAdapter(WeightedTable.class, new WeightedTableParser<>())
            .registerTypeAdapter(LootTable.class, new LootTableParser<>())
            .registerTypeAdapter(RandomObjectTable.class, new RandomObjectTableParser<>())
            .registerTypeAdapter(TableEntry.class, new TableEntryParser<>())
            .registerTypeAdapter(SeededVariableAmount.class, new SeededVariableAmountParser<>())
            .registerTypeAdapter(VariableAmount.class, new VariableAmountParser())
            /////////////////////////
            /// Populator parsers ///
            /////////////////////////
            .registerTypeAdapter(BigMushroom.class, new BigMushroomParser())
            .registerTypeAdapter(BlockBlob.class, new BlockBlobParser())
            .registerTypeAdapter(Cactus.class, new CactusParser())
            .registerTypeAdapter(ChorusFlower.class, new ChorusFlowerParser())
            .registerTypeAdapter(DeadBush.class, new DeadBushParser())
            .registerTypeAdapter(DesertWell.class, new DesertWellParser())
            .registerTypeAdapter(EndIsland.class, new EndIslandParser())
            .registerTypeAdapter(Fossil.class, new FossilParser())
            .registerTypeAdapter(Glowstone.class, new GlowstoneParser())
            .registerTypeAdapter(IceSpike.class, new IceSpikeParser())
            .registerTypeAdapter(NetherFire.class, new NetherFireParser())
            .registerTypeAdapter(Pumpkin.class, new PumpkinParser())
            .registerTypeAdapter(Reed.class, new ReedParser())
            .registerTypeAdapter(Vine.class, new VineParser())
            .registerTypeAdapter(WaterLily.class, new WaterLilyParser())
            .registerTypeAdapter(Populator.class, new PopulatorParser())
            .create();
}
