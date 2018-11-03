package org.lanternpowered.porygen.api.map

interface CellMapElement {

    /**
     * The id of this identifiable. This id is unique
     * within a specific [CellMap].
     */
    val id: Long

    /**
     * The [CellMap] this element is located in.
     */
    val map: CellMap
}
