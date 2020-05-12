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

import org.lanternpowered.porygen.map.polygon.CellPolygon
import org.lanternpowered.porygen.map.polygon.CellPolygonGenerator
import org.lanternpowered.porygen.math.ceilToInt
import org.lanternpowered.porygen.points.PointsGenerator
import org.lanternpowered.porygen.util.random.Xor128Random
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i

/**
 * This generator generates polygons for a specified world, the polygons are generated
 * based of the section coordinates and the world seed resulting in always the same
 * results when the points and polygon generators are re-run.
 *
 * @property seed The seed of the generator
 * @property sectionSize The size of a single section, should
 *   be a multiple of the chunk size
 * @property pointsGenerator The points generator
 * @property polygonGenerator The polygon generator
 */
class SectionPolygonGenerator(
    val seed: Long,
    val sectionSize: Vector2i,
    val pointsGenerator: PointsGenerator,
    val polygonGenerator: CellPolygonGenerator
) {

  fun generate(section: SectionPosition): Collection<CellPolygon> {
    // Some polygon generators need points that are outside
    // their original (0,1) area, so we need to include the
    // points of nearby sections
    // The offset is the amount of sections -x, -y, +x, +y
    // relative to the needed section
    val offset = polygonGenerator.pointsOffset

    val offsetSectionX = ceilToInt(offset.x)
    val offsetSectionY = ceilToInt(offset.y)

    // Local positions are the positions relative to (0,0), (0,0)
    // is represented by the needed section

    val localMinSectionX = -offsetSectionX
    val localMinSectionY = -offsetSectionY
    val localMaxSectionX = offsetSectionX + 1
    val localMaxSectionY = offsetSectionY + 1

    val localMinX = -offset.x
    val localMinY = -offset.y
    val localMaxX = offset.x + 1
    val localMaxY = offset.y + 1

    val sectionSize = sectionSize.toDouble()
    val points = mutableListOf<Vector2d>()

    for (localSectionX in localMinSectionX..localMaxSectionX) {
      for (localSectionY in localMinSectionY..localMaxSectionY) {
        val localSectionOffset = Vector2d(localSectionX.toDouble(), localSectionY.toDouble())

        val globalSectionX = localSectionX + section.x
        val globalSectionY = localSectionY + section.y

        val sectionRandom = Xor128Random(
            (globalSectionX.toLong() * 341873128712L + globalSectionY.toLong() * 132897987541L) xor seed)

        var localPoints = pointsGenerator.generate(sectionRandom).asSequence()
            .map { point -> point.add(localSectionOffset) }
        if (localSectionX == localMinSectionX || localSectionY == localMinSectionY ||
            localSectionX == localMaxSectionX || localSectionY == localMaxSectionY) {
          localPoints = localPoints.filter { point -> point.x in localMinX..localMaxX && point.y in localMinY..localMaxY }
        }
        points.addAll(localPoints)
      }
    }

    val translation = Vector2d(section.x.toDouble() * sectionSize.x, section.y.toDouble() * sectionSize.y)
    return polygonGenerator.generate(points).asSequence()
        // Only keep polygons that has at least one vertex that's inside the section area
        .filter { polygon -> polygon.polygon.vertices.any { point -> point.x in 0.0..1.0 && point.y in 0.0..1.0 } }
        // The points normally in a range of (0,1) if there's no offset, and otherwise a
        // bit below 0 and above 1
        // The polygons need to be translated to their section position and scaled up from
        // the (0, 1) range to the section size
        .map { polygon -> polygon.scale(sectionSize).translate(translation) }
        .toList()
  }
}
