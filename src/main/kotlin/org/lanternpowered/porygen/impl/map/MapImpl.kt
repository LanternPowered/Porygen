/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import org.lanternpowered.porygen.GeneratorContext
import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapChunk
import org.lanternpowered.porygen.map.CellMapElement
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.util.pair.unpackIntPairFirst
import org.lanternpowered.porygen.util.pair.unpackIntPairSecond
import org.spongepowered.math.vector.Vector2i
import java.awt.Graphics
import java.util.HashMap

class MapImpl(
    private val seed: Long,
    private val sectionSize: Vector2i,
    private val polygonGenerator: CellPolygonGenerator,
    private val pointsGenerator: PointsGenerator
) : SimpleDataHolder(), CellMap {

  // All the cells mapped by their center coordinates
  private val cellsByCenter = HashMap<Vector2i, CellImpl>()

  // All the cells mapped by chunk coordinates
  private val cellsByChunk = Long2ObjectOpenHashMap<MutableSet<CellImpl>>()

  private val chunksById = Long2ObjectOpenHashMap<MapChunkImpl>()
  private val cornersById = Long2ObjectOpenHashMap<CornerImpl>()
  private val cellsById = Long2ObjectOpenHashMap<CellImpl>()
  private val edgesById = Long2ObjectOpenHashMap<EdgeImpl>()

  /**
   * The coordinates of all the chunks that are loaded in the [World].
   */
  // private val loadedChunks = Long2IntOpenHashMap()

  // All the map chunks that should be in memory
  private val loadedMapChunks = Long2IntOpenHashMap()

  private val loadQueuedMapChunks = LongOpenHashSet()
  private val unloadQueuedMapChunks = LongOpenHashSet()

  //private val sectionCache = Caffeine.

  fun getSection(position: SectionPosition): MapSection {

    TODO()
  }

  /*
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
*/
  private class Ctx constructor(override val debugGraphics: Graphics?, override val seed: Long) : GeneratorContext

  private fun constructMapView(viewRectangle: Rectangled): MapViewImpl? {
    val context = Ctx(null, this.seed)
    // Generate CenteredPolygons from the given points
    val centeredPolygons: List<CellPolygon> = emptyList() // TODO: this.polygonGenerator.generate()
    // Construct or lookup Cells for the centered polygons
    for (centeredPolygon in centeredPolygons) {
      val center = centeredPolygon.center.toInt()
      var porygenCell: CellImpl? = this.cellsByCenter[center]
      // Construct a new cell if necessary
      if (porygenCell == null) {
        val cellData = buildCellData(centeredPolygon)
        porygenCell = CellImpl(this, cellData)
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
    return TODO()//this.mapViewCache.get(rectangle)!!
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
    for (x in chunkX - SURROUNDING_LOADED_CHUNKS..chunkX + SURROUNDING_LOADED_CHUNKS) {
      for (z in chunkZ - SURROUNDING_LOADED_CHUNKS..chunkZ + SURROUNDING_LOADED_CHUNKS) {
        addChunkRef(packIntPair(chunkX, chunkZ))
      }
    }
  }

  internal fun onUnloadChunk(chunkX: Int, chunkZ: Int) {
    // Decrease the references to the chunk and the surrounding chunks
    for (x in chunkX - SURROUNDING_LOADED_CHUNKS..chunkX + SURROUNDING_LOADED_CHUNKS) {
      for (z in chunkZ - SURROUNDING_LOADED_CHUNKS..chunkZ + SURROUNDING_LOADED_CHUNKS) {
        removeChunkRef(packIntPair(chunkX, chunkZ))
      }
    }
  }

  override fun getChunk(chunkX: Int, chunkY: Int) = getChunk(packIntPair(chunkX, chunkY))

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
    val cells = this.cellsByChunk[id]!!
    val cellBlockData = generateCellBlockData(chunkPos, sectionSize, cells.toTypedArray())
    chunk = MapChunkImpl(this, chunkPos, sectionSize, id, cellBlockData)
    this.chunksById[id] = chunk
    return chunk
  }

  fun getChunkIfLoaded(packedChunkPos: Long): CellMapChunk? = this.chunksById[packedChunkPos]

  override fun getCell(x: Int, z: Int): Cell = getChunk(x shr 4, z shr 4).getCell(x and 0xf, z and 0xf)

  override fun getCorner(id: Long): CornerImpl? = this.cornersById[id]
  override fun getCell(id: Long): CellImpl? = this.cellsById[id]
  override fun getEdge(id: Long): EdgeImpl? = this.edgesById[id]

  override fun contains(element: CellMapElement): Boolean = element.map == this

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
