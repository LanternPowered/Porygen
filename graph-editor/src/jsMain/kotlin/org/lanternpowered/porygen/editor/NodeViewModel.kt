/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.NodeId
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.Colors
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection
import org.lanternpowered.porygen.util.type.GenericType

enum class PortState {
  Default,
  Dragging,
  HoveringOrConnected,
  Disabled,
}

class NodeGraphViewModel(
  val graph: NodeGraph,
  val save: () -> Unit,
) {

  private val nodesById = graph.nodes.associate { it.id to NodeViewModel(it, this) }

  val nodes = nodesById.values.asUnmodifiableCollection()

  private var currentHover: NodePortViewModel? = null

  /**
   * The output that's currently being dragged.
   */
  var draggedOutput: NodeOutputViewModel? by mutableStateOf(null)
    private set

  fun nodeOrNull(id: NodeId): NodeViewModel? = nodesById[id]

  fun node(id: NodeId): NodeViewModel =
    nodeOrNull(id) ?: error("There's no node with the id: $id")

  fun onStartDragOutputPort(output: NodeOutputViewModel) {
    onStopDragOutputPort()

    draggedOutput = output
    output.state = PortState.Dragging

    for (node in nodesById.values) {
      // Disable all the outputs except for the dragged one
      for (otherOutput in node.outputs) {
        if (output == otherOutput)
          continue
        otherOutput.state = PortState.Disabled
      }
      // Disable inputs that don't accept the data
      // type from the dragged output
      for (otherInput in node.inputs) {
        if (otherInput.isDataTypeAccepted(output.dataType))
          continue
        otherInput.state = PortState.Disabled
      }
    }
  }

  fun onStopDragOutputPort() {
    val currentHover = currentHover
    if (draggedOutput != currentHover) {
      val draggedOutput = draggedOutput
      draggedOutput?.state = PortState.Default
      if (draggedOutput != null &&
        currentHover is NodeInputViewModel &&
        currentHover.state != PortState.Disabled
      ) {
        draggedOutput.connectTo(currentHover)
      }
    }
    draggedOutput = null

    resetDisabled()
  }

  /**
   * Resets all the disabled ports.
   */
  private fun resetDisabled() {
    for (node in nodesById.values) {
      for (port in node.outputs + node.inputs) {
        if (port.state == PortState.Disabled)
          port.state = PortState.Default
      }
    }
  }

  fun onStartPortHover(port: NodePortViewModel) {
    val previousHover = currentHover
    val currentDrag = draggedOutput
    currentHover = port
    if (currentDrag != null && port.state == PortState.Disabled)
      return
    previousHover?.state = PortState.Default
    if (port != currentDrag)
      port.state = PortState.HoveringOrConnected
  }

  fun onStopPortHover() {
    val currentHover = currentHover
    this.currentHover = null
    if (currentHover != null &&
      currentHover.state != PortState.Disabled &&
      currentHover != draggedOutput
    ) {
      currentHover.state = PortState.Default
    }
  }
}

abstract class NodePortViewModel(
  val node: NodeViewModel
) {

  abstract val port: Port<*>

  val id: PortId
    get() = port.id

  val name: String
    get() = port.name

  val dataType: GenericType<*>
    get() = port.dataType

  val color: Color = Colors.Green // TODO

  var position by mutableStateOf(Vec2d.Zero)

  var state by mutableStateOf(PortState.Default)

  fun onStartHover() {
    node.onStartHover(this)
  }

  fun onStopHover() {
    node.onStopHover()
  }

  fun onStopDragOutput() {
    node.onStopDragOutput()
  }
}

class NodeOutputViewModel(
  node: NodeViewModel,
  override val port: OutputPort<*>
) : NodePortViewModel(node) {

  var tree: Any? by mutableStateOf(null)
  var connected: Boolean by mutableStateOf(false)

  init {
    rebuildTree()
  }

  fun rebuildTree() {
    tree = port.buildTree()
  }

  fun connectTo(input: NodeInputViewModel) {
    val previousConnectedNode = input.connection?.node
    if (!port.connectTo(input.port))
      return
    node.updateConnections()
    if (previousConnectedNode != null && previousConnectedNode != node)
      previousConnectedNode.updateConnections()
    input.node.updateConnections()
    input.node.rebuildTree()
    node.save()
  }

  fun onStartDragOutput() {
    node.onStartDragOutput(this)
  }
}

