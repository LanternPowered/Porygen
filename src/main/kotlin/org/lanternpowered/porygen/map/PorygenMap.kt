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
package org.lanternpowered.porygen.map

import com.flowpowered.math.vector.Vector2i
import com.github.benmanes.caffeine.cache.CacheLoader
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.data.SimpleDataHolder
import org.lanternpowered.porygen.api.map.Cell
import org.lanternpowered.porygen.api.map.CellMap
import org.lanternpowered.porygen.api.map.CellMapChunk
import org.lanternpowered.porygen.api.map.CellMapView
import org.lanternpowered.porygen.api.map.gen.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.api.points.PointsGenerator
import org.lanternpowered.porygen.api.util.dsi.XoRoShiRo128PlusRandom
import org.lanternpowered.porygen.api.util.geom.Rectangled
import org.lanternpowered.porygen.api.util.geom.Rectanglei
import org.lanternpowered.porygen.api.util.tuple.packIntPair
import org.lanternpowered.porygen.api.util.tuple.unpackIntPairFirst
import org.lanternpowered.porygen.api.util.tuple.unpackIntPairSecond
import org.lanternpowered.porygen.api.util.uncheckedCast
import org.spongepowered.api.world.World
import org.spongepowered.api.world.storage.WorldProperties
import java.awt.Graphics
import java.util.*

