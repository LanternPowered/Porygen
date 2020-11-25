/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.node

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject

@Serializer(forClass = NodeImpl::class)
internal class NodeSerializer : KSerializer<NodeImpl> {

  override val descriptor: SerialDescriptor = serialDescriptor<JsonObject>()

  override fun deserialize(decoder: Decoder): NodeImpl {
    /*
    val json = JsonObject.serializer().deserialize(decoder)

    val id = json["id"]!!.jsonPrimitive.int
    val typeId = json["type"]!!.jsonPrimitive.content
    val type = NodeTypeRegistry.get(typeId) ?:
    throw IllegalStateException("Unknown node type: $typeId")
    val title = json["title"]!!.jsonPrimitive.content

    val node = Node(NodeId(id), type)
    node.title = title
    node.position = Json.decodeFromJsonElement(Vec2d.serializer(), json["pos"]!!)

    // TODO: Properties, inputs, outputs

    return node
    */
    TODO()
  }

  override fun serialize(encoder: Encoder, value: NodeImpl) {
    TODO("Not yet implemented")
  }
}
