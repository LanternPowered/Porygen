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

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.DataKey;
import org.lanternpowered.porygen.map.Edge;
import org.lanternpowered.porygen.map.Site;
import org.lanternpowered.porygen.util.geom.Polygond;
import org.lanternpowered.porygen.util.geom.Rectangled;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PorygenCell implements Cell {

    private final Site site;
    private final Polygond polygon;

    // A set with all the referenced views
    final Set<Rectangled> referencedViews = new HashSet<>();

    // A set with all the chunk coordinates this cell is located in
    final Set<Vector2i> chunks = new HashSet<>();

    PorygenCell(Vector2d center, Polygond polygon) {
        this.site = new SimpleSite(center, this);
        this.polygon = polygon;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Vector2d vertex : polygon.getVertices()) {
            minX = Math.min(minX, vertex.getX());
            maxX = Math.max(maxX, vertex.getX());
            minY = Math.min(minY, vertex.getY());
            maxY = Math.max(maxY, vertex.getY());
        }

        final int cStartX = GenericMath.floor(minX) >> 4;
        final int cStartY = GenericMath.floor(minY) >> 4;
        final int cEndX = GenericMath.floor(maxX) >> 4;
        final int cEndY = GenericMath.floor(maxY) >> 4;

        for (int x = cStartX; x <= cEndX; x++) {
            for (int y = cStartY; y <= cEndY; y++) {
            }
        }
    }

    @Override public <T> Optional<T> get(DataKey<T> key) {
        return null;
    }

    @Override public <T> void set(DataKey<T> key, T object) {

    }

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public boolean contains(double x, double y) {
        return this.polygon.contains(x, y);
    }

    @Override
    public Polygond getPolygon() {
        return this.polygon;
    }

    @Override
    public List<Cell> getNeighbors() {
        return null;
    }

    @Override public List<Edge> getEdges() {
        return null;
    }
}
