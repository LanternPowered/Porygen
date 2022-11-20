/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.porygen.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.NodeId
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.port.PortId
import org.lanternpowered.porygen.graph.node.property.Property
import org.lanternpowered.porygen.math.vector.Vec2d
import org.lanternpowered.porygen.util.collections.asUnmodifiableCollection
import org.lanternpowered.porygen.util.type.GenericType
import kotlin.math.roundToInt

private val NodeHeaderPadding = 8.dp
private val NodeTextStyle = TextStyle(
  color = EditorColors.NodeText,
  fontSize = 15.sp
)

private class NodeGraphViewModel(
  private val graph: NodeGraph
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

private abstract class NodePortViewModel(
  val node: NodeViewModel,
  private val port: Port<*>
) {

  val id: PortId
    get() = port.id

  val name: String
    get() = port.name

  val dataType: GenericType<*>
    get() = port.dataType

  val color: Color = Color.Red // TODO

  var position: Offset = Offset.Zero

  var state by mutableStateOf(PortState.Default)
}

private class NodeOutputViewModel(
  node: NodeViewModel,
  private val output: OutputPort<*>
) : NodePortViewModel(node, output) {

  fun connectTo(input: NodeInputViewModel) {
    val previousConnectedNode = input.connection?.node
    if (!output.connectTo(input.input))
      return
    node.updateConnections()
    if (previousConnectedNode != null && previousConnectedNode != node)
      previousConnectedNode.updateConnections()
    input.node.updateConnections()
  }
}

private class NodeInputViewModel(
  node: NodeViewModel,
  val input: InputPort<*>
) : NodePortViewModel(node, input) {

  val connection: NodeOutputViewModel?
    get() {
      val output = input.connection
        ?: return null
      return node.graphViewModel
        .node(output.node.id)
        .output(output.id)
    }

  fun disconnect() {
    val connectedNode = connection?.node ?: return
    input.disconnect()
    connectedNode.updateConnections()
    node.updateConnections()
  }

  fun isDataTypeAccepted(type: GenericType<*>): Boolean =
    input.isDataTypeAccepted(type)
}

class NodePropertyViewModel(
  private val property: Property<*>
) {

  val name: String
    get() = property.name
}

private class NodeConnection(
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
private class NodeViewModel(
  private val node: Node,
  val graphViewModel: NodeGraphViewModel
) {

  val id: NodeId
    get() = node.id

  var title by mutableStateOf(node.title)
    private set

  var position by mutableStateOf(node.position)
    private set

  var expanded by mutableStateOf(node.expanded)
    private set

  var bounds: Rect by mutableStateOf(Rect.Zero)
    private set

  private var inputsByKey = node.inputs.associate { it.id to NodeInputViewModel(this, it) }
  private var outputsByKey = node.outputs.associate { it.id to NodeOutputViewModel(this, it) }

  val inputs get() = inputsByKey.values
  val outputs get() = outputsByKey.values
  val properties = node.properties.map(::NodePropertyViewModel)

  fun inputOrNull(portId: PortId): NodeInputViewModel? =
    inputsByKey[portId]

  fun outputOrNull(portId: PortId): NodeOutputViewModel? =
    outputsByKey[portId]

  fun input(portId: PortId): NodeInputViewModel =
    inputOrNull(portId) ?: error("There's no input port with the port id: $portId")

  fun output(portId: PortId): NodeOutputViewModel =
    outputOrNull(portId) ?: error("There's no output port with the port id: $portId")

  var connections by mutableStateOf(loadConnections())
    private set

  private fun loadConnections() = node.outputs
    .flatMap { output ->
      output.connections.map { input ->
        NodeConnection(output(output.id), input.id, input.node.id)
      }
    }

  fun updateConnections() {
    connections = loadConnections()
  }

  fun onStartDragOutput(portId: PortId) {
    val output = outputsByKey[portId] ?: error("Port with id $id is missing.")
    graphViewModel.onStartDragOutputPort(output)
  }

  fun onStopDragOutput() {
    graphViewModel.onStopDragOutputPort()
  }

  private fun port(portId: PortId): NodePortViewModel =
    outputsByKey[portId] ?: inputsByKey[portId] ?: error("Port with id $id is missing.")

  fun onStartHover(portId: PortId) {
    graphViewModel.onStartPortHover(port(portId))
  }

  fun onStopHover() {
    graphViewModel.onStopPortHover()
  }

  fun updatePortBounds(portId: PortId, bounds: Rect) {
    val port = port(portId)
    val relative = if (port is NodeInputViewModel) {
      bounds.centerLeft
    } else {
      bounds.centerRight
    }
    port.position = position.toOffset() + relative
  }

  fun updateBounds(bounds: Rect) {
    this.bounds = bounds
  }

  fun updateTitle(title: String) {
    node.title = title
    this.title = title
  }

  fun updatePosition(position: Vec2d) {
    node.position = position
    this.position = position
  }

  fun updateExpanded(expanded: Boolean) {
    node.expanded = expanded
    this.expanded = expanded
  }
}

@Composable
fun DraggableNodeGraphGrid(
  graph: NodeGraph
) {
  var scale by remember { mutableStateOf(1f) }
  var translate by remember { mutableStateOf(Offset.Zero) }
  val dragLock by remember { mutableStateOf(DragLock()) }
  var mousePosition by remember { mutableStateOf(Offset.Zero) }
  var dragGridCoordinates: LayoutCoordinates? by remember { mutableStateOf(null) }
  var translatedGridCoordinates: LayoutCoordinates? by remember { mutableStateOf(null) }

  fun updateScale(newScale: Float) {
    val oldScale = scale
    scale = newScale
    val deltaScale = newScale - oldScale
    translate -= (mousePosition - translate) * deltaScale
  }

  Box(
    modifier = Modifier
      .mouseScroll { delta, _ ->
        updateScale(scale * (1f + (-delta * 0.006f)))
        true
      }
      //.zoomable(onZoomDelta = { updateScale(scale * it) })
      .onDrag(dragLock) { dragDistance ->
        translate += dragDistance
      }
      .pointerInput(Unit) {
        detectTapGestures(onDoubleTap = {
          // Re-center
          translate = Offset.Zero
        })
      }
      .background(EditorColors.Background)
      .onGloballyPositioned {
        dragGridCoordinates = it
      }
      .onHover(
        onMove = {
          if (dragGridCoordinates != null && translatedGridCoordinates != null) {
            // Convert the mouse position in the draggable area to the translated and scaled grid
            mousePosition = translatedGridCoordinates!!.windowToLocal(
              dragGridCoordinates!!.localToWindow(it))
          }
        }
      ),
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .absoluteOffset { translate.toIntOffset() }
        .scale(
          // https://github.com/JetBrains/compose-jb/issues/127
          //   scale will currently break tap/clickables
          //   TODO: Recheck
          scaleX = scale,
          scaleY = scale
        )
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .onGloballyPositioned {
            translatedGridCoordinates = it
          }
      ) {
        NodeGraphGrid(
          dragLock = dragLock,
          scale = scale,
          graph = graph,
          mousePosition = mousePosition
        )
      }
    }
  }
}

