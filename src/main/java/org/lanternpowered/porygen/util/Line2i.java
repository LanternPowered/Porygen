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
package org.lanternpowered.porygen.util;

import com.flowpowered.math.vector.Vector2i;

import java.awt.geom.Line2D;

public final class Line2i extends AbstractLine2<Vector2i> {

    public Line2i(int startX, int startY, int endX, int endY) {
        this(new Vector2i(startX, startY), new Vector2i(endX, endY));
    }

    public Line2i(Vector2i start, Vector2i end) {
        super(start, end);
    }

    @Override
    public boolean intersects(double startX, double startY, double endX, double endY) {
        return Line2D.linesIntersect(this.start.getX(), this.start.getY(),
                this.end.getX(), this.end.getY(), startX, startY, endX, endY);
    }

    @Override
    public Line2i toInt() {
        return this;
    }

    @Override
    public Line2d toDouble() {
        return new Line2d(this.start.toDouble(), this.end.toDouble());
    }
}
