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
@file:Suppress("NOTHING_TO_INLINE")

package org.lanternpowered.porygen.map

import org.lanternpowered.porygen.api.map.Cell
import org.lanternpowered.porygen.api.util.NibbleArray
import java.util.*

interface CellBlockData {

    fun getCell(localX: Int, localZ: Int): Cell

    val cells: Collection<Cell>
}

object NullCellBlockData : CellBlockData {

    override val cells: Collection<Cell> get() = throw IllegalStateException()
    override fun getCell(localX: Int, localZ: Int): Cell = throw IllegalStateException()
}

class SingleCellBlockData(
        private val cell: Cell
) : CellBlockData {

    override val cells: Collection<Cell> = Collections.singleton(this.cell)
    override fun getCell(localX: Int, localZ: Int) = this.cell
}

class NibbleBackedCellBlockData(
        private val cellArray: Array<Cell>,
        private val blocks: NibbleArray
) : CellBlockData {

    override val cells: Collection<Cell> = Collections.unmodifiableList(this.cellArray.asList())
    override fun getCell(localX: Int, localZ: Int) = this.cellArray[this.blocks[index(localZ, localX)].toInt()]

    companion object {

        inline fun index(localX: Int, localZ: Int) = (localZ shl 4) or localX
    }
}

/**
 * Generates a [CellBlockData] for the chunk coordinates with the
 * cells. There must be at least one [Cell] in the collection.
 *
 * Calling this method requires that the cells in the chunk area are already generated.
 */
internal fun generateCellBlockData(chunkX: Int, chunkZ: Int, cells: Array<Cell>): CellBlockData {
    check(cells.isNotEmpty()) { "Cell list cannot be empty" }
    check(cells.size <= 16) { "Exceeded max different cells per chunk ${cells.size} is > 16"}

    if (cells.size == 1) {
        return SingleCellBlockData(cells[0])
    }

    val blockData = NibbleArray(16 * 16)
    val last = cells.size - 1

    for (localX in 0 until 16) {
        val x = chunkX * 16 + localX + 0.5

        zLoop@ for (localZ in 0 until 16) {
            val z = chunkZ * 16 + localZ + 0.5
            val index = NibbleBackedCellBlockData.index(localX, localZ)

            // Loop through all cells except the last one to
            // reduce the amount of contains checks.
            for (i in 0 until last) {
                if (cells[i].polygon.contains(x, z)) {
                    blockData[index] = i.toByte()
                    continue@zLoop
                }
            }

            blockData[index] = last.toByte()
        }
    }

    return NibbleBackedCellBlockData(cells, blockData)
}
