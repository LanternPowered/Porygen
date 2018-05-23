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

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

/**
 * Wraps around a {@link PorygenGeneratorType} which implements
 * a {@link WorldGeneratorModifier} to support {@link GeneratorType}s
 * on lantern or other platforms that support custom {@link GeneratorType}s.
 */
final class GeneratorModifierToType implements GeneratorType {

    private final PorygenGeneratorType generatorType;

    GeneratorModifierToType(PorygenGeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    @Override
    public DataContainer getGeneratorSettings() {
        final DataContainer dataContainer = DataContainer.createNew();
        this.generatorType.applyToSettings(dataContainer);
        return dataContainer;
    }

    @Override
    public WorldGenerator createGenerator(World world) {
        final WorldGenerator generator = GeneratorTypes.FLAT.createGenerator(world);
        this.generatorType.modifyWorldGenerator(world.getProperties(), world.getProperties().getGeneratorSettings(), generator);
        return generator;
    }

    @Override
    public String getId() {
        return this.generatorType.getId();
    }

    @Override
    public String getName() {
        return this.generatorType.getName();
    }
}
