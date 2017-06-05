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

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector2i;

import java.util.Objects;

/**
 * Represents a rectangle.
 */
public final class Rectanglei {

    private final Vector2i min;
    private final Vector2i max;

    public Rectanglei(Vector2i min, Vector2i max) {
        checkNotNull(min, "min");
        checkNotNull(max, "max");
        this.min = new Vector2i(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()));
        this.max = new Vector2i(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()));
    }

    public Vector2i getMin() {
        return this.min;
    }

    public Vector2i getMax() {
        return this.max;
    }

    /**
     * Gets whether this rectangle contains the
     * specified coordinates.
     *
     * @param point The point coordinates
     * @return Whether this rectangle contains the coordinates
     */
    public boolean contains(Vector2i point) {
        checkNotNull(point, "points");
        return contains(point.getX(), point.getY());
    }

    /**
     * Gets whether this rectangle contains the
     * specified coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Whether this rectangle contains the coordinates
     */
    public boolean contains(int x, int y) {
        return x <= this.max.getX() && x >= this.min.getX() &&
                y <= this.max.getY() && y >= this.min.getY();
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("min", this.min)
                .add("max", this.max)
                .toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rectanglei)) {
            return false;
        }
        final Rectanglei other = (Rectanglei) object;
        return other.min.equals(this.min) && other.max.equals(this.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.min, this.max);
    }
}
