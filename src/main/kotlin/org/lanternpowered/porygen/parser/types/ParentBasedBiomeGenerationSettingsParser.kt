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
package org.lanternpowered.porygen.parser.types

import com.google.common.reflect.TypeParameter
import com.google.common.reflect.TypeToken
import org.lanternpowered.porygen.parser.*
import org.lanternpowered.porygen.settings.ListOperation
import org.lanternpowered.porygen.settings.ParentBasedBiomeGenerationSettings
import org.lanternpowered.porygen.api.util.DoubleRange
import org.spongepowered.api.world.biome.GroundCoverLayer
import org.spongepowered.api.world.gen.GenerationPopulator
import org.spongepowered.api.world.gen.Populator
import kotlin.reflect.KClass

class ParentBasedBiomeGenerationSettingsParser : PoryObjectParser<ParentBasedBiomeGenerationSettings> {

    override fun parse(obj: PoryObject, type: TypeToken<ParentBasedBiomeGenerationSettings>, ctx: PoryParserContext):
            ParentBasedBiomeGenerationSettings {
        // Get the id of the parent
        val parent = obj["parent"]?.asString()

        val height = obj.getAsObj<DoubleRange>("height")
        if (height == null && parent == null) {
            throw ParseException("Missing height object")
        }

        val operatedObject = obj.asObj<OperatedPoryObject>()

        val populators = parseList(operatedObject, "populators", Populator::class)
        val genPopulators = parseList(operatedObject, "generation-populators", GenerationPopulator::class)
        val groundCoverLayers = parseList(operatedObject, "ground-cover-layers", GroundCoverLayer::class)

        return ParentBasedBiomeGenerationSettings(parent, populators, genPopulators, groundCoverLayers, height)
    }

    private fun <T : Any> parseList(obj: OperatedPoryObject, key: String, type: KClass<T>): Pair<MutableList<T>, ListOperation>? {
        return obj.tryGetAsObj(key, object : TypeToken<MutableList<T>>() {}.where(object : TypeParameter<T>() {}, type.java))
                .let { list -> Pair(list.obj, ListOperation.parse(list.operations)) }
    }
}
