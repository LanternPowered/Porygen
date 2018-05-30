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
package org.lanternpowered.porygen.settings;

import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.GenerationPopulator;
import org.spongepowered.api.world.gen.Populator;

import java.util.List;

import javax.annotation.Nullable;

public final class ParentBasedBiomeGenerationSettings {

    private final String parent;

    @Nullable private final Tuple<List<Populator>, ListOperation> populators;
    @Nullable private final Tuple<List<GenerationPopulator>, ListOperation> generationPopulators;
    @Nullable private final Tuple<List<GroundCoverLayer>, ListOperation> groundCoverLayers;

    @Nullable private final Double minHeight;
    @Nullable private final Double maxHeight;

    /**
     * Constructs a new {@link ParentBasedBiomeGenerationSettings}.
     *
     * @param parent The id of the parent generation settings
     * @param populators The populators to apply
     * @param generationPopulators The generation populators to apply
     * @param groundCoverLayers The ground cover layers to apply
     * @param minHeight The minimum height
     * @param maxHeight The maximum height
     */
    public ParentBasedBiomeGenerationSettings(String parent,
            @Nullable Tuple<List<Populator>, ListOperation> populators,
            @Nullable Tuple<List<GenerationPopulator>, ListOperation> generationPopulators,
            @Nullable Tuple<List<GroundCoverLayer>, ListOperation> groundCoverLayers,
            @Nullable Double minHeight, @Nullable Double maxHeight) {
        this.groundCoverLayers = groundCoverLayers;
        this.generationPopulators = generationPopulators;
        this.populators = populators;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.parent = parent;
    }

    /**
     * Constructs the {@link BiomeGenerationSettings} based
     * on the parent {@link BiomeGenerationSettings}.
     *
     * @return The biome generation settings
     */
    public BiomeGenerationSettings construct(@Nullable BiomeGenerationSettings parent) {
        final BiomeGenerationSettings copy = parent == null ? BiomeGenerationSettings.builder().build() : parent.copy();
        if (this.maxHeight != null) {
            copy.setMaxHeight(this.maxHeight.floatValue());
        }
        if (this.minHeight != null) {
            copy.setMinHeight(this.minHeight.floatValue());
        }
        if (this.populators != null) {
            this.populators.getSecond()
                    .merge(copy.getPopulators(), this.populators.getFirst());
        }
        if (this.generationPopulators != null) {
            this.generationPopulators.getSecond()
                    .merge(copy.getGenerationPopulators(), this.generationPopulators.getFirst());
        }
        if (this.groundCoverLayers != null) {
            this.groundCoverLayers.getSecond()
                    .merge(copy.getGroundCoverLayers(), this.groundCoverLayers.getFirst());
        }
        return copy;
    }

    /**
     * Gets the id of the parent {@link BiomeGenerationSettings}.
     *
     * @return The parent
     */
    public String getParent() {
        return this.parent;
    }
}