class PorygenMap(
        private val worldProperties: WorldProperties,
        private val polygonGenerator: CellPolygonGenerator,
        private val pointsGenerator: PointsGenerator,
        override val world: World
) : SimpleDataHolder(), CellMap {

    // All the cells mapped by their center coordinates
    private val cellsByCenter = HashMap<Vector2i, PorygenCell>()

    // All the cells mapped by chunk coordinates
    private val cellsByChunk = Long2ObjectOpenHashMap<MutableSet<PorygenCell>>()

    private val chunksById = Long2ObjectOpenHashMap<PorygenMapChunk>()
    private val cornersById = Long2ObjectOpenHashMap<PorygenCorner>()
    private val cellsById = Long2ObjectOpenHashMap<PorygenCell>()
    private val edgesById = Long2ObjectOpenHashMap<PorygenEdge>()

    /**
     * The coordinates of all the chunks that are loaded in the [World].
     */
    // private val loadedChunks = Long2IntOpenHashMap()

    // All the map chunks that should be in memory
    private val loadedMapChunks = Long2IntOpenHashMap()

    private val loadQueuedMapChunks = LongOpenHashSet()
    private val unloadQueuedMapChunks = LongOpenHashSet()

    // A cache with all the map views that are currently allocated
    private val mapViewCache = (Caffeine.newBuilder().uncheckedCast<Caffeine<Rectanglei, PorygenMapView>>())
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
                //PorygenMapView() // TODO
                TODO()
            })

    private class Ctx constructor(override val debugGraphics: Graphics?, override val seed: Long, override val random: Random) : GeneratorContext

    private fun constructMapView(viewRectangle: Rectangled): PorygenMapView? {
        val seed = this.worldProperties.seed
        val random = XoRoShiRo128PlusRandom(seed)
        val context = Ctx(null, seed, random)
        // Generate CenteredPolygons from the given points
        val centeredPolygons = this.polygonGenerator.generate(context, viewRectangle)
        // Construct or lookup Cells for the centered polygons
        for (centeredPolygon in centeredPolygons) {
            val center = centeredPolygon.center.toInt()
            var porygenCell: PorygenCell? = this.cellsByCenter[center]
            // Construct a new cell if necessary
            if (porygenCell == null) {
                val cellData = buildCellData(centeredPolygon)
                porygenCell = PorygenCell(this, cellData)
                this.cellsByCenter[center] = porygenCell

                // Loop through all the edges and construct them if necessary
                val polygon = centeredPolygon.polygon
                val vertices = polygon.vertices
                var i = 0
                var j = vertices.size - 1
                while (i < vertices.size) {
                    val vi = vertices[i].toInt()
                    val vj = vertices[j].toInt()
                    // Construct the line which represents the edge
                    //val line = Line2i(vi, vj)
                    // Only construct a new edge if there isn't already a cell using it
                    /*
                    val edge = this.edgesByLine.computeIfAbsent(line) { PorygenEdge(it) }*/
                    /*edge.theCells.add(porygenCell) // Add the cell we constructed
                    j = i++*/
                }
            }
        }
        return null
    }

    fun buildMapView() {

    }

    override fun getSubView(rectangle: Rectanglei): CellMapView {
        return this.mapViewCache.get(rectangle)!!
    }

    // Keep references to everything that should cause the map pieces
    // to be loaded or kept in memory.

    private fun addChunkRef(id: Long) {
        // Increase the references to the chunk and the surrounding chunks
        val ref = this.loadedMapChunks[id]
        this.loadedMapChunks[id] = ref + 1
        // Check if the chunk is newly added,
        if (ref == 0) {
            // Queue the chunk to be loaded
            this.loadQueuedMapChunks.add(id)
            // Remove a old unload entry
            this.unloadQueuedMapChunks.remove(id)
        }
    }

    private fun removeChunkRef(id: Long) {
        // Increase the references to the chunk and the surrounding chunks
        val ref = this.loadedMapChunks[id]
        this.loadedMapChunks[id] = ref - 1
        // Check if the chunk is newly added,
        if (ref == 1) {
            // Queue the chunk to be unloaded
            this.loadQueuedMapChunks.remove(id)
            // Remove a load entry
            this.unloadQueuedMapChunks.add(id)
        }
    }

    internal fun onLoadChunk(chunkX: Int, chunkZ: Int) {
        // Increase the references to the chunk and the surrounding chunks
        for (x in chunkX - SURROUNDING_LOADED_CHUNKS .. chunkX + SURROUNDING_LOADED_CHUNKS) {
            for (z in chunkZ - SURROUNDING_LOADED_CHUNKS .. chunkZ + SURROUNDING_LOADED_CHUNKS) {
                addChunkRef(packIntPair(chunkX, chunkZ))
            }
        }
    }

    internal fun onUnloadChunk(chunkX: Int, chunkZ: Int) {
        // Decrease the references to the chunk and the surrounding chunks
        for (x in chunkX - SURROUNDING_LOADED_CHUNKS .. chunkX + SURROUNDING_LOADED_CHUNKS) {
            for (z in chunkZ - SURROUNDING_LOADED_CHUNKS .. chunkZ + SURROUNDING_LOADED_CHUNKS) {
                removeChunkRef(packIntPair(chunkX, chunkZ))
            }
        }
    }

    override fun getChunk(chunkX: Int, chunkZ: Int) = getChunk(packIntPair(chunkX, chunkZ))

    fun getChunk(id: Long): CellMapChunk {
        var chunk = this.chunksById[id]
        if (chunk != null) {
            return chunk
        }
        this.loadQueuedMapChunks.remove(id)
        this.unloadQueuedMapChunks.remove(id)
        val chunkX = unpackIntPairFirst(id)
        val chunkZ = unpackIntPairSecond(id)
        val chunkPos = Vector2i(chunkX, chunkZ)
        chunk = PorygenMapChunk(this, chunkPos, id)
        val cells = this.cellsByChunk[id]!!
        chunk.cellBlockData = generateCellBlockData(chunkX, chunkZ, cells.toTypedArray())
        this.chunksById[id] = chunk
        return chunk
    }

    fun getChunkIfLoaded(packedChunkPos: Long): CellMapChunk? = this.chunksById[packedChunkPos]

    override fun getCell(x: Int, z: Int): Cell = getChunk(x shr 4, z shr 4).getCell(x and 0xf, z and 0xf)

    override fun getCorner(id: Long): PorygenCorner? = this.cornersById[id]
    override fun getCell(id: Long): PorygenCell? = this.cellsById[id]
    override fun getEdge(id: Long): PorygenEdge? = this.edgesById[id]

    /**
     * Gets the [Cell] using its center point.
     *
     * @param point The center point
     * @return The cell, if found
     */
    fun getCellByCenter(point: Vector2i) = getCell(packIntPair(point.x, point.y))

    companion object {

        const val SURROUNDING_LOADED_CHUNKS = 5
    }
}
