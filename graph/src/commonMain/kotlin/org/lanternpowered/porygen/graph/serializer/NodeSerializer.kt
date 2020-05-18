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

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlinx.serialization.json.int
import org.lanternpowered.porygen.math.vector.Vector2d
import org.lanternpowered.porygen.graph.Node
import org.lanternpowered.porygen.graph.NodeTypeRegistry

@Serializer(forClass = Node::class)
class NodeSerializer : KSerializer<Node> {

  override val descriptor: SerialDescriptor = SerialDescriptor("node", StructureKind.OBJECT)

  override fun deserialize(decoder: Decoder): Node {
    val json = JsonObject.serializer().deserialize(decoder)

    val id = json["id"]!!.int
    val typeId = json["type"]!!.content
    val type = NodeTypeRegistry.get(typeId) ?:
        throw IllegalStateException("Unknown node type: $typeId")
    val title = json["title"]!!.content

    val node = Node(id, type)
    node.title = title
    node.position = Json.fromJson(Vector2d.serializer(), json["pos"]!!)

    // TODO: Properties, inputs, outputs
    
    return node
  }

  override fun serialize(encoder: Encoder, value: Node) {
    TODO("Not yet implemented")
  }
}
