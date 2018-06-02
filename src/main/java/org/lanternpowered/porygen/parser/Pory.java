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
package org.lanternpowered.porygen.parser;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A parsing system inspired by gson, but more flexible in
 * the way of functional programming.
 */
@SuppressWarnings("unchecked")
public final class Pory {

    /**
     * The current {@link PoryParserContext} stack.
     */
    private static final ThreadLocal<PoryParserContext> currentContext = new ThreadLocal<>();

    /**
     * Gets the current {@link PoryParserContext}, or
     * throws {@link IllegalStateException} if not present.
     *
     * @return The current context
     */
    static PoryParserContext currentContext() {
        final PoryParserContext context = currentContext.get();
        checkState(context != null, "There is not active context");
        return context;
    }

    // Parser order matters
    private final Map<TypeToken<?>, PoryParser> parsers = new LinkedHashMap<>();
    // The context that is used within parsers
    private final PoryParserContext context = new PoryParserContext() {
        @Override
        public <T> T parse(PoryElement element, Type objectType) {
            return parse0(element, (TypeToken<T>) TypeToken.of(objectType));
        }

        @Override
        public <T> T parse(PoryElement element, Class<T> objectType) {
            return parse0(element, TypeToken.of(objectType));
        }

        @Override
        public <T> T parse(PoryElement element, TypeToken<T> objectType) {
            return parse0(element, objectType);
        }
    };

    {
        registerParser(Boolean.class, (element, type, ctx) -> element.asBoolean());
        registerParser(Byte.class, (element, type, ctx) -> element.asByte());
        registerParser(Character.class, (element, type, ctx) -> element.asChar());
        registerParser(Double.class, (element, type, ctx) -> element.asDouble());
        registerParser(Float.class, (element, type, ctx) -> element.asFloat());
        registerParser(Integer.class, (element, type, ctx) -> element.asInt());
        registerParser(Long.class, (element, type, ctx) -> element.asLong());
        registerParser(Short.class, (element, type, ctx) -> element.asShort());
        registerParser(String.class, (element, type, ctx) -> element.asString());
        registerParser(Number.class, (element, type, ctx) -> element.asNumber());
        registerParser(Enum.class, new EnumParser());
        registerParser(Optional.class, new OptionalParser<>());
        registerParser(List.class, new ListParser<>());
        registerParser(Set.class, new SetParser<>());
        registerParser(Map.class, new MapParser<>());
        registerParser(OperatedPoryObject.class, new OperatedPoryObjectParser());
    }

    public <T> T parse(PoryElement element, Type objectType) {
        return parse(element, (TypeToken<T>) TypeToken.of(objectType));
    }

    public <T> T parse(PoryElement element, Class<T> objectType) {
        return parse(element, TypeToken.of(objectType));
    }

    public <T> T parse(PoryElement element, TypeToken<T> objectType) {
        final PoryParserContext lastContext = currentContext.get();
        try {
            // Update the current context if it changed
            if (this.context != lastContext) {
                currentContext.set(this.context);
            }
            return parse0(element, objectType);
        } finally {
            if (this.context != lastContext) { // Reset back to the previous context
                currentContext.set(lastContext);
            }
        }
    }

    private <T> T parse0(PoryElement element, TypeToken<T> objectType) {
        return null;
    }

    /**
     * Registers a {@link PoryParser} for the given {@link Class}.
     *
     * @param type The type token
     * @param parser The parser
     * @param <T> The object type to be parsed
     * @return This object, for chaining
     */
    public <T> Pory registerParser(Type type, PoryParser<T> parser) {
        return registerParser((TypeToken<T>) TypeToken.of(type), parser);
    }

    /**
     * Registers a {@link PoryParser} for the given {@link TypeToken}.
     *
     * @param type The type token
     * @param parser The parser
     * @param <T> The object type to be parsed
     * @return This object, for chaining
     */
    public <T> Pory registerParser(TypeToken<T> type, PoryParser<T> parser) {
        this.parsers.put(type, parser);
        return this;
    }

    /**
     * Converts the given {@link JsonElement} into a {@link PoryElement}.
     *
     * @param element The json element
     * @return The pory element
     */
    public static PoryElement fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new PoryPrimitive(primitive.getAsString());
            } else if (primitive.isBoolean()) {
                return new PoryPrimitive(primitive.getAsBoolean());
            } else if (primitive.isNumber()) {
                return new PoryPrimitive(primitive.getAsNumber());
            }
        } else if (element.isJsonArray()) {
            // Guava isn't up to date with sponge?
            return new PoryArray(StreamSupport.stream(element.getAsJsonArray().spliterator(), false)
                    .map(Pory::fromJson).collect(Collectors.toList()));
        } else if (element.isJsonObject()) {
            final PoryObject object = new PoryObject();
            element.getAsJsonObject().entrySet().forEach(e -> object.put(e.getKey(), fromJson(e.getValue())));
            return object;
        } else if (element.isJsonNull()) {
            return PoryNull.INSTANCE;
        }
        throw new IllegalStateException();
    }

    /**
     * Converts the given {@link ConfigValue} into a {@link PoryElement}.
     *
     * @param configValue The config value
     * @return The pory element
     */
    public static PoryElement fromConfigValue(ConfigValue configValue) {
        final ConfigValueType valueType = configValue.valueType();
        if (valueType == ConfigValueType.OBJECT) {
            final ConfigObject configObject = (ConfigObject) configValue;
            final PoryObject object = new PoryObject();
            configObject.forEach((key, value) -> object.put(key, fromConfigValue(value)));
            return object;
        } else if (valueType == ConfigValueType.LIST) {
            final ConfigList configList = (ConfigList) configValue;
            return new PoryArray(configList.stream().map(Pory::fromConfigValue).collect(Collectors.toList()));
        } else if (valueType == ConfigValueType.NULL) {
            return PoryNull.INSTANCE;
        } else {
            return new PoryPrimitive(configValue.unwrapped());
        }
    }
}