fun Vec2d.toOffset(): Offset =
  Offset(x.toFloat(), y.toFloat())

fun Offset.toVec2d(): Vec2d =
  Vec2d(x.toDouble(), y.toDouble())

fun Offset.toIntOffset(): IntOffset =
  IntOffset(x.roundToInt(), y.roundToInt())

@Composable
private fun NodeGraphGrid(
  dragLock: DragLock,
  scale: Float,
  graph: NodeGraph,
  mousePosition: Offset,
) {
  val graphViewModel by remember { mutableStateOf(NodeGraphViewModel(graph)) }

  Box {
    // TODO: Draw background grid?

    for (node in graphViewModel.nodes) {
      Node(
        node = node,
        dragLock = dragLock,
        scale = scale
      )
    }

    Canvas(Modifier) {
      val offset1 = Offset(-2.dp.toPx(), 0f)
      val offset2 = Offset(15.dp.toPx(), 0f)
      val thickness = 1.2.dp.toPx()

      val draggedOutput = graphViewModel.draggedOutput
      if (draggedOutput != null) {
        val position = draggedOutput.position
        val start = position + offset1
        val middle = position + offset2
        drawPoints(
          pointMode = PointMode.Polygon,
          points = listOf(start, middle, mousePosition),
          color = draggedOutput.color,
          strokeWidth = thickness,
          cap = StrokeCap.Round
        )
      }

      for (node in graphViewModel.nodes) {
        for (connection in node.connections) {
          val output = connection.output
          val input = connection.input

          val point1 = input.position - offset1
          val point2 = input.position - offset2
          val point3 = output.position + offset2
          val point4 = output.position + offset1
          drawPoints(
            pointMode = PointMode.Polygon,
            points = listOf(point1, point2, point3, point4),
            color = output.color,
            strokeWidth = thickness,
            cap = StrokeCap.Round
          )
        }
      }
    }
  }
}

