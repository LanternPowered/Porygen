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
package org.lanternpowered.porygen.api.map.gen.biome

import org.lanternpowered.porygen.api.data.DataKey
import org.lanternpowered.porygen.api.map.CellMap
import org.spongepowered.api.world.biome.BiomeType
import org.spongepowered.api.world.biome.BiomeTypes
import org.spongepowered.api.world.extent.MutableBiomeVolume
import org.spongepowered.api.world.gen.BiomeGenerator

/**
 * A [BiomeGenerator] which uses a single biome per cell.
 */
class SingleCellBiomeGenerator(
        private val map: CellMap
) : BiomeGenerator {

    override fun generateBiomes(buffer: MutableBiomeVolume) {
        buffer.biomeWorker.fill { x, _, z ->
            val cell = this.map.getCell(x, z)
            cell[BIOME_DATA_KEY] ?: BiomeTypes.PLAINS
        }
    }

    companion object {

        /**
         * A key which represents the biome to be used for a specific cell.
         */
        val BIOME_DATA_KEY = DataKey<BiomeType>()
    }
}
