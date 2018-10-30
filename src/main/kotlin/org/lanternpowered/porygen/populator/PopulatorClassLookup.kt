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
package org.lanternpowered.porygen.populator

import org.spongepowered.api.world.gen.Populator
import org.spongepowered.api.world.gen.PopulatorType
import org.spongepowered.api.world.gen.PopulatorTypes
import org.spongepowered.api.world.gen.populator.*
import java.util.*
import kotlin.reflect.KClass

/**
 * A lookup to get the [Populator]
 * class from a [PopulatorType].
 */
object PopulatorClassLookup {

    private val lookup = HashMap<PopulatorType, Class<out Populator>>()

    init {
        fun add(type: PopulatorType, populatorClass: KClass<out Populator>) {
            this.lookup[type] = populatorClass.java
        }

        add(PopulatorTypes.BIG_MUSHROOM, BigMushroom::class)
        add(PopulatorTypes.BLOCK_BLOB, BlockBlob::class)
        add(PopulatorTypes.CACTUS, Cactus::class)
        add(PopulatorTypes.CHORUS_FLOWER, ChorusFlower::class)
        add(PopulatorTypes.DEAD_BUSH, DeadBush::class)
        add(PopulatorTypes.DESERT_WELL, DesertWell::class)
        add(PopulatorTypes.DOUBLE_PLANT, DoublePlant::class)
        add(PopulatorTypes.DUNGEON, Dungeon::class)
        add(PopulatorTypes.END_ISLAND, EndIsland::class)
        add(PopulatorTypes.FLOWER, Flower::class)
        add(PopulatorTypes.FOREST, Forest::class)
        add(PopulatorTypes.FOSSIL, Fossil::class)
        add(PopulatorTypes.GENERIC_BLOCK, RandomBlock::class)
        add(PopulatorTypes.GENERIC_OBJECT, RandomObject::class)
        add(PopulatorTypes.GLOWSTONE, Glowstone::class)
        add(PopulatorTypes.ICE_PATH, IcePath::class)
        add(PopulatorTypes.ICE_SPIKE, IceSpike::class)
        add(PopulatorTypes.LAKE, Lake::class)
        add(PopulatorTypes.MELON, Melon::class)
        add(PopulatorTypes.MUSHROOM, Mushroom::class)
        add(PopulatorTypes.NETHER_FIRE, NetherFire::class)
        add(PopulatorTypes.ORE, Ore::class)
        add(PopulatorTypes.PUMPKIN, Pumpkin::class)
        add(PopulatorTypes.REED, Reed::class)
        add(PopulatorTypes.SEA_FLOOR, SeaFloor::class)
        add(PopulatorTypes.SHRUB, Shrub::class)
        add(PopulatorTypes.VINE, Vine::class)
        add(PopulatorTypes.WATER_LILY, WaterLily::class)
    }

    /**
     * Gets the [Populator] class for the given [PopulatorType].
     *
     * @param populatorType The populator type
     * @return The populator class
     */
    fun getPopulatorClass(populatorType: PopulatorType): Class<out Populator> {
        if (populatorType is PorygenPopulatorType<*>) {
            return populatorType.populatorClass
        }
        val populatorClass = this.lookup[populatorType]
        return checkNotNull(populatorClass) { "Cannot find a populator class mapped to: ${populatorType.id}" }
    }
}
