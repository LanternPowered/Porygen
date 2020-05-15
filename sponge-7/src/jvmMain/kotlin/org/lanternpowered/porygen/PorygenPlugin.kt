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
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.world.LoadWorldEvent
import org.spongepowered.api.event.world.UnloadWorldEvent
import org.spongepowered.api.event.world.chunk.LoadChunkEvent
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(id = "porygen")
class PorygenPlugin @Inject constructor(
    val logger: Logger
) {

  @Listener
  fun onInit(event: GameInitializationEvent) {
    logger.info("Initializing!")
  }

  @Listener
  fun onLoadWorld(event: LoadWorldEvent) {

  }

  @Listener
  fun onUnloadWorld(event: UnloadWorldEvent) {

  }

  @Listener
  fun onLoadChunk(event: LoadChunkEvent) {

  }

  @Listener
  fun onUnloadChunk(event: UnloadChunkEvent) {

  }
}
