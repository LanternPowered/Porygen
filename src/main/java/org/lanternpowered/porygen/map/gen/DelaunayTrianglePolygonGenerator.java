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
package org.lanternpowered.porygen.map.gen;

import com.flowpowered.math.vector.Vector2d;
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.util.geom.Polygond;
import org.lanternpowered.porygen.util.geom.Rectangled;
import org.lanternpowered.porygen.util.geom.TriangleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates {@link Cell}s that are delaunay triangles.
 */
public class DelaunayTrianglePolygonGenerator implements CenteredPolygonGenerator {

    @Override
    public List<CenteredPolygon> generate(GeneratorContext context, Rectangled rectangle, List<Vector2d> points) {
        final List<CenteredPolygon> centeredPolygons = new ArrayList<>();
        final List<Vector2D> pointSet = points.stream()
                .map(v -> new Vector2D(v.getX(), v.getY()))
                .collect(Collectors.toList());

        try {
            final DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
            delaunayTriangulator.triangulate();

            final List<Triangle2D> triangles = delaunayTriangulator.getTriangles();
            for (Triangle2D triangle : triangles) {
                final Vector2d center = TriangleHelper.getCircumcenter(triangle);
                final Polygond polygon = Polygond.newConvexPolygon(
                        new Vector2d(triangle.a.x, triangle.a.y),
                        new Vector2d(triangle.b.x, triangle.b.y),
                        new Vector2d(triangle.c.x, triangle.c.y));
                centeredPolygons.add(new CenteredPolygon(center, polygon));
            }
        } catch (NotEnoughPointsException e) {
            throw new RuntimeException(e);
        }

        return centeredPolygons;
    }
}
