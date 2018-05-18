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
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.CellGenerator;
import org.lanternpowered.porygen.util.Rectangled;
import org.lanternpowered.porygen.util.TriangleHelper;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoronoiCellGenerator implements CellGenerator {

    @Override
    public List<Cell> generate(World world, Rectangled rectangle, List<Vector2d> points) {
        final List<Cell> cells = new ArrayList<>();
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
                final List<Vector2d> polygonVertices = new ArrayList<>();
                for (Triangle2D triangle : triangles) {
                    if (triangle.hasVertex(vertex)) {
                        polygonVertices.add(TriangleHelper.getCircumcenter(triangle));
                    }
                }
                final Cell cell = new SimpleCell(new Vector2d(vertex.x, vertex.y), polygonVertices);
                cells.add(cell);
            }
        } catch (NotEnoughPointsException e) {
            throw new RuntimeException(e);
        }

        return cells;
    }
}