@Composable
fun NodeTitle(
  title: String,
  onUpdateTitle: (String) -> Unit
) {
  var editTitle by remember { mutableStateOf(false) }
  fun finishEdit() {
    editTitle = false
    // Reset to the default title if needed
    if (title.isEmpty())
      onUpdateTitle("Title")
  }
  val horizontalTextPadding = 4.dp
  if (editTitle) {
    BasicTextField(
      value = title,
      textStyle = NodeTextStyle + TextStyle(textDecoration = TextDecoration.Underline),
      onValueChange = { onUpdateTitle(it) },
      singleLine = true,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      // onImeActionPerformed = { finishEdit() }, // TODO
      modifier = Modifier
        // Without this, content after the text field may
        // be pushed a bit out of bounds
        .requiredWidth(IntrinsicSize.Max)
        .padding(horizontal = horizontalTextPadding)
    )
  } else {
    Box(
      modifier = Modifier
        .combinedClickable(
          onDoubleClick = { editTitle = true },
          onClick = {}
        )
        .pointerInput(Unit) {
          detectTapGestures(onDoubleTap = {
            editTitle = true
          })
        }
    ) {
      BasicText(
        text = title,
        style = NodeTextStyle,
        softWrap = false,
        modifier = Modifier
          .padding(horizontal = horizontalTextPadding)
          // Make sure that the complete text is visible
          .requiredWidth(IntrinsicSize.Max)
      )
    }
  }
}

@Composable
fun NodeHeader(
  title: String,
  onUpdateTitle: (String) -> Unit,
  expanded: Boolean,
  onUpdateExpanded: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .padding(NodeHeaderPadding)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    NodeTitle(title, onUpdateTitle)

    Icon(
      modifier = Modifier
        .size(24.dp)
        .clickable(onClick = {
          onUpdateExpanded(!expanded)
        }),
      contentDescription = "Minimize/Maximize Node",
      imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowLeft,
      tint = EditorColors.NodeText
    )
  }
}

/**
 * @param dragLock The shared drag lock of the application
 */
