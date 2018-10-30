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
package org.lanternpowered.porygen.map.impl

import com.flowpowered.math.GenericMath
import com.flowpowered.math.vector.Vector2d
import com.github.benmanes.caffeine.cache.CacheLoader
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import com.google.common.base.Preconditions.checkNotNull
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.gen.CenteredPolygonGenerator
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.dsi.XoRoShiRo128PlusRandom
import org.lanternpowered.porygen.util.geom.Line2d
import org.lanternpowered.porygen.util.geom.Rectangled
import org.lanternpowered.porygen.util.geom.Rectanglei
import org.lanternpowered.porygen.util.uncheckedCast
import java.util.*

class WorldMap(
        private val polygonGenerator: CenteredPolygonGenerator,
        private val pointsGenerator: PointsGenerator,
        private val context: GeneratorContext
) : CellMap {

    // All the cells mapped by their center coordinates
    private val cellsByCenter = HashMap<Vector2d, PorygenCell>()

    // All the cells mapped by their center coordinates
    private val edgesByLine = HashMap<Line2d, PorygenEdge>()

    // All the cells mapped by chunk coordinates
    private val cellsByChunk = Long2ObjectOpenHashMap<MutableSet<PorygenCell>>()

    // A cache with all the map views that are currently allocated
    private val mapViewCache = (Caffeine.newBuilder().uncheckedCast<Caffeine<Rectangled, PorygenMapView>>())
            .weakValues()
            .removalListener(RemovalListener removalListener@ { key, value, _ ->
                // Cleanup the cells when the MapView is being cleaned up
                if (value == null) {
                    return@removalListener
                }
                val cells = value.cells
                for (cell in cells) {
                    cell as PorygenCell
                    // The view reference got removed
                    cell.referencedViews.remove(key)
                    // When all references to the cell get removed, destroy the cell
                    if (cell.referencedViews.isEmpty()) {
                        this.cellsByCenter.remove(cell.centerPoint)
                        cell.chunks.forEach { coords ->
                            val list = this.cellsByChunk.get(coords)
                            if (list != null && list.remove(cell)) {
                                this.cellsByChunk.remove(coords)
                            }
                        }
                    }
                }
            })
            .build(CacheLoader { key ->
                PorygenMapView() // TODO
            })

    private fun constructMapView(viewRectangle: Rectangled): PorygenMapView? {
        val random = XoRoShiRo128PlusRandom(this.context.seed)
        // Generate all the points for this map view
        val points = this.pointsGenerator.generatePoints(this.context, random, viewRectangle)
        // Generate CenteredPolygons from the given points
        val centeredPolygons = this.polygonGenerator.generate(this.context, viewRectangle, points)
        // Construct or lookup Cells for the centered polygons
        for (centeredPolygon in centeredPolygons) {
            val center = centeredPolygon.center
            var porygenCell: PorygenCell? = this.cellsByCenter[center]
            // Construct a new cell if necessary
            if (porygenCell == null) {
                porygenCell = PorygenCell(centeredPolygon)
                this.cellsByCenter[center] = porygenCell

                // Loop through all the edges and construct them if necessary
                val polygon = centeredPolygon.polygon
                val vertices = polygon.vertices
                var i: Int
                var j: Int
                i = 0
                j = vertices.size - 1
                while (i < vertices.size) {
                    val vi = vertices[i]
                    val vj = vertices[j]
                    // Construct the line which represents the edge
                    val line = Line2d(vi, vj)
                    // Only construct a new edge if there isn't already a cell using it
                    val edge = this.edgesByLine.computeIfAbsent(line) { PorygenEdge(it) }
                    edge.theCells.add(porygenCell) // Add the cell we constructed
                    j = i++
                }
            }
        }
        return null
    }

    override fun getSubView(rectangle: Rectangled): CellMapView {
        checkNotNull(rectangle, "rectangle")
        return this.mapViewCache.get(rectangle)!!
    }

    override fun getSubView(rectangle: Rectanglei): CellMapView {
        return getSubView(rectangle.toDouble())
    }

    override fun getCell(x: Double, y: Double): Cell {
        val fx = GenericMath.floor(x)
        val fy = GenericMath.floor(y)
        TODO()
    }

    /**
     * Gets the [Cell] using it's center point.
     *
     * @param point The center point
     * @return The cell, if found
     */
    fun getCellByCenter(point: Vector2d): Cell? {
        return this.cellsByCenter[point]
    }
}
