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

import com.google.common.base.Preconditions.checkNotNull

import org.lanternpowered.porygen.catalog.AbstractCatalogType
import org.spongepowered.api.world.World
import org.spongepowered.api.world.gen.PopulatorObject
import org.spongepowered.api.world.gen.PopulatorType

import java.util.Random

/**
 * A [PopulatorType] implementation that will be
 * used for all Porygen provided populator objects.
 */
class PorygenPopulatorObject : AbstractCatalogType, PopulatorObject {

    private val placeableObject: PlaceableObject

    /**
     * Constructs a new [PorygenPopulatorObject] for
     * the given id and [PlaceableObject].
     *
     * @param id The id
     * @param placeableObject The placeable object
     */
    constructor(id: String, placeableObject: PlaceableObject) : super(id) {
        checkNotNull(placeableObject, "object")
        this.placeableObject = placeableObject
    }

    /**
     * Constructs a new [PorygenPopulatorObject] for
     * the given id, name and [PlaceableObject].
     *
     * @param id The id
     * @param name The name
     * @param placeableObject The placeable object
     */
    constructor(id: String, name: String, placeableObject: PlaceableObject) : super(id, name) {
        checkNotNull(placeableObject, "object")
        this.placeableObject = placeableObject
    }

    override fun canPlaceAt(world: World, x: Int, y: Int, z: Int): Boolean {
        return this.placeableObject.canPlaceAt(world, x, y, z)
    }

    override fun placeObject(world: World, random: Random, x: Int, y: Int, z: Int) {
        this.placeableObject.placeAt(world, random, x, y, z)
    }
}
