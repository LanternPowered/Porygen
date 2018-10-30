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
package org.lanternpowered.porygen.catalog

import com.google.common.base.MoreObjects
import org.spongepowered.api.CatalogType
import org.spongepowered.api.Sponge
import org.spongepowered.api.plugin.PluginContainer
import java.util.*

/**
 * A abstract class that should be used for all the [CatalogType]s.
 */
abstract class AbstractCatalogType @JvmOverloads protected constructor(id: String, name: String = id) : CatalogType {

    private val id: String

    /**
     * The id of the plugin that constructed this catalog type.
     */
    val pluginId: String

    private val name: String

    init {
        check(id.isNotEmpty()) { "id may not be empty" }
        check(name.isNotEmpty()) { "name may not be empty" }
        this.pluginId = Sponge.getCauseStackManager().currentCause.first(PluginContainer::class.java)
                .orElseThrow {
                    IllegalStateException(
                            "There must be a PluginContainer in the current cause stack while constructing CatalogTypes.")
                }.id
        this.id = this.pluginId + ':'.toString() + id.toLowerCase(Locale.ENGLISH)
        this.name = name
    }

    override fun getId(): String = this.id
    override fun getName(): String = this.name

    protected open fun toStringHelper(): MoreObjects.ToStringHelper {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("name", getName())
    }

    override fun toString(): String = toStringHelper().toString()
}
