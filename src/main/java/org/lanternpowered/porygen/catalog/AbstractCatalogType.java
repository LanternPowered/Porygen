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
package org.lanternpowered.porygen.catalog;

import static org.lanternpowered.porygen.util.Conditions.checkNotNullOrEmpty;

import com.google.common.base.Objects;
import org.spongepowered.api.CatalogType;

import java.util.Locale;

/**
 * A abstract class that should be used for all the {@link CatalogType}s.
 */
public abstract class AbstractCatalogType implements CatalogType {

    private final String id;
    private final String pluginId;
    private final String name;

    protected AbstractCatalogType(String pluginId, String name) {
        this(pluginId, name, name);
    }

    protected AbstractCatalogType(String pluginId, String id, String name) {
        this.pluginId = checkNotNullOrEmpty(pluginId, "pluginId").toLowerCase(Locale.ENGLISH);
        this.id = this.pluginId + ':' + checkNotNullOrEmpty(id, "id").toLowerCase(Locale.ENGLISH);
        this.name = checkNotNullOrEmpty(name, "name");
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Gets the identifier of the plugin.
     *
     * @return The plugin id
     */
    public String getPluginId() {
        return this.pluginId;
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .add("id", getId())
                .add("name", getName());
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }
}
