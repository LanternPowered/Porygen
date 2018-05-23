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
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

public abstract class PorygenGeneratorType extends AbstractCatalogType implements WorldGeneratorModifier {

    protected static final DataQuery MINIMAL_SPAWN_HEIGHT = DataQuery.of('.', "Lantern.MinimalSpawnHeight");
    protected static final DataQuery GENERATOR_HEIGHT = DataQuery.of('.', "Lantern.GeneratorHeight");

    protected PorygenGeneratorType(String id, String name) {
        super(id, name);
    }

    /**
     * Applies the default settings to the given {@link DataView}.
     *
     * @param dataView The data view
     */
    protected abstract void applyToSettings(DataView dataView);
}
