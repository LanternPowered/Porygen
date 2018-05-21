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

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.lanternpowered.porygen.GeneratorContext;
import org.lanternpowered.porygen.map.Cell;
import org.lanternpowered.porygen.map.CellMap;
import org.lanternpowered.porygen.map.CellMapView;
import org.lanternpowered.porygen.map.gen.CenteredPolygon;
import org.lanternpowered.porygen.map.gen.CenteredPolygonGenerator;
import org.lanternpowered.porygen.points.PointsGenerator;
import org.lanternpowered.porygen.util.dsi.XoRoShiRo128PlusRandom;
import org.lanternpowered.porygen.util.geom.Line2d;
import org.lanternpowered.porygen.util.geom.Polygond;
import org.lanternpowered.porygen.util.geom.Rectangled;
import org.lanternpowered.porygen.util.geom.Rectanglei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

public class WorldMap implements CellMap {

    // All the cells mapped by their center coordinates
    private final Map<Vector2d, PorygenCell> cellsByCenter = new HashMap<>();

    // All the cells mapped by their center coordinates
    private final Map<Line2d, PorygenEdge> edgesByLine = new HashMap<>();

    // All the cells mapped by chunk coordinates
    private final Multimap<Vector2i, PorygenCell> cellsByChunk = HashMultimap.create();

    // A cache with all the map views that are currently allocated
    private final LoadingCache<Rectangled, PorygenMapView> mapViewCache = Caffeine.newBuilder()
            .weakValues()
            .removalListener((RemovalListener<Rectangled, PorygenMapView>) (key, value, cause) -> {
                // Cleanup the cells when the MapView is being cleaned up
                if (value == null) {
                    return;
                }
                final List<Cell> cells = value.getCells();
                for (Cell cell : cells) {
                    final PorygenCell c = (PorygenCell) cell;
                    // The view reference got removed
                    c.referencedViews.remove(key);
                    // When all references to the cell get removed, destroy the cell
                    if (c.referencedViews.isEmpty()) {
                        this.cellsByCenter.remove(c.getCenterPoint());
                        c.chunks.forEach(coords -> this.cellsByChunk.remove(coords, c));
                    }
                }
            })
            .build(key -> {
                return new PorygenMapView(); // TODO
            });

    private final CenteredPolygonGenerator polygonGenerator;
    private final PointsGenerator pointsGenerator;
    private final GeneratorContext context;

    public WorldMap(CenteredPolygonGenerator polygonGenerator, PointsGenerator pointsGenerator,
            GeneratorContext context) {
        this.polygonGenerator = polygonGenerator;
        this.pointsGenerator = pointsGenerator;
        this.context = context;
    }

    private PorygenMapView constructMapView(Rectangled viewRectangle) {
        final Random random = new XoRoShiRo128PlusRandom(this.context.getSeed());
        // Generate all the points for this map view
        final List<Vector2d> points = this.pointsGenerator.generatePoints(this.context, random, viewRectangle);
        // Generate CenteredPolygons from the given points
        final List<CenteredPolygon> centeredPolygons = this.polygonGenerator.generate(this.context, viewRectangle, points);
        // Construct or lookup Cells for the centered polygons
        for (CenteredPolygon centeredPolygon : centeredPolygons) {
            final Vector2d center = centeredPolygon.getCenter();
            PorygenCell porygenCell = this.cellsByCenter.get(center);
            // Construct a new cell if necessary
            if (porygenCell == null) {
                porygenCell = new PorygenCell(centeredPolygon);
                this.cellsByCenter.put(center, porygenCell);

                // Loop through all the edges and construct them if necessary
                final Polygond polygon = centeredPolygon.getPolygon();
                final List<Vector2d> vertices = polygon.getVertices();
                int i;
                int j;
                for (i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
                    final Vector2d vi = vertices.get(i);
                    final Vector2d vj = vertices.get(j);
                    // Construct the line which represents the edge
                    final Line2d line = new Line2d(vi, vj);
                    // Only construct a new edge if there isn't already a cell using it
                    final PorygenEdge edge = this.edgesByLine.computeIfAbsent(line, PorygenEdge::new);
                    edge.cells.add(porygenCell); // Add the cell we constructed
                }
            }
        }
        return null;
    }

    @Override
    public CellMap getParent() {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public CellMapView getSubView(Rectangled rectangle) {
        checkNotNull(rectangle, "rectangle");
        return this.mapViewCache.get(rectangle);
    }

    @Override
    public CellMapView getSubView(Rectanglei rectangle) {
        return getSubView(rectangle.toDouble());
    }

    @Override
    public Cell getCell(double x, double y) {
        final int fx = GenericMath.floor(x);
        final int fy = GenericMath.floor(y);



        return null;
    }

    /**
     * Gets the {@link Cell} using it's center point.
     *
     * @param point The center point
     * @return The cell, if found
     */
    @Nullable
    public Cell getCellByCenter(Vector2d point) {
        return this.cellsByCenter.get(point);
    }

    @Override
    public List<Cell> getCells() {
        return null;
    }
}
