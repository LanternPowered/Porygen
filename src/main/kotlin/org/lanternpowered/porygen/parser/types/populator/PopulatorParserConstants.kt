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

import org.spongepowered.api.util.weighted.LootTable
import org.spongepowered.api.world.gen.PopulatorObject

object PopulatorParserConstants {

    internal const val PER_CHUNK = "per-chunk"
    internal const val RADIUS = "radius"
    internal const val WEIGHTED_OBJECT = "weighted-object"
    internal const val BLOCK = "block"

    /**
     * The height of the populated object.
     */
    internal const val HEIGHT = "height"

    /**
     * The height that will be added to it's base [.HEIGHT] when
     * a object is placed in it's extreme form.
     */
    internal const val EXTREME_HEIGHT_INCREASE = "extreme-height"

    /**
     * The chance that a object will be placed in it's extreme form.
     */
    internal const val EXTREME_CHANCE = "extreme-chance"

    /**
     * The height at which a object will be populated.
     */
    internal const val SPAWN_HEIGHT = "spawn-height"

    internal const val EXCLUSION_RADIUS = "exclusion-radius"

    /**
     * When a [PopulatorObject] will be used.
     */
    internal const val OBJECT = "object"
    internal const val CHANCE = "chance"
    internal const val STARTING_RADIUS = "starting-radius"
    internal const val RADIUS_DECREMENT = "radius-decrement"
    internal const val PER_CLUSTER = "per-cluster"

    /**
     * A [LootTable] for chests.
     */
    internal const val CHEST_LOOT = "chest-loot"

    /**
     * Related to mob spawners.
     */
    internal const val SPAWNER = "spawner"
}