class NodeInputViewModel(
  node: NodeViewModel,
  override val port: InputPort<*>
) : NodePortViewModel(node) {

  val connection: NodeOutputViewModel?
    get() {
      val output = port.connection
        ?: return null
      return node.graphViewModel
        .node(output.node.id)
        .output(output.id)
    }

  fun disconnect() {
    val connectedNode = connection?.node ?: return
    port.disconnect()
    connectedNode.updateConnections()
    node.updateConnections()
    node.rebuildTree()
    node.save()
  }

  fun isDataTypeAccepted(type: GenericType<*>): Boolean =
    port.isDataTypeAccepted(type)
}

class NodePropertyViewModel(
  private val property: Property<*>
) {

  val name: String
    get() = property.name
}

class NodeConnection(
  val output: NodeOutputViewModel,
  private val inputPortId: PortId,
  private val inputNodeId: NodeId
) {

  val input: NodeInputViewModel
    get() = output.node.graphViewModel.node(inputNodeId).input(inputPortId)
}

/**
 * The view model of a single node.
 */
class NodeViewModel(
  val node: Node,
  val graphViewModel: NodeGraphViewModel
) {

  var initialized = false

  val id: NodeId
    get() = node.id

  var title by mutableStateOf(node.title)
    private set

  var position by mutableStateOf(node.position)
    private set

  var expanded by mutableStateOf(node.expanded)
    private set

  var bounds: Rectangled by mutableStateOf(Rectangled.Zero)
    private set

  var dragging by mutableStateOf(false)

  private var inputsByKey = node.inputs.associate { it.id to NodeInputViewModel(this, it) }
  private var outputsByKey = node.outputs.associate { it.id to NodeOutputViewModel(this, it) }

  val inputs get() = inputsByKey.values
  val outputs get() = outputsByKey.values
  val properties = node.properties.map(::NodePropertyViewModel)

  fun rebuildTree() {
    outputs.forEach { it.rebuildTree() }
  }

  fun save() {
    graphViewModel.save()
  }

  fun inputOrNull(portId: PortId): NodeInputViewModel? =
    inputsByKey[portId]

  fun outputOrNull(portId: PortId): NodeOutputViewModel? =
    outputsByKey[portId]

  fun input(portId: PortId): NodeInputViewModel =
    inputOrNull(portId) ?: error("There's no input port with the port id: $portId")

  fun output(portId: PortId): NodeOutputViewModel =
    outputOrNull(portId) ?: error("There's no output port with the port id: $portId")

  var connections: List<NodeConnection> by mutableStateOf(loadConnections())
    private set

  private fun loadConnections() = node.outputs
    .flatMap { output ->
      val outputViewModel = output(output.id)
      outputViewModel.connected = output.connections.isNotEmpty()
      output.connections.map { input ->
        NodeConnection(outputViewModel, input.id, input.node.id)
      }
    }

  fun updateConnections() {
    connections = loadConnections()
  }

  fun onStartDragOutput(port: NodeOutputViewModel) {
    graphViewModel.onStartDragOutputPort(port)
  }

  fun onStopDragOutput() {
    graphViewModel.onStopDragOutputPort()
  }

  private fun port(portId: PortId): NodePortViewModel =
    outputsByKey[portId] ?: inputsByKey[portId] ?: error("Port with id $id is missing.")

  fun onStartHover(port: NodePortViewModel) {
    graphViewModel.onStartPortHover(port)
  }

  fun onStopHover() {
    graphViewModel.onStopPortHover()
  }

  fun updatePortBounds(portId: PortId, bounds: Rectangled) {
    val port = port(portId)
    val y = bounds.min.y + (bounds.size.y) / 2
    val relative = if (port is NodeInputViewModel) {
      Vec2d(bounds.min.x, y)
    } else {
      Vec2d(bounds.max.x, y)
    }
    port.position = position + relative
  }

  fun updateBounds(bounds: Rectangled) {
    this.bounds = bounds
  }

  fun updateTitle(title: String) {
    node.title = title
    this.title = title
  }

  fun updatePosition(position: Vec2d) {
    node.position = position
    this.position = position
    graphViewModel.save()
  }

  fun updateExpanded(expanded: Boolean) {
    node.expanded = expanded
    this.expanded = expanded
  }
}

