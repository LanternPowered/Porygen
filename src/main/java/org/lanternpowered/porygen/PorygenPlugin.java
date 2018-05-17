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
import org.spongepowered.api.event.game.GameRegistryEvent;
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
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
    }

    @Listener
    public void onGamePostInit(GamePostInitializationEvent event) {
    }

    @Listener
    public void onRegisterGeneratorTypes(GameRegistryEvent.Register<GeneratorType> event) {
        // Register the generator types
        event.register(new OverworldGeneratorType("overworld", "Porygen Overworld"));
        event.register(new NetherGeneratorType("nether", "Porygen Nether"));
        event.register(new TheEndGeneratorType("the_end", "Porygen The End"));

        // TODO
        // These will just be the same as overworld, but with some custom settings
        event.register(new OverworldGeneratorType("amplified", "Porygen Amplified Overworld"));
        event.register(new OverworldGeneratorType("large_biomes", "Porygen Large Biomes Overworld"));

        // The default-world-gen.json is lantern related, a way to register the porygen
        // generator types as the default ones. The lantern default ones are flat generator,
        // this plugin is required for awesomeness.
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
