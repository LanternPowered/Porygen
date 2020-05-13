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

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapChunk
import org.lanternpowered.porygen.map.CellMapElement
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.math.geom.Line2i
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.util.pair.unpackIntPairFirst
import org.lanternpowered.porygen.util.pair.unpackIntPairSecond
import org.spongepowered.math.vector.Vector2i

class MapImpl(
    override val seed: Long,
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
   * The polygon generator of sections.
   */
  private val sectionPolygonGenerator = SectionPolygonGenerator(seed, sectionSize, pointsGenerator, polygonGenerator)

  /**
   * All the sections that are currently loaded.
   */
  private val sections = Long2ObjectOpenHashMap<MapSection>()

  fun getSection(position: SectionPosition): MapSectionReference {
    var section = sections.get(position.packed)
    if (section != null)
      return MapSectionReference(section)

    section = generateSectionData(position)
    // TODO: Run processors

    sections[section.position.packed] = section
    return MapSectionReference(section)
  }

  private fun generateSectionData(position: SectionPosition): MapSection {
    val viewRectangleMin = Vector2i(position.x, position.y).mul(sectionSize)
    val viewRectangleMax = viewRectangleMin.add(sectionSize)
    val viewRectangle = Rectanglei(viewRectangleMin, viewRectangleMax)

    val cellPolygons = sectionPolygonGenerator.generate(position)

    val cells = mutableSetOf<CellImpl>()
    val edges = mutableSetOf<EdgeImpl>()
    val corners = mutableSetOf<CornerImpl>()

    for (cellPolygon in cellPolygons) {
      val center = cellPolygon.center.toInt()
      val cellId = packIntPair(center.x, center.y)
      val cell = cellsById.computeIfAbsent(cellId) {
        CellImpl(cellId, this, center, cellPolygon.polygon)
      }
      cells += cell

      fun getCorner(point: Vector2i): CornerImpl {
        val cornerId = packIntPair(point.x, point.y)
        return cornersById.computeIfAbsent(cornerId) { CornerImpl(cornerId, point, this) }
      }

      val vertices = cellPolygon.polygon.vertices
      var i = 0
      var j = vertices.size - 1
      while (i < vertices.size) {
        val vi = vertices[i].toInt()
        val vj = vertices[j].toInt()

        val corneri = getCorner(vi)
        val cornerj = getCorner(vj)
        corners += corneri
        corners += cornerj

        cell.mutableCorners += corneri
        cell.mutableCorners += cornerj

        val edgeLine = Line2i(vi, vj)
        val edgeCenter = edgeLine.center
        val edgeId = packIntPair(edgeCenter.x, edgeCenter.y)
        val edge = edgesById.computeIfAbsent(edgeId) { EdgeImpl(edgeLine.toInt(), this) }
        edges += edge

        edge.mutableCorners += corneri
        edge.mutableCorners += cornerj

        corneri.mutableEdges += edge
        cornerj.mutableEdges += edge

        corneri.mutableCells += cell
        cornerj.mutableCells += cell

        corneri.mutableNeighbors += cornerj
        cornerj.mutableNeighbors += corneri

        j = i++
      }
    }

    return MapSection(position, this, viewRectangle, cells, corners, edges)
  }

  override fun getSubView(rectangle: Rectanglei): CellMapView {
    // Get the sections that are required for this sub view
    val minSectionX = floorToInt(rectangle.min.x.toDouble() / sectionSize.x)
    val minSectionY = floorToInt(rectangle.min.y.toDouble() / sectionSize.y)
    val maxSectionX = floorToInt(rectangle.max.x.toDouble() / sectionSize.x)
    val maxSectionY = floorToInt(rectangle.max.y.toDouble() / sectionSize.y)

    val sectionReferences = mutableSetOf<MapSectionReference>()
    for (sectionX in minSectionX..maxSectionX) {
      for (sectionY in minSectionY..maxSectionY) {
        sectionReferences += getSection(SectionPosition(sectionX, sectionY))
      }
    }

    fun isEdgeSection(section: MapSection): Boolean {
      val position = section.position
      return position.x == minSectionX || position.y == minSectionY ||
          position.x == maxSectionX || position.y == maxSectionY
    }

    val sections = sectionReferences.asSequence()
        .map { reference -> reference.get() }
    val edges = sections
        .map { section ->
          if (isEdgeSection(section)) {
            section.edges.filter { edge ->
              val line = edge.line
              rectangle.contains(line.start) || rectangle.contains(line.end)
            }
          } else {
            section.edges
          }
        }
        .flatten()
        .toSet()
    val corners = sections
        .map { section ->
          if (isEdgeSection(section)) {
            section.corners.filter { corner -> rectangle.contains(corner.point) }
          } else {
            section.corners
          }
        }
        .flatten()
        .toSet()
    val cells = sections
        .map { section ->
          if (isEdgeSection(section)) {
            section.cells.filter { cell ->
              cell.polygon.vertices.any { vertex -> rectangle.contains(vertex) }
            }
          } else {
            section.cells
          }
        }
        .flatten()
        .toSet()

    return MapViewImpl(sectionReferences, rectangle, cells, corners, edges)
  }

  override fun getChunk(chunkX: Int, chunkY: Int) = getChunk(packIntPair(chunkX, chunkY))

  fun getChunk(id: Long): CellMapChunk {
    var chunk = this.chunksById[id]
    if (chunk != null) {
      return chunk
    }
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
}
