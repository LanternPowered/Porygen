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
package org.lanternpowered.porygen.map.impl;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.CellMap;
import org.lanternpowered.porygen.map.CellMapView;
import org.lanternpowered.porygen.map.Site;
import org.lanternpowered.porygen.util.Rectangled;
import org.lanternpowered.porygen.util.Rectanglei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap implements CellMap {

    // All the cells mapped by their center coordinates
    private final Map<Vector2d, PorygenCell> cellsByCenter = new HashMap<>();

    // All the cells mapped by chunk coordinates
    private final Multimap<Vector2i, PorygenCell> cellsByChunk = HashMultimap.create();

    // A cache with all the map views that are currently allocated
    private final LoadingCache<Rectangled, PorygenMapView> mapViewCache = Caffeine.newBuilder()
            .weakValues()
            .removalListener((RemovalListener<Rectangled, PorygenMapView>) (key, value, cause) -> {
                // Cleanup the cells when the MapView is being cleaned up
                if (value == null) {
                    return;
                }
                final List<Cell> cells = value.getCells();
                for (Cell cell : cells) {
                    final PorygenCell c = (PorygenCell) cell;
                    // The view reference got removed
                    c.referencedViews.remove(key);
                    // When all references to the cell get removed, destroy the cell
                    if (c.referencedViews.isEmpty()) {
                        this.cellsByCenter.remove(c.getSite().getCoordinates());
                        c.chunks.forEach(coords -> this.cellsByChunk.remove(coords, c));
                    }
                }
            })
            .build(key -> {
                return null; // TODO
            });

    @Override
    public CellMap getParent() {
        return null;
    }

    @Override
    public CellMapView getSubView(Rectangled rectangle) {
        return null;
    }

    @Override
    public CellMapView getSubView(Rectanglei rectangle) {
        return null;
    }

    @Override
    public Cell getCell(double x, double y) {
        return null;
    }

    @Override
    public List<Cell> getCells() {
        return null;
    }

    @Override
    public List<Site> getSites() {
        return null;
    }
}
