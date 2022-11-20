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

import org.lanternpowered.porygen.data.SimpleDataHolder
import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.map.CellMap
import org.lanternpowered.porygen.map.CellMapChunk
import org.lanternpowered.porygen.map.CellMapElement
import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.map.processor.CellMapProcessor
import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.math.geom.Line2i
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.math.vector.Vec2i
import org.lanternpowered.porygen.math.vector.max
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.collections.Long2ObjectOpenHashMap
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.util.pair.unpackIntPairFirst
import org.lanternpowered.porygen.util.pair.unpackIntPairSecond

class MapImpl(
  override val seed: Long,
  private val sectionSize: Vec2i,
  private val polygonGenerator: CellPolygonGenerator,
  private val pointsGenerator: PointsGenerator,
  private val processors: List<CellMapProcessor>
) : SimpleDataHolder(), CellMap {

  // All the cells mapped by chunk coordinates
  private val cellsByChunk = Long2ObjectOpenHashMap<MutableSet<CellImpl>>()

  private val chunksById = Long2ObjectOpenHashMap<MapChunkImpl>()
  private val cornersById = Long2ObjectOpenHashMap<CornerImpl>()
  private val cellsById = Long2ObjectOpenHashMap<CellImpl>()
  private val edgesById = Long2ObjectOpenHashMap<EdgeImpl>()

  /**
   * The polygon generator of sections.
   */
  private val sectionPolygonGenerator = SectionPolygonGenerator(
    seed, sectionSize, pointsGenerator, polygonGenerator)

  /**
   * All the sections that are currently loaded.
   */
  private val sections = Long2ObjectOpenHashMap<MapSection>()

  fun getSection(position: SectionPosition): MapSectionReference {
    var section = sections[position.packed]
    if (section != null)
      return MapSectionReference(section)

    val cornersById = Long2ObjectOpenHashMap<CornerImpl>()
    val cellsById = Long2ObjectOpenHashMap<CellImpl>()
    val edgesById = Long2ObjectOpenHashMap<EdgeImpl>()

    // Processing area sections
    val areaSections = Long2ObjectOpenHashMap<MapSection>()
    for (x in -1..1) {
      for (y in -1..1) {
        val added = position.offset(x, y)
        areaSections[added.packed] = generateSectionData(added, cornersById, cellsById, edgesById)
      }
    }

    // The section that was requested
    section = areaSections[position.packed]!!

    val sectionRectangleMin = Vec2i(position.x, position.y) * sectionSize
    val sectionRectangleMax = sectionRectangleMin + sectionSize

    println("Started processing: $position")

    // Find the biggest required area
    var offsetFactor = Vec2d.Zero
    for (processor in processors)
      offsetFactor = max(offsetFactor, processor.areaOffset)

    val offset = (sectionSize * offsetFactor).floorToInt()

    val processorRectangleMin = sectionRectangleMin - offset
    val processorRectangleMax = sectionRectangleMax + offset
    val processorRectangle = Rectanglei(processorRectangleMin, processorRectangleMax)

    val view = getSubView(processorRectangle) { areaSectionPosition ->
      val areaSection = areaSections[areaSectionPosition.packed]
          ?: throw IllegalStateException("Requested an area that's too big.")
      MapSectionReference(areaSection)
    }

    for (processor in processors) {
      println("Started process: " + (processor::class.simpleName ?: "unknown"))
      processor.process(view)
    }

    view.release()

    sections[section.position.packed] = section
    return MapSectionReference(section)
  }

  private fun generateSectionData(
      position: SectionPosition,
      cornersById: Long2ObjectOpenHashMap<CornerImpl>,
      cellsById: Long2ObjectOpenHashMap<CellImpl>,
      edgesById: Long2ObjectOpenHashMap<EdgeImpl>
  ): MapSection {
    val viewRectangleMin = Vec2i(position.x, position.y) * sectionSize
    val viewRectangleMax = viewRectangleMin + sectionSize
    val viewRectangle = Rectanglei(viewRectangleMin, viewRectangleMax)

    val cellPolygons = sectionPolygonGenerator.generate(position)

    val cells = LinkedHashSet<CellImpl>()
    val edges = LinkedHashSet<EdgeImpl>()
    val corners = LinkedHashSet<CornerImpl>()

    for (cellPolygon in cellPolygons) {
      val center = cellPolygon.center.floorToInt()
      val cellId = packIntPair(center.x, center.y)
      val cell = cellsById.getOrPut(cellId) {
        CellImpl(cellId, this, center, cellPolygon.polygon)
      }
      cells += cell

      fun getCorner(point: Vec2i): CornerImpl {
        val cornerId = packIntPair(point.x, point.y)
        return cornersById.getOrPut(cornerId) { CornerImpl(cornerId, point, this) }
      }

      val vertices = cellPolygon.polygon.vertices
      var i = 0
      var j = vertices.lastIndex
      while (i < vertices.size) {
        val vi = vertices[i].floorToInt()
        val vj = vertices[j].floorToInt()

        val corneri = getCorner(vi)
        val cornerj = getCorner(vj)
        corners += corneri
        corners += cornerj

        cell.mutableCorners += corneri
        cell.mutableCorners += cornerj

        val edgeLine = Line2i(vi, vj)
        val edgeCenter = edgeLine.center
        val edgeId = packIntPair(edgeCenter.x, edgeCenter.y)
        val edge = edgesById.getOrPut(edgeId) { EdgeImpl(edgeId, edgeLine, this) }
        edges += edge

        cell.mutableEdges += edge

        edge.mutableCells += cell
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

    for (corner in corners) {
      corner.mutableCells.sort()
      corner.mutableEdges.sort()
      corner.mutableNeighbors.sort()

      for (cell in corner.cells)
        cell.mutableNeighbors.addAll(corner.cells.filter { it != cell })
    }

    for (edge in edges) {
      edge.mutableCells.sort()
      edge.mutableCorners.sort()
    }
    
    for (cell in cells) {
      cell.mutableCorners.sort()
      cell.mutableEdges.sort()
      cell.mutableNeighbors.sort()
    }

    cells.sort()
    corners.sort()
    edges.sort()

    return MapSection(position, this, viewRectangle, cells, corners, edges)
  }

  override fun getSubView(rectangle: Rectanglei): CellMapView = getSubView(rectangle, ::getSection)

  private fun getSubView(rectangle: Rectanglei, getSection: (position: SectionPosition) -> MapSectionReference): CellMapView {
    // Get the sections that are required for this sub view
    val minSectionX = floorToInt(rectangle.min.x.toDouble() / sectionSize.x)
    val minSectionY = floorToInt(rectangle.min.y.toDouble() / sectionSize.y)
    val maxSectionX = floorToInt((rectangle.max.x - 1.toDouble()) / sectionSize.x)
    val maxSectionY = floorToInt((rectangle.max.y - 1.toDouble()) / sectionSize.y)

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
    val corners = sections
        .map { section ->
          if (isEdgeSection(section)) {
            section.corners.filter { corner -> rectangle.contains(corner.point) }
          } else {
            section.corners
          }
        }
        .flatten()
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
    val chunkPos = Vec2i(chunkX, chunkZ)
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
  fun getCellByCenter(point: Vec2i) = getCell(packIntPair(point.x, point.y))
}
