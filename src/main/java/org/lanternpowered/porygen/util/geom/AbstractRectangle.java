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
package org.lanternpowered.porygen.util.geom;

import com.google.common.base.MoreObjects;

import java.util.Objects;

@SuppressWarnings({"RedundantIfStatement", "SimplifiableIfStatement"})
abstract class AbstractRectangle<T extends Comparable<T>> extends AbstractShape implements Rectangle<T> {

    final T min;
    final T max;

    AbstractRectangle(T min, T max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public T getMin() {
        return this.min;
    }

    @Override
    public T getMax() {
        return this.max;
    }

    @Override
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        int inside = 0;
        // If more than 0, but less than 3 points
        // are inside, then there is an intersection
        if (contains(minX, minY)) {
            inside++;
        }
        if (contains(maxX, minY)) {
            inside++;
        }
        if (contains(maxX, maxY) && ++inside == 3) {
            return false;
        }
        if (contains(minX, maxY) && ++inside == 3) {
            return false;
        }
        return inside > 0;
    }

    @Override
    public boolean contains(double minX, double minY, double maxX, double maxY) {
        return contains(minX, minY) && contains(maxX, maxY);
    }

    @Override
    public boolean contains(int minX, int minY, int maxX, int maxY) {
        return contains(minX, minY) && contains(maxX, maxY);
    }

    @Override
    public boolean intersects(Polygond polygon) {
        return polygon.intersects(this);
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
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        final AbstractRectangle other = (AbstractRectangle) object;
        return other.min.equals(this.min) && other.max.equals(this.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.min, this.max);
    }
 }
