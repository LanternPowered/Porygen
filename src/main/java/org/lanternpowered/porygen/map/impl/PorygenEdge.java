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

import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.Corner;
import org.lanternpowered.porygen.map.Edge;
import org.lanternpowered.porygen.util.geom.Line2d;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class PorygenEdge implements Edge {

    private final Line2d line;
    final Set<PorygenCell> cells = new HashSet<>();
    final Set<PorygenCorner> corners = new HashSet<>();

    PorygenEdge(Line2d line) {
        this.line = line;
    }

    @Override
    public Line2d getLine() {
        return this.line;
    }

    @Override
    public Collection<Cell> getCells() {
        return Collections.unmodifiableSet(this.cells);
    }

    @Override
    public Collection<Corner> getCorners() {
        return Collections.unmodifiableSet(this.corners);
    }
}
