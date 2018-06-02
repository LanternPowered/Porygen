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
package org.lanternpowered.porygen.parser.types.populator;

import org.spongepowered.api.util.weighted.LootTable;
import org.spongepowered.api.world.gen.PopulatorObject;

public class PopulatorParserConstants {

    static final String PER_CHUNK = "per-chunk";
    static final String RADIUS = "radius";
    static final String WEIGHTED_OBJECT = "weighted-object";
    static final String BLOCK = "block";

    /**
     * The height of the populated object.
     */
    static final String HEIGHT = "height";

    /**
     * The height that will be added to it's base {@link #HEIGHT} when
     * a object is placed in it's extreme form.
     */
    static final String EXTREME_HEIGHT_INCREASE = "extreme-height";

    /**
     * The chance that a object will be placed in it's extreme form.
     */
    static final String EXTREME_CHANCE = "extreme-chance";

    /**
     * The height at which a object will be populated.
     */
    static final String SPAWN_HEIGHT = "spawn-height";

    static final String EXCLUSION_RADIUS = "exclusion-radius";

    /**
     * When a {@link PopulatorObject} will be used.
     */
    static final String OBJECT = "object";
    static final String CHANCE = "chance";
    static final String STARTING_RADIUS = "starting-radius";
    static final String RADIUS_DECREMENT = "radius-decrement";
    static final String PER_CLUSTER = "per-cluster";

    /**
     * A {@link LootTable} for chests.
     */
    static final String CHEST_LOOT = "chest-loot";

    /**
     * Related to mob spawners.
     */
    static final String SPAWNER = "spawner";
}
