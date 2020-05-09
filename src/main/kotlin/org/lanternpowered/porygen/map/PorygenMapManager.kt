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
package org.lanternpowered.porygen.map

import org.lanternpowered.porygen.api.map.CellMap
import org.lanternpowered.porygen.api.map.CellMapManager
import org.lanternpowered.porygen.generator.GeneratorModifierToType
import org.lanternpowered.porygen.generator.PorygenGeneratorType
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.world.GenerateChunkEvent
import org.spongepowered.api.event.world.LoadWorldEvent
import org.spongepowered.api.event.world.UnloadWorldEvent
import org.spongepowered.api.event.world.chunk.ForcedChunkEvent
import org.spongepowered.api.event.world.chunk.LoadChunkEvent
import org.spongepowered.api.event.world.chunk.UnforcedChunkEvent
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent
import org.spongepowered.api.world.World
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PorygenMapManager : CellMapManager {

    private val mapsByWorld = ConcurrentHashMap<UUID, PorygenMap?>()

    override fun getMap(world: World): CellMap? = this.mapsByWorld[world.uniqueId]

    @Listener(order = Order.LAST)
    fun onLoadWorld(event: LoadWorldEvent) {
        val world = event.targetWorld

        // If the world is at this phase using a PorygenGeneratorType modifier
        // then we can consider this world using the Porygen cell map.
        // Post injected generator modifiers are not supported.
        val isCellWorld = world.properties.generatorType is GeneratorModifierToType ||
                world.properties.generatorModifiers.find { it is PorygenGeneratorType } != null
        if (isCellWorld) {
            // Get the porygen generator settings from
            val porygenSettings = world.properties.generatorSettings[DataQuery.of("porygen")].orElse(null)
            // this.mapsByWorld[event.targetWorld.uniqueId]
        }
    }

    @Listener
    fun onUnloadWorld(event: UnloadWorldEvent) {

    }

    @Listener
    fun onPreGenerateChunk(event: GenerateChunkEvent.Pre) {

    }

    @Listener
    fun onLoadChunk(event: LoadChunkEvent) {
        val chunk = event.targetChunk
        val map = this.mapsByWorld[chunk.world.uniqueId] ?: return
        val chunkStartPos = chunk.position
        map.onLoadChunk(chunkStartPos.x shr 4, chunkStartPos.z shr 4)
    }

    @Listener
    fun onUnloadChunk(event: UnloadChunkEvent) {
        val chunk = event.targetChunk
        val map = this.mapsByWorld[chunk.world.uniqueId] ?: return
        val chunkStartPos = chunk.position
        map.onUnloadChunk(chunkStartPos.x shr 4, chunkStartPos.z shr 4)
    }

    @Listener
    fun onForcedChunk(event: ForcedChunkEvent) {
        val map = this.mapsByWorld[event.ticket.world.uniqueId] ?: return
        val chunkPos = event.chunkCoords
        map.onLoadChunk(chunkPos.x, chunkPos.z)
    }

    @Listener
    fun onUnforcedChunk(event: UnforcedChunkEvent) {
        val map = this.mapsByWorld[event.ticket.world.uniqueId] ?: return
        val chunkPos = event.chunkCoords
        map.onUnloadChunk(chunkPos.x, chunkPos.z)
    }
}
