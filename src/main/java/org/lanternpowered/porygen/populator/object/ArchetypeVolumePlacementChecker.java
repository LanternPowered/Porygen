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
package org.lanternpowered.porygen.populator.object;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.SolidCubeProperty;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ArchetypeVolume;

@SuppressWarnings("ConstantConditions")
@FunctionalInterface
public interface ArchetypeVolumePlacementChecker {

    /**
     * The volume will always be placeable.
     */
    ArchetypeVolumePlacementChecker ALWAYS = (volume, world, x, y, z) -> true;

    /**
     * Checks whether the bottom layer of the {@link ArchetypeVolume} can
     * be placed completely on the ground, for every non air block in the
     * archetype should it be placed on a solid block.
     */
    ArchetypeVolumePlacementChecker SOLID_FOUNDATION = (volume, world, x, y, z) -> {
        // This object cannot be placed at the void level?
        if (y == 0) {
            return false;
        }
        final Vector3i min = volume.getBlockMin();
        final Vector3i max = volume.getBlockMax();
        final int y1 = min.getY(); // Only check the ground layer
        for (int x1 = min.getX(); x1 <= max.getX(); x1++) {
            for (int z1 = min.getZ(); z1 <= max.getZ(); z1++) {
                BlockState block = volume.getBlock(x1, y1, z1);
                final BlockType type = block.getType();
                if (type == BlockTypes.AIR || type == BlockTypes.STRUCTURE_VOID) {
                    continue;
                }
                block = world.getBlock(x + x1, y - 1, z + z1);
                // The foundation block should be solid
                if (!block.getProperty(SolidCubeProperty.class).get().getValue()) {
                    return false;
                }
            }
        }
        return true;
    };

    /**
     * Checks whether the {@link ArchetypeVolume} can be placed
     * at the given coordinates in the the {@link World}.
     *
     * @param volume The volume to place
     * @param world The world to place the volume in
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return Whether it can be placed
     */
    boolean canPlaceAt(ArchetypeVolume volume, World world, int x, int y, int z);
}
