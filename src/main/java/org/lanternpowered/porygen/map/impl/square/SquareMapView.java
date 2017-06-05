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
package org.lanternpowered.porygen.map.impl.square;

import com.flowpowered.math.vector.Vector2d;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.Map;
import org.lanternpowered.porygen.map.MapView;
import org.lanternpowered.porygen.map.Site;
import org.lanternpowered.porygen.util.Rectangled;

import java.util.List;

public class SquareMapView implements MapView {

    private final Map parent;

    public SquareMapView(Map parent) {
        this.parent = parent;
    }

    @Override
    public Map getParent() {
        return this.parent;
    }

    @Override
    public MapView getSubView(Rectangled rectangle) {
        return null;
    }

    @Override public Cell getCell(Vector2d point) {
        return null;
    }

    @Override public List<Cell> getCells() {
        return null;
    }

    @Override public List<Site> getSites() {
        return null;
    }
}
