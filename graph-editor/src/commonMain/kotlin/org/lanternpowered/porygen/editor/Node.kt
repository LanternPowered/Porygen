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
import androidx.compose.foundation.gestures.zoomable
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
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.preferredWidth
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.gesture.doubleTapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Bounds
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
import org.lanternpowered.porygen.util.type.GenericType

private val NodeHeaderPadding = 8.dp
private val NodeTextStyle = TextStyle(
  color = EditorColors.NodeText,
  fontSize = 16.sp
)

private class NodeGraphViewModel(
  private val graph: NodeGraph
) {

  val nodes = graph.nodes.map { NodeViewModel(it, this) }

  val connections = mutableStateListOf<NodeConnectionViewModel>()

  /**
   * Updates the positions of the connections.
   */
  fun updateConnectionPositions(nodeId: NodeId, portBounds: Map<PortId, Bounds>) {

  }
}

class NodeConnectionViewModel(
  private val input: InputPort<*>,
  private val output: OutputPort<*>
) {

  /**
   * The bounds of the input port.
   */
  val inputBounds by mutableStateOf(Offset.Zero)

  /**
   * The bounds of the output port.
   */
  val outputBounds by mutableStateOf(Offset.Zero)
}

private abstract class NodePortViewModel(
  private val port: Port<*>
) {

  val id: PortId
    get() = port.id

  val name: String
    get() = port.name

  val dataType: GenericType<*>
    get() = port.dataType

  val color: Color = Color.Red // TODO

  var state by mutableStateOf(PortState.Default)
}

private class NodeOutputViewModel(
  private val output: OutputPort<*>
) : NodePortViewModel(output)

private class NodeInputViewModel(
  private val input: InputPort<*>
) : NodePortViewModel(input) {

  fun isDataTypeAccepted(type: GenericType<*>): Boolean =
    input.isDataTypeAccepted(type)
}

class NodePropertyViewModel(
  private val property: Property<*>
) {

  val name: String
    get() = property.name
}

/**
 * The view model of a single node.
 */
private class NodeViewModel(
  private val node: Node,
  private val graphViewModel: NodeGraphViewModel
) {

  val id: NodeId
    get() = node.id

  var title by mutableStateOf(node.title)
    private set

  var position by mutableStateOf(node.position)
    private set

  var expanded by mutableStateOf(node.expanded)
    private set

  private var inputsByKey = node.inputs.associate { it.id to NodeInputViewModel(it) }
  private var outputsByKey = node.outputs.associate { it.id to NodeOutputViewModel(it) }

  val inputs get() = inputsByKey.values
  val outputs get() = outputsByKey.values
  val properties = node.properties.map(::NodePropertyViewModel)

  /**
   * The bounds of all the ports relative to the node graph grid.
   */
  var portBounds = mutableMapOf<PortId, Rect>()

  private var currentHover: NodePortViewModel? = null
  private var currentDrag: NodeOutputViewModel? = null

  fun onStartDragOutput(portId: PortId) {
    onStopDragOutput()
    val output = outputsByKey[portId] ?: error("Port with id $id is missing.")
    currentDrag = output
    output.state = PortState.Dragging

    for (node in graphViewModel.nodes) {
      for (otherOutput in node.outputs) {
        if (output == otherOutput)
          continue
        otherOutput.state = PortState.Disabled
      }
      for (otherInput in node.inputs) {
        if (otherInput.isDataTypeAccepted(output.dataType))
          continue
        otherInput.state = PortState.Disabled
      }
    }
  }

  fun onStopDragOutput() {
    if (currentDrag != currentHover)
      currentDrag?.state = PortState.Default
    currentDrag = null

    for (node in graphViewModel.nodes) {
      for (otherOutput in node.outputs) {
        if (otherOutput == currentHover)
          continue
        otherOutput.state = PortState.Default
      }
      for (otherInput in node.inputs) {
        if (otherInput == currentHover)
          continue
        otherInput.state = PortState.Default
      }
    }
  }

  fun onStartHover(portId: PortId) {
    onStopHover()
    val port = outputsByKey[portId] ?: inputsByKey[portId] ?: error("Port with id $id is missing.")
    currentHover = port
    port.state = PortState.HoveringOrConnected
  }

  fun onStopHover() {
    if (currentDrag != currentHover)
      currentHover?.state = PortState.Default
    currentHover = null
  }

  fun updatePortBounds(port: PortId, bounds: Rect) {
    portBounds[port] = bounds
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

  Box(
    modifier = Modifier
      .mouseScroll { delta, _ ->
        scale *= 1f + (-delta * 0.006f)
        true
      }
      .zoomable(onZoomDelta = { scale *= it })
      .onDrag(dragLock) { dragDistance ->
        translate += dragDistance
      }
      .doubleTapGestureFilter {
        // Re-center
        translate = Offset.Zero
      }
      .background(EditorColors.Background),
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .absoluteOffset(
          x = { translate.x },
          y = { translate.y }
        )
        .drawLayer(
          // https://github.com/JetBrains/compose-jb/issues/127
          //   scale will currently break tap/clickables
          scaleX = scale,
          scaleY = scale
        ),
    ) {
      NodeGraphGrid(
        dragLock = dragLock,
        scale = scale,
        graph = graph
      )
    }
  }
}

fun Vec2d.toOffset(): Offset =
  Offset(x.toFloat(), y.toFloat())