@Composable
private fun Node(
  node: NodeViewModel,
  dragLock: DragLock,
  scale: Float
) {
  val position = node.position.toOffset()
  var nodeCoordinates: LayoutCoordinates? by remember { mutableStateOf(null) }

  val borderSize = 1.dp
  val roundedCornerShape = RoundedCornerShape(4.dp)

  Box(
    modifier = Modifier
      .absoluteOffset { position.toIntOffset() }
      .requiredHeight(IntrinsicSize.Min)
      .requiredWidth(IntrinsicSize.Min)
      .onGloballyPositioned { coordinates ->
        nodeCoordinates = coordinates
        node.updateBounds(coordinates.boundsInParent())
      }
  ) {
    var isSelected by remember { mutableStateOf(false) }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .border(
          width = borderSize,
          color = if (isSelected) EditorColors.NodeSelectionOutline else Color.Transparent,
          shape = roundedCornerShape
        )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(borderSize) // Needed to show double border
          .background(
            color = EditorColors.Node,
            shape = roundedCornerShape
          )
          .border(
            width = borderSize,
            color = EditorColors.NodeBorder,
            shape = roundedCornerShape
          )
          // This just holds the lock so the node area
          // can't acquire the lock when dragging
          .onDrag(dragLock)
          .onHover(
            onEnter = { isSelected = true; },
            onExit = { isSelected = false; }
          )
      ) {
        Box(modifier = Modifier
          .onDrag(dragLock) { dragDistance ->
            node.updatePosition((position + dragDistance / scale).toVec2d())
          }
        ) {
          NodeHeader(
            title = node.title,
            onUpdateTitle = node::updateTitle,
            expanded = node.expanded,
            onUpdateExpanded = node::updateExpanded,
          )
        }

        if (node.expanded) {
          HorizontalDivider(
            thickness = borderSize,
            color = EditorColors.NodeInnerDivider
          )

          // Inputs and outputs
          Row(
            modifier = Modifier
              .requiredHeight(IntrinsicSize.Min)
          ) {
            // Inputs
            if (node.inputs.isNotEmpty()) {
              Column(
                modifier = Modifier
                  .background(EditorColors.NodeInputs)
                  .weight(1f)
                  .fillMaxHeight()
              ) {
                Column(
                  modifier = Modifier
                    .padding(end = 7.dp, top = 7.dp, bottom = 7.dp)
                ) {
                  for (input in node.inputs) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Port(
                        modifier = Modifier
                          .onHover(
                            onEnter = { node.onStartHover(input.id) },
                            onExit = node::onStopHover
                          )
                          .clickable(
                            onClick = { input.disconnect() }
                          )
                          .onGloballyPositioned { coordinates ->
                            val nodeCoordinates0 = nodeCoordinates
                              ?: return@onGloballyPositioned
                            val portBounds = nodeCoordinates0.localBoundingBoxOf(coordinates)
                            node.updatePortBounds(input.id, portBounds)
                          },
                        color = input.color,
                        state = input.state,
                        isInput = true
                      )
                      Spacer(Modifier.width(5.dp))
                      BasicText(text = input.name, style = NodeTextStyle)
                    }
                  }
                }
              }
            }
            VerticalDivider(
              thickness = borderSize,
              color = EditorColors.NodeInnerDivider
            )
            if (node.outputs.isNotEmpty()) {
              // Outputs
              Column(
                modifier = Modifier
                  .background(EditorColors.NodeOutputs)
                  .weight(1f)
                  .fillMaxHeight(),
                horizontalAlignment = Alignment.End
              ) {
                Column(
                  modifier = Modifier
                    .padding(start = 7.dp, top = 7.dp, bottom = 7.dp)
                ) {
                  for (output in node.outputs) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      BasicText(text = output.name, style = NodeTextStyle)
                      Spacer(Modifier.width(5.dp))
                      Port(
                        modifier = Modifier
                          .onGloballyPositioned { coordinates ->
                            val nodeCoordinates0 = nodeCoordinates
                              ?: return@onGloballyPositioned
                            val portBounds = nodeCoordinates0.localBoundingBoxOf(coordinates)
                            node.updatePortBounds(output.id, portBounds)
                          }
                          .onHover(
                            onEnter = { node.onStartHover(output.id) },
                            onExit = { node.onStopHover() }
                          )
                          .onDrag(
                            lock = dragLock,
                            onStart = { node.onStartDragOutput(output.id) },
                            onEnd = { node.onStopDragOutput() }
                          ),
                        color = output.color,
                        state = output.state,
                        isInput = false
                      )
                    }
                  }
                }
              }
            }
          }

          HorizontalDivider(
            thickness = borderSize,
            color = EditorColors.NodeInnerDivider
          )

          Box(Modifier.requiredHeight(6.dp))
        }
      }
    }
  }
}

private enum class PortState {
  Default,
  Dragging,
  HoveringOrConnected,
  Disabled,
}

@Composable
private fun Port(
  modifier: Modifier,
  color: Color,
  state: PortState,
  isInput: Boolean
) {
  val fullCircleSize = 7.2.dp
  val lineThickness = 1.2.dp
  val centerCircle = 4.5.dp
  val lineLength = 10.dp
  Box(
    modifier = modifier
      .requiredWidth(15.dp)
  ) {
    if (state != PortState.Disabled) {
      Box(modifier = Modifier
        .offset(
          x = if (!isInput) fullCircleSize / 2 else 0.dp,
          y = fullCircleSize / 2 + lineThickness / 2
        )
        .requiredSize(
          height = lineThickness,
          width = lineLength
        )
        .background(color.copy(alpha = 0.6f))
      )
    }
    Box(modifier = Modifier
      .offset(x = if (!isInput) 0.dp else lineLength - fullCircleSize / 2)
      .clip(CircleShape)
      .background(when (state) {
        PortState.Dragging -> Color.Transparent
        PortState.Disabled -> EditorColors.NodeBorder
        else -> color
      })
    ) {
      Box(modifier = Modifier
        .padding(lineThickness)
        .clip(CircleShape)
        .background(if (state == PortState.Dragging) Color.Transparent else EditorColors.NodePortInner)
      ) {
        Box(modifier = Modifier
          .padding(fullCircleSize - centerCircle - lineThickness)
          .requiredSize(centerCircle)
          .clip(CircleShape)
          .background(if (state == PortState.HoveringOrConnected ||
            state == PortState.Dragging) color else EditorColors.NodePortInner)
        )
      }
    }
  }
}
