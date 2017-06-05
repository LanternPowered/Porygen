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
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.Site;

import java.util.List;

/**
 * Created by Seppe on 4/06/2017.
 */
public class SimpleCell implements Cell {

    private final Site site;

    public SimpleCell(Site site) {
        this.site = site;
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public boolean contains(Vector2d point) {
        return false;
    }

    @Override
    public List<Cell> getNeighbors() {
        return null;
    }

    @Override
    public List<Vector2d> getVertices() {
        return null;
    }
}
