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

import com.flowpowered.math.vector.Vector2d;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public final class TriangleHelper {

    private static boolean testSign(double x, double y, Vector2D b, Vector2D c) {
        return (x - c.x) * (b.y - c.y) - (b.x - c.x) * (y - c.y) < 0;
    }

    /**
     * Gets whether the given point {@link Vector2D} is located inside the triangle.
     *
     * @param point The point
     * @param triangle The triangle
     * @return Is inside
     */
    public static boolean isInside(Vector2d point, Triangle2D triangle) {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        // Yeah, I know, not optimized, worries for later
        final boolean b = testSign(point.getX(), point.getY(), triangle.a, triangle.b);
        return b == testSign(point.getX(), point.getY(), triangle.b, triangle.c) &&
                b == testSign(point.getX(), point.getY(), triangle.c, triangle.a);
    }

    /**
     * Gets whether the given point {@link Vector2D} is located inside the triangle.
     *
     * @param point The point
     * @param triangle The triangle
     * @return Is inside
     */
    public static boolean isInside(Vector2D point, Triangle2D triangle) {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        // Yeah, I know, not optimized, worries for later
        final boolean b = testSign(point.x, point.y, triangle.a, triangle.b);
        return b == testSign(point.x, point.y, triangle.b, triangle.c) &&
                b == testSign(point.x, point.y, triangle.c, triangle.a);
    }

    /**
     * Gets the incenter from the given {@link Triangle2D}.
     *
     * @param triangle The triangle
     * @return The incenter
     */
    public static Vector2d getIncenter(Triangle2D triangle) {
        // https://www.mathopenref.com/coordincenter.html
        final Vector2D a = triangle.a;
        final Vector2D b = triangle.b;
        final Vector2D c = triangle.c;

        double dx = b.x - c.x;
        double dy = b.y - c.y;
        final double da = Math.sqrt(dx * dx + dy * dy);

        dx = c.x - a.x;
        dy = c.y - a.y;
        final double db = Math.sqrt(dx * dx + dy * dy);

        dx = a.x - b.x;
        dy = a.y - b.y;
        final double dc = Math.sqrt(dx * dx + dy * dy);

        final double dp = da + db + dc;
        final double ox = (da * a.x + db * b.x + dc * c.x) / dp;
        final double oy = (da * a.y + db * b.y + dc * c.y) / dp;

        return new Vector2d(ox, oy);
    }

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

    /**
     * Gets the centroid from the given {@link Triangle2D}.
     *
     * @param triangle The triangle
     * @return The centroid
     */
    public static Vector2d getCentroid(Triangle2D triangle) {
        return new Vector2d(
                (triangle.a.x + triangle.b.x + triangle.c.x) / 3.0,
                (triangle.a.y + triangle.b.y + triangle.c.y) / 3.0);
    }
}
