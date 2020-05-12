/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("NOTHING_TO_INLINE")

package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.map.Cell
import org.lanternpowered.porygen.util.BitHelper
import org.lanternpowered.porygen.util.VariableValueArray
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector2i

interface CellBlockData {

  fun getCell(localX: Int, localY: Int): Cell

  val cells: Collection<Cell>
}

class SingleCellBlockData(
    private val cell: Cell
) : CellBlockData {

  override val cells: Collection<Cell> = listOf(this.cell)
  override fun getCell(localX: Int, localY: Int) = this.cell
}

class ArrayBackedCellBlockData(
    private val cellArray: Array<Cell>,
    private val blocks: VariableValueArray,
    private val sizeY: Int
) : CellBlockData {

  override val cells: Collection<Cell> = this.cellArray.asList()
  override fun getCell(localX: Int, localY: Int) = this.cellArray[this.blocks[localX * sizeY + localY]]
}

/**
 * Generates a [CellBlockData] for the section coordinates with the
 * cells. There must be at least one [Cell] in the collection.
 *
 * Calling this method requires that the cells in the chunk area are already generated.
 */
internal fun generateCellBlockData(position: Vector2i, size: Vector2i, cells: Array<Cell>): CellBlockData {
  check(cells.isNotEmpty()) { "Cell list cannot be empty" }
  if (cells.size == 1)
    return SingleCellBlockData(cells[0])

  val blockData = VariableValueArray(BitHelper.requiredBits(cells.size - 1), size.x * size.y)
  val last = cells.size - 1

  for (localX in 0 until size.x) {
    val x = position.x * size.x + localX + 0.5
    yLoop@ for (localY in 0 until size.y) {
      val y = position.y * size.y + localY + 0.5
      val index = localX * size.y + localY

      // Loop through all cells except the last one to
      // reduce the amount of contains checks.
      for (i in 0 until last) {
        if (cells[i].polygon.contains(Vector2d(x, y))) {
          blockData[index] = i
          continue@yLoop
        }
      }

      blockData[index] = last
    }
  }

  return ArrayBackedCellBlockData(cells, blockData, size.y)
}
