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
package org.lanternpowered.porygen.populator;

import static com.google.common.base.Preconditions.checkNotNull;

import org.lanternpowered.porygen.catalog.AbstractCatalogType;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.PopulatorType;

import java.util.Random;

/**
 * A {@link PopulatorType} implementation that will be
 * used for all Porygen provided populator objects.
 */
public final class PorygenPopulatorObject extends AbstractCatalogType implements PopulatorObject {

    private final PlaceableObject object;

    /**
     * Constructs a new {@link PorygenPopulatorObject} for
     * the given id and {@link PlaceableObject}.
     *
     * @param id The id
     * @param object The placeable object
     */
    public PorygenPopulatorObject(String id, PlaceableObject object) {
        super(id);
        checkNotNull(object, "object");
        this.object = object;
    }

    /**
     * Constructs a new {@link PorygenPopulatorObject} for
     * the given id, name and {@link PlaceableObject}.
     *
     * @param id The id
     * @param name The name
     * @param object The placeable object
     */
    public PorygenPopulatorObject(String id, String name, PlaceableObject object) {
        super(id, name);
        checkNotNull(object, "object");
        this.object = object;
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z) {
        return this.object.canPlaceAt(world, x, y, z);
    }

    @Override
    public void placeObject(World world, Random random, int x, int y, int z) {
        this.object.placeAt(world, random, x, y, z);
    }
}
