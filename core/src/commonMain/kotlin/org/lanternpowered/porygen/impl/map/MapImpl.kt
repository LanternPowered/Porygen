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
import org.lanternpowered.porygen.math.vector.max
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.pair.packIntPair
import org.lanternpowered.porygen.util.pair.unpackIntPairFirst
import org.lanternpowered.porygen.util.pair.unpackIntPairSecond
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.math.vector.Vector2i
import org.lanternpowered.porygen.util.collections.Long2ObjectMap
import org.lanternpowered.porygen.util.collections.getOrPutUnboxed
import org.lanternpowered.porygen.util.collections.getUnboxed
import org.lanternpowered.porygen.util.collections.long2ObjectMapOf

class MapImpl(
    override val seed: Long,
    private val sectionSize: Vector2i,
    private val polygonGenerator: CellPolygonGenerator,
    private val pointsGenerator: PointsGenerator,
    private val processors: List<CellMapProcessor>
) : SimpleDataHolder(), CellMap {

  // All the cells mapped by their center coordinates
  private val cellsByCenter = HashMap<Vector2i, CellImpl>()

  // All the cells mapped by chunk coordinates
  private val cellsByChunk = long2ObjectMapOf<MutableSet<CellImpl>>()

  private val chunksById = long2ObjectMapOf<MapChunkImpl>()
  private val cornersById = long2ObjectMapOf<CornerImpl>()
  private val cellsById = long2ObjectMapOf<CellImpl>()
  private val edgesById = long2ObjectMapOf<EdgeImpl>()

  /**
   * The polygon generator of sections.
   */
  private val sectionPolygonGenerator = SectionPolygonGenerator(seed, sectionSize, pointsGenerator, polygonGenerator)

  /**
   * All the sections that are currently loaded.
   */
  private val sections = long2ObjectMapOf<MapSection>()

  fun getSection(position: SectionPosition): MapSectionReference {
    var section = sections.getUnboxed(position.packed)
    if (section != null)
      return MapSectionReference(section)

    val cornersById = long2ObjectMapOf<CornerImpl>()
    val cellsById = long2ObjectMapOf<CellImpl>()
    val edgesById = long2ObjectMapOf<EdgeImpl>()

    // Processing area sections
    val areaSections = long2ObjectMapOf<MapSection>()
    for (x in -1..1) {
      for (y in -1..1) {
        val added = position.offset(x, y)
        areaSections[added.packed] = generateSectionData(added, cornersById, cellsById, edgesById)
      }
    }

    // The section that was requested
    section = areaSections[position.packed]!!

    val sectionRectangleMin = Vector2i(position.x, position.y) * sectionSize
    val sectionRectangleMax = sectionRectangleMin + sectionSize

    val processorViews = mutableMapOf<Rectanglei, CellMapView>()

    println("Started processing: $position")

    // Find the biggest required area
    var offsetFactor = Vector2d.ZERO
    for (processor in processors)
      offsetFactor = max(offsetFactor, processor.areaOffset)

    for (i in processors.indices) {
      val processor = processors[i]
      println("Started process: " + (processor::class.simpleName ?: "unknown"))

      val offset = (sectionSize * offsetFactor).floorToInt()

      val processorRectangleMin = sectionRectangleMin - offset
      val processorRectangleMax = sectionRectangleMax + offset
      val processorRectangle = Rectanglei(processorRectangleMin, processorRectangleMax)

      val view = processorViews.getOrPutUnboxed(processorRectangle) {
        getSubView(processorRectangle) { areaSectionPosition ->
          val areaSection = areaSections[areaSectionPosition.packed]
              ?: throw IllegalStateException("Requested an area that's too big.")
          MapSectionReference(areaSection)
        }
      }

      processor.process(view)
    }

    for (view in processorViews.values)
      view.release()

    sections[section.position.packed] = section
    return MapSectionReference(section)
  }

  private fun generateSectionData(
      position: SectionPosition,
      cornersById: Long2ObjectMap<CornerImpl>,
      cellsById: Long2ObjectMap<CellImpl>,
      edgesById: Long2ObjectMap<EdgeImpl>
  ): MapSection {
    val viewRectangleMin = Vector2i(position.x, position.y) * sectionSize
    val viewRectangleMax = viewRectangleMin + sectionSize
    val viewRectangle = Rectanglei(viewRectangleMin, viewRectangleMax)

    val cellPolygons = sectionPolygonGenerator.generate(position)

    val cells = LinkedHashSet<CellImpl>()
    val edges = LinkedHashSet<EdgeImpl>()
    val corners = LinkedHashSet<CornerImpl>()

    for (cellPolygon in cellPolygons) {
      val center = cellPolygon.center.floorToInt()
      val cellId = packIntPair(center.x, center.y)
      val cell = cellsById.getOrPutUnboxed(cellId) {
        CellImpl(cellId, this, center, cellPolygon.polygon)
      }
      cells += cell

      fun getCorner(point: Vector2i): CornerImpl {
        val cornerId = packIntPair(point.x, point.y)
        return cornersById.getOrPutUnboxed(cornerId) { CornerImpl(cornerId, point, this) }
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
        val edge = edgesById.getOrPutUnboxed(edgeId) { EdgeImpl(edgeId, edgeLine, this) }
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
    val chunkPos = Vector2i(chunkX, chunkZ)
    val cells = this.cellsByChunk[id]!!
    val cellBlockData = generateCellBlockData(chunkPos, sectionSize, cells.toTypedArray())
    chunk = MapChunkImpl(this, chunkPos, sectionSize, id, cellBlockData)
    this.chunksById[id] = chunk
    return chunk
  }

  fun getChunkIfLoaded(packedChunkPos: Long): CellMapChunk? = this.chunksById[packedChunkPos]

  override fun getCell(x: Int, z: Int): Cell = getChunk(x shr 4, z shr 4).getCell(x and 0xf, z and 0xf)

  override fun getCorner(id: Long): CornerImpl? = this.cornersById.getUnboxed(id)
  override fun getCell(id: Long): CellImpl? = this.cellsById.getUnboxed(id)
  override fun getEdge(id: Long): EdgeImpl? = this.edgesById.getUnboxed(id)

  override fun contains(element: CellMapElement): Boolean = element.map == this

  /**
   * Gets the [Cell] using its center point.
   *
   * @param point The center point
   * @return The cell, if found
   */
  fun getCellByCenter(point: Vector2i) = getCell(packIntPair(point.x, point.y))
}
