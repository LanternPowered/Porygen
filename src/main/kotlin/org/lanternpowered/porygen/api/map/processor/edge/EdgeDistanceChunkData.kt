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
package org.lanternpowered.porygen.api.map.processor.edge

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.lanternpowered.porygen.api.map.Edge

class EdgeDistanceChunkData {

    internal val distanceData: Long2ObjectMap<ByteArray> = Long2ObjectOpenHashMap()

    /**
     * Attempts to get the distance to the given [Edge] or -1 if too far away.
     *
     * @param edge The edge to get the distance to
     * @param localX The local x coordinate
     * @param localZ The local z coordinate
     */
    fun getDistanceToEdge(edge: Edge, localX: Int, localZ: Int): Int {
        val distanceData = this.distanceData[edge.id] ?: return -1
        return distanceData[localZ * 16 + localX].toInt()
    }

    internal fun copy(): EdgeDistanceChunkData {
        TODO()
    }

    companion object {

        internal const val NoDistance: Byte = -1
    }
}
