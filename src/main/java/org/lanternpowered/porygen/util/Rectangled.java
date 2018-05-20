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

import com.flowpowered.math.vector.Vector2d;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.awt.Polygon;

public final class Rectangled extends AbstractRectangle<Vector2d> {

    public Rectangled(double minX, double minY, double maxX, double maxY) {
        super(new Vector2d(Math.min(minX, maxX), Math.min(minY, maxY)),
                new Vector2d(Math.max(minX, maxX), Math.max(minY, maxY)));
    }

    public Rectangled(Vector2d min, Vector2d max) {
        super(new Vector2d(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY())),
                new Vector2d(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY())));
    }

    @Override
    public boolean contains(double x, double y) {
        return x <= this.max.getX() && x >= this.min.getX() &&
                y <= this.max.getY() && y >= this.min.getY();
    }

    /**
     * Converts this {@link Rectangled} into
     * a drawable {@link Polygon}.
     *
     * @return The drawable polygon
     */
    public Polygon toDrawable() {
        final int[] pointsX = new int[4];
        final int[] pointsY = new int[4];
        pointsX[0] = (int) this.min.getX();
        pointsY[0] = (int) this.min.getY();
        pointsX[1] = (int) this.max.getX();
        pointsY[1] = (int) this.min.getY();
        pointsX[2] = (int) this.max.getX();
        pointsY[2] = (int) this.max.getY();
        pointsX[3] = (int) this.min.getX();
        pointsY[3] = (int) this.max.getY();
        return new Polygon(pointsX, pointsY, 4);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("min", this.min)
                .add("max", this.max)
                .toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rectangled)) {
            return false;
        }
        final Rectangled other = (Rectangled) object;
        return other.min.equals(this.min) && other.max.equals(this.max);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.min, this.max);
    }
}
