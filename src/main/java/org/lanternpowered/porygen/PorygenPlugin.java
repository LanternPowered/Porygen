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
package org.lanternpowered.porygen;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.GeneratorType;

@Plugin(id = "porygen")
public final class PorygenPlugin {

    private final Logger logger;
    private final PluginContainer container;
    private final Game game;

    @Inject
    public PorygenPlugin(Logger logger, PluginContainer container, Game game) {
        this.container = container;
        this.logger = logger;
        this.game = game;
    }

    @Listener
    public void onGamePreInit(GamePreInitializationEvent event) {
        // Register the generator types
        this.game.getRegistry().register(GeneratorType.class, new OverworldGeneratorType(
                this.container.getId(), "overworld", "Porygen Overworld"));
        this.game.getRegistry().register(GeneratorType.class, new NetherGeneratorType(
                this.container.getId(), "nether", "Porygen Nether"));
        this.game.getRegistry().register(GeneratorType.class, new TheEndGeneratorType(
                this.container.getId(), "the_end", "Porygen The End"));

        // TODO
        // These will just be the same as overworld, but with some custom settings
        this.game.getRegistry().register(GeneratorType.class, new OverworldGeneratorType(
                this.container.getId(), "amplified", "Porygen Amplified Overworld"));
        this.game.getRegistry().register(GeneratorType.class, new OverworldGeneratorType(
                this.container.getId(), "large_biomes", "Porygen Large Biomes Overworld"));

        // The default-world-gen.json is lantern related, a way to register the porygen
        // generator types as the default ones. The lantern default ones are flat generator,
        // this plugin is required for awesomeness.
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
    }

    @Listener
    public void onGamePostInit(GamePostInitializationEvent event) {
    }

    /**
     * Gets the {@link Logger} of this plugin.
     *
     * @return The logger
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Gets the {@link PluginContainer} of this plugin.
     *
     * @return The plugin container
     */
    public PluginContainer getContainer() {
        return this.container;
    }
}