fun Offset.toVec2d(): Vec2d =
  Vec2d(x.toDouble(), y.toDouble())

@Composable
private fun NodeGraphGrid(
  dragLock: DragLock,
  scale: Float,
  graph: NodeGraph
) {
  val graphViewModel by remember { mutableStateOf(NodeGraphViewModel(graph)) }

  for (node in graphViewModel.nodes) {
    Node(
      viewModel = node,
      dragLock = dragLock,
      scale = scale
    )
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
      onImeActionPerformed = { finishEdit() },
      modifier = Modifier
        // Without this, content after the text field may
        // be pushed a bit out of bounds
        .preferredWidth(IntrinsicSize.Max)
        .padding(horizontal = horizontalTextPadding)
    )
  } else {
    Box(
      modifier = Modifier
        .clickable(
          onDoubleClick = { editTitle = true },
          onClick = {}
        )
        .doubleTapGestureFilter { editTitle = true }
    ) {
      BasicText(
        text = title,
        style = NodeTextStyle,
        softWrap = false,
        modifier = Modifier
          .padding(horizontal = horizontalTextPadding)
          // Make sure that the complete text is visible
          .preferredWidth(IntrinsicSize.Max)
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
      imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowLeft,
      tint = EditorColors.NodeText
    )
  }
}

@Composable
private fun NodeConnections(

) {

}

/**
 * @param dragLock The shared drag lock of the application
 */
@Composable
private fun Node(
  viewModel: NodeViewModel,
  dragLock: DragLock,
  scale: Float
) {
  val position = viewModel.position.toOffset()
  var nodeCoordinates: LayoutCoordinates? by remember { mutableStateOf(null) }

  val borderSize = 1.dp
  val roundedCornerShape = RoundedCornerShape(4.dp)

  Box(
    modifier = Modifier
      .absoluteOffset(
        x = { position.x },
        y = { position.y }
      )
      .preferredHeight(IntrinsicSize.Min)
      .preferredWidth(IntrinsicSize.Min)
      .onGloballyPositioned { coordinates -> nodeCoordinates = coordinates },
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
            viewModel.updatePosition((position + dragDistance / scale).toVec2d())
          }
        ) {
          NodeHeader(
            title = viewModel.title,
            onUpdateTitle = viewModel::updateTitle,
            expanded = viewModel.expanded,
            onUpdateExpanded = viewModel::updateExpanded,
          )
        }

        if (viewModel.expanded) {
          HorizontalDivider(
            thickness = borderSize,
            color = EditorColors.NodeInnerDivider
          )

          // Inputs and outputs
          Row(
            modifier = Modifier
              .preferredHeight(IntrinsicSize.Min)
          ) {
            // Inputs
            if (viewModel.inputs.isNotEmpty()) {
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
                  for (input in viewModel.inputs) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Port(
                        modifier = Modifier
                          .onHover(
                            onEnter = { viewModel.onStartHover(input.id) },
                            onExit = viewModel::onStopHover
                          )
                          .onGloballyPositioned { coordinates ->
                            val nodeCoordinates0 = nodeCoordinates
                              ?: return@onGloballyPositioned
                            val portBounds = nodeCoordinates0.childBoundingBox(coordinates)
                            viewModel.updatePortBounds(input.id, portBounds)
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
            if (viewModel.outputs.isNotEmpty()) {
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
                  for (output in viewModel.outputs) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      BasicText(text = output.name, style = NodeTextStyle)
                      Spacer(Modifier.width(5.dp))
                      Port(
                        modifier = Modifier
                          .onHover(
                            onEnter = { viewModel.onStartHover(output.id) },
                            onExit = { viewModel.onStopHover() }
                          )
                          .onDrag(
                            lock = dragLock,
                            onStart = { viewModel.onStartDragOutput(output.id) },
                            onStop = { viewModel.onStopDragOutput() }
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

          Box(Modifier.preferredHeight(6.dp))
        }
      }
    }
  }

  Canvas(Modifier) {
    for ((_, bounds) in viewModel.portBounds) {
      drawLine(Color.White, start = Offset.Zero, end = position + bounds.centerLeft)
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
      .preferredWidth(15.dp)
  ) {
    if (state != PortState.Disabled) {
      Box(modifier = Modifier
        .offset(
          x = if (!isInput) fullCircleSize / 2 else 0.dp,
          y = fullCircleSize / 2 + lineThickness / 2
        )
        .preferredSize(
          height = lineThickness,
          width = lineLength
        )
        .background(color.copy(alpha = 0.6f))
      )
    }
    Box(modifier = Modifier
      .offset(x = if (!isInput) 0.dp else lineLength - fullCircleSize / 2)
      .clip(CircleShape)
      .background(if (state == PortState.Disabled || state == PortState.Dragging) Color.Transparent else color)
    ) {
      Box(modifier = Modifier
        .padding(lineThickness)
        .clip(CircleShape)
        .background(if (state == PortState.Dragging) Color.Transparent else EditorColors.NodePortInner)
      ) {
        Box(modifier = Modifier
          .padding(fullCircleSize - centerCircle - lineThickness)
          .preferredSize(centerCircle)
          .clip(CircleShape)
          .background(if (state == PortState.HoveringOrConnected ||
            state == PortState.Dragging) color else EditorColors.NodePortInner)
        )
      }
    }
  }
}
