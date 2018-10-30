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
package org.lanternpowered.porygen.populator.`object`

import org.lanternpowered.porygen.populator.PlaceableObject
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import org.spongepowered.api.world.extent.ArchetypeVolume
import java.util.*

class ArchetypeVolumePopulatorObject(
        private val volume: ArchetypeVolume,
        private val placementChecker: ArchetypeVolumePlacementChecker) : PlaceableObject {

    override fun canPlaceAt(world: World, x: Int, y: Int, z: Int): Boolean {
        return this.placementChecker.canPlaceAt(this.volume, world, x, y, z)
    }

    override fun placeAt(world: World, random: Random, x: Int, y: Int, z: Int) {
        val worker = this.volume.blockWorker
        worker.iterate { _, x1, y1, z1 ->
            val x2 = x + x1
            val y2 = y + y1
            val z2 = z + z1
            val state = this.volume.getBlock(x2, y2, z2)
            // Don't place structure void
            if (state.type === BlockTypes.STRUCTURE_VOID) {
                return@iterate
            }
            world.setBlock(x2, y2, z2, state)
        }
        this.volume.tileEntityArchetypes.forEach { pos, tile -> tile.apply(Location(world, pos)) }
    }
}
