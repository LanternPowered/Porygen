package org.lanternpowered.porygen.graph.serializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.serializerOrNull
import org.lanternpowered.porygen.graph.node.DynamicNodeImpl
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.NodeGraphImpl
import org.lanternpowered.porygen.graph.node.NodeId
import org.lanternpowered.porygen.graph.node.SpecNodeImpl
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.graph.node.property.PropertyId
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpec
import org.lanternpowered.porygen.graph.node.spec.NodeGraphSpecImpl
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.unsafeCast

private object Keys {
  const val Nodes = "nodes"
  const val NodeId = "nodeId"
  const val PortId = "portId"
  const val Id = "id"
  const val Spec = "spec"
  const val Title = "title"
  const val Position = "position"
  const val Expanded = "expanded"
  const val Connections = "connections"
  const val Outputs = "outputs"
  const val Value = "value"
  const val Properties = "properties"
}

fun JsonObject.require(key: String) = this[key] ?: error("Missing key $key")

fun JsonObject.toNodeGraph(graphSpec: NodeGraphSpec): NodeGraph {
  graphSpec as NodeGraphSpecImpl
  val graph = NodeGraphImpl(graphSpec)
  val json = this
  val nodeJsonArray = json.require(Keys.Nodes).jsonArray
  nodeJsonArray.forEach { nodeJson ->
    nodeJson as JsonObject
    val id = nodeJson.require(Keys.Id).nodeId
    val specId = nodeJson.require(Keys.Spec).jsonPrimitive.content
    val spec = graphSpec.nodeSpec(specId)
      ?: error("Unable to find node spec with id $specId")
    val position = Json.decodeFromJsonElement<Vec2d>(nodeJson.require(Keys.Position))
    val node = graph.create(id, spec, position)
    node.title = nodeJson.require(Keys.Title).jsonPrimitive.content
    node.expanded = nodeJson.require(Keys.Expanded).jsonPrimitive.boolean
    nodeJson.require(Keys.Properties).jsonArray.forEach { propertyJson ->
      propertyJson as JsonObject
      val propertyId = PropertyId(propertyJson.require(Keys.Id).jsonPrimitive.content)
      val property = node.property(propertyId).unsafeCast<Property<Any?>?>()
      if (property != null) {
        val serializer = serializerOrNull(property.dataType.kType)
          ?: error("${property.dataType} is not deserializable")
        val value = propertyJson.require(Keys.Value)
        property.value = Json.decodeFromJsonElement(serializer, value)
      }
    }
  }
  nodeJsonArray.forEach { nodeJson ->
    nodeJson as JsonObject
    val id = NodeId(nodeJson.require(Keys.Id).jsonPrimitive.int)
    val node = graph[id]!!
    nodeJson.require(Keys.Outputs).jsonArray.forEach { outputPortJson ->
      outputPortJson as JsonObject
      val outputPortId = outputPortJson.require(Keys.Id).portId
      val outputPort = node.output(outputPortId)
      if (outputPort != null) {
        outputPortJson.require(Keys.Connections).jsonArray.forEach { connectionJson ->
          connectionJson as JsonObject
          val inputPortId = connectionJson.require(Keys.PortId).portId
          val inputNodeId = connectionJson.require(Keys.NodeId).nodeId
          val inputNode = graph[inputNodeId]
          if (inputNode != null) {
            val inputPort = inputNode.input(inputPortId)
            if (inputPort != null) {
              outputPort.connectTo(inputPort)
            }
          }
        }
      }
    }
  }
  return graph
}

private val JsonElement.portId
  get() = PortId(jsonPrimitive.content)

private val JsonElement.nodeId
  get() = NodeId(jsonPrimitive.int)

fun NodeGraph.toJson(): JsonObject {
  val graph = this
  return buildJsonObject {
    val nodeArray = graph.nodes.map { node ->
      buildJsonObject {
        put(Keys.Id, node.id.value)
        when (node) {
          is SpecNodeImpl -> {
            put(Keys.Spec, node.spec.id)
          }
          is DynamicNodeImpl -> {
            TODO()
          }
          else -> {
            error("Unexpected node implementation: ${node::class.simpleName}")
          }
        }
        put(Keys.Title, node.title)
        put(Keys.Position, Json.encodeToJsonElement(node.position))
        put(Keys.Expanded, node.expanded)
        val outputPortArray = node.outputs.map { outputPort ->
          buildJsonObject {
            put(Keys.Id, outputPort.id.value)
            val connectionArray = outputPort.connections.map { inputPort ->
              buildJsonObject {
                put(Keys.NodeId, inputPort.node.id.value)
                put(Keys.PortId, inputPort.id.value)
              }
            }
            put(Keys.Connections, JsonArray(connectionArray))
          }
        }
        put(Keys.Outputs, JsonArray(outputPortArray))
        val propertyArray = node.properties.map { property ->
          buildJsonObject {
            put(Keys.Id, property.id.value)
            val serializer = serializerOrNull(property.dataType.kType)
              ?: error("${property.dataType} is not serializable")
            put(Keys.Value, Json.encodeToJsonElement(serializer, property.value))
          }
        }
        put(Keys.Properties, JsonArray(propertyArray))
      }
    }
    put(Keys.Nodes, JsonArray(nodeArray))
  }
}
