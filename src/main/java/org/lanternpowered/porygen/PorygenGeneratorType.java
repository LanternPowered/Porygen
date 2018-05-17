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
package org.lanternpowered.porygen;

import org.lanternpowered.porygen.catalog.AbstractCatalogType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;

public abstract class PorygenGeneratorType extends AbstractCatalogType implements GeneratorType {

    protected static final DataQuery MINIMAL_SPAWN_HEIGHT = DataQuery.of('.', "Lantern.MinimalSpawnHeight");
    protected static final DataQuery GENERATOR_HEIGHT = DataQuery.of('.', "Lantern.GeneratorHeight");

    protected PorygenGeneratorType(String id, String name) {
        super(id, name);
    }

    @Override
    public DataContainer getGeneratorSettings() {
        final DataContainer dataContainer = DataContainer.createNew();
        applyToSettings(dataContainer);
        return dataContainer;
    }

    @Override
    public WorldGenerator createGenerator(World world) {
        // Just generate a base WorldGenerator that we can modify, we want an
        // actual generator type so no WorldGeneratorModifier
        final WorldGenerator generator = GeneratorTypes.FLAT.createGenerator(world);
        applyToGenerator(generator);
        return generator;
    }

    protected abstract void applyToSettings(DataContainer container);

    protected abstract void applyToGenerator(WorldGenerator worldGenerator);
}
