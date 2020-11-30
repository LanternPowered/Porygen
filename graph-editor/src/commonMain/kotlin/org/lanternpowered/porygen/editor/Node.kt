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

class NodeGraphViewModel(
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

abstract class NodePortViewModel(
  private val port: Port<*>
) {

  val id: PortId
    get() = port.id

  val name: String
    get() = port.name

  val color: Color = Color.Red // TODO
}

class NodeOutputViewModel(
  private val output: OutputPort<*>
) : NodePortViewModel(output) {

}

class NodeInputViewModel(
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
class NodeViewModel(
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

  val inputs = node.inputs.map(::NodeInputViewModel)
  val outputs = node.outputs.map(::NodeOutputViewModel)
  val properties = node.properties.map(::NodePropertyViewModel)

  /**
   * The bounds of all the ports relative to the node graph grid.
   */
  var portBounds = mutableMapOf<PortId, Rect>()

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

  val portDragTracker by remember { mutableStateOf(PortDragTracker(
    onStartDrag = { input ->
      //println("Start dragging: ${input.node.title}:${input.name}")
    },
    onStopDrag = { input, output ->
      /*
      print("Stop dragging: ${input.node.title}:${input.name}")
      if (output != null)
        print(" on top of: ${output.node.title}:${output.name}")
      println()
      */
    }
  )) }

  val graphViewModel by remember { mutableStateOf(NodeGraphViewModel(graph)) }

  for (node in graphViewModel.nodes) {
    Node(
      viewModel = node,
      dragLock = dragLock,
      portDragTracker = portDragTracker,
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

class PortDragTracker(
  val onStartDrag: (PortId) -> Unit,
  val onStopDrag: (PortId, PortId?) -> Unit
) {

  private var output: PortId? = null
  private var currentlyHovering: PortId? = null

  fun onStartHover(port: PortId) {
    currentlyHovering = port
  }

  fun onStopHover() {
    currentlyHovering = null
  }

  fun onStartDrag(port: PortId) {
    output = port
    onStartDrag.invoke(port)
  }

  fun onStopDrag() {
    val input = output ?: return
    onStopDrag.invoke(input, currentlyHovering)
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
  portDragTracker: PortDragTracker,
  dragLock: DragLock,
  scale: Float
) {
  val position = viewModel.position.toOffset()
  var nodeCoordinates: LayoutCoordinates? by remember { mutableStateOf(null) }

  val borderSize = 1.dp
  val roundedCornerShape = RoundedCornerShape(4.dp)

  Canvas(Modifier) {
    for ((_, bounds) in viewModel.portBounds)
      drawLine(Color.White, start = Offset.Zero, end = position + bounds.center)
  }

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
                    .padding(7.dp)
                ) {
                  for (input in viewModel.inputs) {
                    var isHovering by remember { mutableStateOf(false) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Box(modifier = Modifier
                        .preferredSize(10.dp)
                        .clip(CircleShape)
                        .background(if (isHovering) Color.Red else Color.Blue)
                        .onHover(
                          onEnter = {
                            isHovering = true
                            portDragTracker.onStartHover(input.id)
                          },
                          onExit = {
                            isHovering = false
                            portDragTracker.onStopHover()
                          }
                        )
                        .onGloballyPositioned { coordinates ->
                          val nodeCoordinates0 = nodeCoordinates
                            ?: return@onGloballyPositioned
                          val portBounds = nodeCoordinates0.childBoundingBox(coordinates)
                          viewModel.updatePortBounds(input.id, portBounds)
                        }
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
                    .padding(7.dp)
                ) {
                  for (output in viewModel.outputs) {
                    var isHovering by remember { mutableStateOf(false) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      BasicText(text = output.name, style = NodeTextStyle)
                      Spacer(Modifier.width(5.dp))
                      Box(modifier = Modifier
                        .preferredSize(10.dp)
                        .clip(CircleShape)
                        .background(if (isHovering) Color.Red else Color.Blue)
                        .onHover(
                          onEnter = { isHovering = true },
                          onExit = { isHovering = false }
                        )
                        .onDrag(
                          lock = dragLock,
                          onStart = { portDragTracker.onStartDrag(output.id) },
                          onStop = { portDragTracker.onStopDrag() }
                        )
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
}
