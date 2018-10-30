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
package org.lanternpowered.porygen.populator

import com.google.common.base.MoreObjects
import org.lanternpowered.porygen.catalog.AbstractCatalogType
import org.spongepowered.api.text.translation.FixedTranslation
import org.spongepowered.api.text.translation.Translation
import org.spongepowered.api.world.gen.Populator
import org.spongepowered.api.world.gen.PopulatorType

/**
 * A [PopulatorType] implementation that will be
 * used for all Porygen provided populator types.
 *
 * @param T The populator class type
 */
class PorygenPopulatorType<T : Populator> : AbstractCatalogType, PopulatorType {

    /**
     * Gets the [Populator] class of this populator type.
     *
     * @return The populator class
     */
    val populatorClass: Class<T>

    private val theTranslation: Translation by lazy { FixedTranslation(this.name) }

    /**
     * Constructs a new [PorygenPopulatorType] with
     * the given id and populator class.
     *
     * @param id The id
     * @param populatorClass The populator class
     */
    constructor(id: String, populatorClass: Class<T>) : super(id) {
        this.populatorClass = populatorClass
    }

    /**
     * Constructs a new [PorygenPopulatorType] with
     * the given id, name and populator class.
     *
     * @param id The id
     * @param name The name
     * @param populatorClass The populator class
     */
    constructor(id: String, name: String, populatorClass: Class<T>) : super(id, name) {
        this.populatorClass = populatorClass
    }

    override fun getTranslation() = this.theTranslation

    override fun toStringHelper(): MoreObjects.ToStringHelper {
        return super.toStringHelper()
                .add("populatorClass", this.populatorClass.name)
    }
}
