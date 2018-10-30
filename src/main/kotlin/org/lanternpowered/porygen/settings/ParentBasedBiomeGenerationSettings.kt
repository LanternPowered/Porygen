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
package org.lanternpowered.porygen.settings

import org.spongepowered.api.world.biome.BiomeGenerationSettings
import org.spongepowered.api.world.biome.GroundCoverLayer
import org.spongepowered.api.world.gen.GenerationPopulator
import org.spongepowered.api.world.gen.Populator

/**
 * @property parent The id of the parent generation settings
 * @property populators The populators to apply
 * @property generationPopulators The generation populators to apply
 * @property groundCoverLayers The ground cover layers to apply
 * @property height The height
 */
class ParentBasedBiomeGenerationSettings(
        val parent: String?,
        private val populators: Pair<MutableList<Populator>, ListOperation>?,
        private val generationPopulators: Pair<MutableList<GenerationPopulator>, ListOperation>?,
        private val groundCoverLayers: Pair<MutableList<GroundCoverLayer>, ListOperation>?,
        private val height: ClosedFloatingPointRange<Double>?
) {

    /**
     * Constructs the [BiomeGenerationSettings] based
     * on the parent [BiomeGenerationSettings].
     *
     * @return The biome generation settings
     */
    fun construct(parent: BiomeGenerationSettings?): BiomeGenerationSettings {
        val copy = parent?.copy() ?: BiomeGenerationSettings.builder().build()
        if (this.height != null) {
            copy.maxHeight = this.height.endInclusive.toFloat()
            copy.minHeight = this.height.start.toFloat()
        }
        if (this.populators != null) {
            this.populators.second.merge(copy.populators, this.populators.first)
        }
        if (this.generationPopulators != null) {
            this.generationPopulators.second.merge(copy.generationPopulators, this.generationPopulators.first)
        }
        if (this.groundCoverLayers != null) {
            this.groundCoverLayers.second.merge(copy.groundCoverLayers, this.groundCoverLayers.first)
        }
        return copy
    }
}
