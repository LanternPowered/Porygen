package org.lanternpowered.porygen.api.map

import org.lanternpowered.porygen.api.map.gen.polygon.CellPolygonGenerator

interface CellMapGenerator {

    /**
     * The generator that is used to generate the cell map polygons.
     */
    var polygonGenerator: CellPolygonGenerator
}
