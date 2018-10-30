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
@file:Suppress("FunctionName", "NOTHING_TO_INLINE")

package org.lanternpowered.porygen.populator.`object`

import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.property.block.SolidCubeProperty
import org.spongepowered.api.world.World
import org.spongepowered.api.world.extent.ArchetypeVolume

inline fun ArchetypeVolumePlacementChecker(noinline fn: (volume: ArchetypeVolume, world: World, x: Int, y: Int, z: Int) -> Boolean) =
        object : ArchetypeVolumePlacementChecker {
            override fun canPlaceAt(volume: ArchetypeVolume, world: World, x: Int, y: Int, z: Int) = fn(volume, world, x, y, z)
        }

@FunctionalInterface
interface ArchetypeVolumePlacementChecker {

    /**
     * Checks whether the [ArchetypeVolume] can be placed
     * at the given coordinates in the the [World].
     *
     * @param volume The volume to place
     * @param world The world to place the volume in
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return Whether it can be placed
     */
    fun canPlaceAt(volume: ArchetypeVolume, world: World, x: Int, y: Int, z: Int): Boolean

    companion object {

        /**
         * The volume will always be placeable.
         */
        val ALWAYS = ArchetypeVolumePlacementChecker { _, _, _, _, _ -> true }

        /**
         * Checks whether the bottom layer of the [ArchetypeVolume] can
         * be placed completely on the ground, for every non air block in the
         * archetype should it be placed on a solid block.
         */
        val SOLID_FOUNDATION = ArchetypeVolumePlacementChecker solidFoundation@ { volume, world, x, y, z ->
            // This object cannot be placed at the void level?
            if (y == 0) {
                return@solidFoundation false
            }
            val min = volume.blockMin
            val max = volume.blockMax
            val y1 = min.y // Only check the ground layer
            for (x1 in min.x..max.x) {
                for (z1 in min.z..max.z) {
                    var block = volume.getBlock(x1, y1, z1)
                    val type = block.type
                    if (type === BlockTypes.AIR || type === BlockTypes.STRUCTURE_VOID) {
                        continue
                    }
                    block = world.getBlock(x + x1, y - 1, z + z1)
                    // The foundation block should be solid
                    if (block.getProperty(SolidCubeProperty::class.java).get().value != true) {
                        return@solidFoundation false
                    }
                }
            }
            return@solidFoundation true
        }
    }
}
