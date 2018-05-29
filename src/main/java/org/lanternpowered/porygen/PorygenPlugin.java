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
import org.lanternpowered.porygen.populator.PorygenPopulatorType;
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
import org.spongepowered.api.world.gen.PopulatorType;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.populator.RandomBlock;
import org.spongepowered.api.world.gen.populator.RandomObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Plugin(id = "porygen")
public final class PorygenPlugin {

    private final Logger logger;
    private final PluginContainer container;
    private final Game game;

    /**
     * All the {@link PorygenGeneratorType}s that will be registered by the plugin.
     */
    private final List<PorygenGeneratorType> generatorTypes = new ArrayList<>();

    @Inject
    public PorygenPlugin(Logger logger, PluginContainer container, Game game) {
        this.container = container;
        this.logger = logger;
        this.game = game;
    }

    @Listener
    public void onGamePreInit(GamePreInitializationEvent event) {
        this.logger.info("Registering generator types...");

        // Register the generator types
        this.generatorTypes.add(new OverworldGeneratorType("overworld", "Porygen Overworld"));
        this.generatorTypes.add(new NetherGeneratorType("nether", "Porygen Nether"));
        this.generatorTypes.add(new TheEndGeneratorType("the_end", "Porygen The End"));

        // TODO
        // These will just be the same as overworld, but with some custom settings
        this.generatorTypes.add(new OverworldGeneratorType("amplified", "Porygen Amplified Overworld"));
        this.generatorTypes.add(new OverworldGeneratorType("large_biomes", "Porygen Large Biomes Overworld"));
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
    }

    @Listener
    public void onGamePostInit(GamePostInitializationEvent event) {
    }

    @Listener
    public void onRegisterGeneratorModifiers(GameRegistryEvent.Register<WorldGeneratorModifier> event) {
        this.logger.info("Registering world generator modifiers...");

        for (PorygenGeneratorType generatorType : this.generatorTypes) {
            this.logger.info("Registered the generator modifier '{}' with the name '{}'",
                    generatorType.getId(), generatorType.getName());
            event.register(generatorType);
        }
    }

    /**
     * Will only be called on platforms that support custom {@link GeneratorType}s.
     *
     * @param event The event
     */
    @Listener
    public void onRegisterGeneratorTypes(GameRegistryEvent.Register<GeneratorType> event) {
        this.logger.info("Registering generator types...");

        for (PorygenGeneratorType generatorType : this.generatorTypes) {
            this.logger.info("Registered the generator type '{}' with the name '{}'",
                    generatorType.getId(), generatorType.getName());
            event.register(new GeneratorModifierToType(generatorType));
        }

        // The default-world-gen.json is lantern related, a way to register the porygen
        // generator types as the default ones. The lantern default ones are flat generator,
        // this plugin is required for awesomeness.
    }

    /**
     * Will only be called on platforms that support custom {@link GeneratorType}s.
     *
     * @param event The event
     */
    @Listener
    public void onRegisterPopulatorTypes(GameRegistryEvent.Register<PopulatorType> event) {
        this.logger.info("Registering world populator types...");

        final Consumer<PopulatorType> register = type -> {
            event.register(type);
            this.logger.info("Registered the populator type '{}' with the name '{}'",
                    type.getId(), type.getName());
        };

        // A few alias populator types, to reflect their class name
        register.accept(new PorygenPopulatorType<>("random_object", "Random Object", RandomObject.class));
        register.accept(new PorygenPopulatorType<>("random_block", "Random Block", RandomBlock.class));
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
