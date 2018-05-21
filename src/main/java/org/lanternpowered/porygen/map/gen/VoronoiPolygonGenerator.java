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

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector2d;
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.util.geom.Polygond;
import org.lanternpowered.porygen.util.geom.Rectangled;
import org.lanternpowered.porygen.util.geom.TriangleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VoronoiPolygonGenerator implements CenteredPolygonGenerator {

    // The function to use to generate the center point of a triangle,
    // by default is a circumcenter used, this is center that should be
    // used in a real voronoi diagram
    private Function<Triangle2D, Vector2d> triangleCenterProvider = TriangleHelper::getCircumcenter;
    // Whether the usage of the triangle center provider will result in convex polygons
    private boolean isConvex = true;

    /**
     * Gets the triangle center provider.
     *
     * @return The triangle center provider
     */
    public Function<Triangle2D, Vector2d> getTriangleCenterProvider() {
        return this.triangleCenterProvider;
    }

    /**
     * Sets the triangle center provider.
     *
     * @param provider The triangle center provider
     */
    public VoronoiPolygonGenerator setTriangleCenterProvider(
            Function<Triangle2D, Vector2d> provider) {
        return setTriangleCenterProvider(provider, false);
    }

    /**
     * Sets the triangle center provider.
     *
     * @param provider The triangle center provider
     * @param convexPolygons Whether convex polygons will always be
     *                       generated when using the provided center provider
     */
    public VoronoiPolygonGenerator setTriangleCenterProvider(
            Function<Triangle2D, Vector2d> provider, boolean convexPolygons) {
        checkNotNull(provider, "provider");
        this.triangleCenterProvider = provider;
        this.isConvex = convexPolygons;
        return this;
    }

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

            // Go through all the vertices, to find all the touching triangles,
            // for this triangles is each circumcenter a point of the polygon shape
            for (Vector2D vertex : delaunayTriangulator.getPointSet()) {
                final List<VertexEntry> polygonVertices = new ArrayList<>();
                for (Triangle2D triangle : triangles) {
                    if (triangle.hasVertex(vertex)) {
                        final Vector2d point = this.triangleCenterProvider.apply(triangle);
                        final double angle = Math.atan2(point.getX() - vertex.x, point.getY() - vertex.y);
                        polygonVertices.add(new VertexEntry(point, angle));
                    }
                }
                if (polygonVertices.size() <= 2) { // Not enough vertices
                    continue;
                }
                // Create a polygon from vertices that are sorted clockwise+
                final List<Vector2d> orderedVertices = polygonVertices.stream()
                        .sorted().map(e -> e.point).collect(Collectors.toList());
                final Polygond polygon;
                if (this.isConvex) {
                    polygon = Polygond.newConvexPolygon(orderedVertices);
                } else {
                    polygon = new Polygond(orderedVertices);
                }
                centeredPolygons.add(new CenteredPolygon(polygon.getCentroid(), polygon));
            }
        } catch (NotEnoughPointsException e) {
            throw new RuntimeException(e);
        }

        return centeredPolygons;
    }

    private static final class VertexEntry implements Comparable<VertexEntry> {

        private final Vector2d point;
        private final double angle;

        private VertexEntry(Vector2d point, double angle) {
            this.point = point;
            this.angle = angle;
        }

        @Override
        public int compareTo(VertexEntry o) {
            if (this.angle > o.angle) {
                return 1;
            }
            return -1;
        }
    }
}
