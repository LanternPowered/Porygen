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
package org.lanternpowered.porygen.settings.json;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.lanternpowered.porygen.settings.ListOperation;
import org.lanternpowered.porygen.settings.ParentBasedBiomeGenerationSettings;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.GenerationPopulator;
import org.spongepowered.api.world.gen.Populator;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Nullable;

public class ParentBasedBiomeGenerationSettingsParser implements JsonDeserializer<ParentBasedBiomeGenerationSettings> {

    private final static String PARENT = "parent";
    private final static String HEIGHT = "height";
    private final static String MIN = "min";
    private final static String MAX = "max";

    @Override
    public ParentBasedBiomeGenerationSettings deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        final JsonObject obj = element.getAsJsonObject();

        // Get the id of the parent
        final String parent = obj.has(PARENT) ? obj.get(PARENT).getAsString() : null;

        final JsonObject height = obj.getAsJsonObject(HEIGHT);
        Double minHeight = null;
        Double maxHeight = null;
        if (height != null) {
            if (obj.has(MIN)) {
                minHeight = obj.get(MIN).getAsDouble();
            } else {
                check(parent != null, "Missing min height value");
            }
            if (obj.has(MAX)) {
                maxHeight = obj.get(MAX).getAsDouble();
            } else {
                check(parent != null, "Missing max height value");
            }
        } else {
            check(parent != null, "Missing height object");
        }

        final Tuple<List<Populator>, ListOperation> populators =
                parseList(obj, "populators", ctx, Populator.class);
        final Tuple<List<GenerationPopulator>, ListOperation> genPopulators =
                parseList(obj, "generation-populators", ctx, GenerationPopulator.class);
        final Tuple<List<GroundCoverLayer>, ListOperation> groundCoverLayers =
                parseList(obj, "ground-cover-layers", ctx, GroundCoverLayer.class);

        return new ParentBasedBiomeGenerationSettings(parent, populators, genPopulators, groundCoverLayers, minHeight, maxHeight);
    }

    @Nullable
    private static <T> Tuple<List<T>, ListOperation> parseList(JsonObject json, String key, JsonDeserializationContext ctx, Class<T> type) {
        final OperatedJsonElement element = OperatedJsonElement.get(json, key);
        if (element == null) {
            return null;
        }
        final List<T> list = ctx.deserialize(element.getElement(), new TypeToken<List<T>>() {}.where(new TypeParameter<T>() {}, type));
        final ListOperation operation = ListOperation.parse(element.getOperations());
        return new Tuple<>(list, operation);
    }
}
