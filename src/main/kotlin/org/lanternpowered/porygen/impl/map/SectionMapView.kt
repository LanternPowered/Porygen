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

import org.lanternpowered.porygen.math.geom.Rectanglei
import org.spongepowered.math.vector.Vector2i

/**
 * Represents a "section" of a map. A section is a bigger area
 * that contains multiple chunks. The map is also generated per
 * section.
 *
 * So if you request a single chunk, the whole section will be
 * loaded into memory.
 */
class SectionMapView(
    val sectionPosition: Vector2i,
    override val map: MapImpl,
    override val viewRectangle: Rectanglei,
    override val cells: Collection<CellImpl>,
    override val corners: Collection<CornerImpl>,
    override val edges: Collection<EdgeImpl>
) : MapViewImpl(map, viewRectangle, cells, corners, edges)
