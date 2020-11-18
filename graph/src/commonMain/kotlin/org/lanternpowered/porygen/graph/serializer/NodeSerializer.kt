/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import org.lanternpowered.porygen.graph.Node
import org.lanternpowered.porygen.graph.NodeTypeRegistry
import org.lanternpowered.porygen.math.vector.Vector2d

@Serializer(forClass = Node::class)
class NodeSerializer : KSerializer<Node> {

  override val descriptor: SerialDescriptor = serialDescriptor<JsonObject>()

  override fun deserialize(decoder: Decoder): Node {
    val json = JsonObject.serializer().deserialize(decoder)

    val id = json["id"]!!.jsonPrimitive.int
    val typeId = json["type"]!!.jsonPrimitive.content
    val type = NodeTypeRegistry.get(typeId) ?:
    throw IllegalStateException("Unknown node type: $typeId")
    val title = json["title"]!!.jsonPrimitive.content

    val node = Node(id, type)
    node.title = title
    node.position = Json.decodeFromJsonElement(Vector2d.serializer(), json["pos"]!!)

    // TODO: Properties, inputs, outputs

    return node
  }

  override fun serialize(encoder: Encoder, value: Node) {
    TODO("Not yet implemented")
  }
}
