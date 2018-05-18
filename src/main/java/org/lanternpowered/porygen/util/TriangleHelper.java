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
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public final class TriangleHelper {

    /**
     * Gets the circumcenter from the given {@link Triangle2D}.
     *
     * @param triangle The triangle
     * @return The circumcenter
     */
    public static Vector2d getCircumcenter(Triangle2D triangle) {
        final Vector2D a = triangle.a;
        final Vector2D b = triangle.b;
        final Vector2D c = triangle.c;

        final Vector2D abCenter = a.add(b).mult(0.5);
        final Vector2D bcCenter = b.add(c).mult(0.5);

        final double abSlope = -1.0 / ((b.y - a.y) / (b.x - a.x));
        final double bcSlope = -1.0 / ((c.y - b.y) / (c.x - b.x));

        final double bAB = abCenter.y - abSlope * abCenter.x;
        final double bBC = bcCenter.y - bcSlope * bcCenter.x;

        final double x = (bAB - bBC) / (bcSlope - abSlope);
        return new Vector2d(x, abSlope * x + bAB);
    }
}
