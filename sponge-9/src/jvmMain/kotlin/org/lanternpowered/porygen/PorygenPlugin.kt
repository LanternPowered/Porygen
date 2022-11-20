/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.spongepowered.api.Server
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.StartedEngineEvent
import org.spongepowered.api.event.world.LoadWorldEvent
import org.spongepowered.api.event.world.UnloadWorldEvent
import org.spongepowered.api.event.world.chunk.ChunkEvent
import org.spongepowered.plugin.builtin.jvm.Plugin

@Plugin("porygen")
class PorygenPlugin @Inject constructor(
  val logger: Logger
) {

  @Listener
  fun onInit(event: StartedEngineEvent<Server>) {
    logger.info("Initializing!")
  }

  @Listener
  fun onLoadWorld(event: LoadWorldEvent) {

  }

  @Listener
  fun onUnloadWorld(event: UnloadWorldEvent) {

  }

  @Listener
  fun onLoadChunk(event: ChunkEvent.Load) {

  }

  @Listener
  fun onUnloadChunk(event: ChunkEvent.Unload) {

  }
}
