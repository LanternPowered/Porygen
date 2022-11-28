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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.web.events.SyntheticMouseEvent
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.borderWidth
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.div
import org.jetbrains.compose.web.css.flexBasis
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.transform
import org.jetbrains.compose.web.css.transitions
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.svg.LinearGradient
import org.jetbrains.compose.web.svg.Polyline
import org.jetbrains.compose.web.svg.Stop
import org.jetbrains.compose.web.svg.Svg
import org.lanternpowered.porygen.editor.spec.NodeGraphEditorSpec
import org.lanternpowered.porygen.editor.web.css.Cursor
import org.lanternpowered.porygen.editor.web.css.Overflow
import org.lanternpowered.porygen.editor.web.css.Width
import org.lanternpowered.porygen.graph.node.Node
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.node.port.OutputPort
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.graph.node.spec.OutputPortSpec
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.radToDeg
import org.lanternpowered.porygen.math.vector.Vec2d
import kotlin.math.atan

private val gridSize = 1000000.px
private val halfGridSize = gridSize / 2
private const val gridCenterId = "grid-center"

@Composable
fun NodeGrid(graph: NodeGraph, editorSpec: NodeGraphEditorSpec) {
  val graphViewModel by remember { mutableStateOf(NodeGraphViewModel(graph, save = { save(graph) })) }
  var scale by remember { mutableStateOf(1.0) }
  var translate by remember { mutableStateOf(Vec2d(0.0, 0.0)) }
  var dragging by mutableStateOf(false)
  var mousePosition by mutableStateOf(Vec2d.Zero)

  fun updateMousePosition(event: SyntheticMouseEvent) {
    val rect = document.getElementById(gridCenterId).asDynamic().getBoundingClientRect()
    val x = event.clientX.toDouble() - rect.left as Double
    val y = event.clientY.toDouble() - rect.top as Double
    mousePosition = Vec2d(x, y) / scale
  }

  Div(
    attrs = {
      style {
        flexGrow(1)
        overflow(Overflow.Hidden)
        cursor(when {
          dragging || graphViewModel.nodes.any { node -> node.dragging } -> Cursor.Grabbing
          graphViewModel.draggedOutput != null -> Cursor.Auto
          else -> Cursor.Grab
        })
      }
      onWheel { event ->
        event.preventDefault()
        scale *= 1.0 + -event.deltaY * 0.0008
      }
      onDoubleClick {
        scale = 1.0
        translate = Vec2d.Zero
      }
      onMouseDown {
        dragging = true
      }
      onMouseUp {
        dragging = false
        graphViewModel.onStopDragOutputPort()
        graphViewModel.nodes.forEach { node -> node.dragging = false }
      }
      onMouseLeave {
        dragging = false
        graphViewModel.nodes.forEach { node -> node.dragging = false }
      }
      onMouseMove { event ->
        updateMousePosition(event)
        if (dragging) {
          val (tx, ty) = translate
          translate = Vec2d(tx + event.movementX, ty + event.movementY)
        } else {
          graphViewModel.nodes.forEach { node -> node.move(event, scale) }
        }
      }
    },
    content = {
      Div(
        attrs = {
          style {
            height(100.percent)
            width(gridSize)
            height(gridSize)
            transform {
              val (tx, ty) = translate
              translate(-halfGridSize + tx.px, -halfGridSize + ty.px)
            }
          }
        },
        content = {
          Div(
            attrs = {
              style {
                height(100.percent)
                width(100.percent)
                transform {
                  scale(scale)
                  transitions {
                    properties("transform") {
                      duration = 0.5.s
                      timingFunction = AnimationTimingFunction.EaseInOut
                    }
                  }
                }
              }
            },
            content = {
              Div(
                attrs = {
                  id(gridCenterId)
                  style {
                    position(Position.Absolute)
                    left(halfGridSize)
                    top(halfGridSize)
                  }
                },
                content = {
                  for (node in graphViewModel.nodes) {
                    Node(
                      editorSpec = editorSpec,
                      node = node,
                      scale = scale,
                      updateMousePosition = ::updateMousePosition,
                    )
                  }
                }
              )
              Div(
                attrs = {
                  style {
                    position(Position.Absolute)
                    property("z-index", -1)
                  }
                },
                content = {
                  Svg(
                    attrs = {
                      style {
                        overflow(Overflow.Visible)
                      }
                      onClick {
                        println("CLICK SVG")
                      }
                    },
                    content = {
                      @Composable
                      fun drawLine(
                        id: String,
                        start: Vec2d,
                        end: Vec2d,
                        startColor: CSSColorValue,
                        endColor: CSSColorValue
                      ) {
                        val gradient = startColor != endColor
                        if (gradient) {
                          LinearGradient(
                            attrs = {
                              val xDiff = end.x - start.x
                              val yDiff = end.y - start.y
                              val radians = atan(yDiff / xDiff)
                              var degrees = radToDeg(radians)
                              if (degrees.isNaN()) {
                                degrees = 0.0
                              }
                              attr("id", id)
                              attr("x1", if (start.x < end.x) "0%" else "100%")
                              attr("y1", "0%")
                              attr("x2", if (start.x < end.x) "100%" else "0%")
                              attr("y2", "0%")
                              attr("gradientTransform", "rotate(${degrees} 0.5 0.5)")
                            },
                            content = {
                              Stop(
                                attrs = {
                                  attr("offset", "0%")
                                  attr("style", "stop-color:$startColor;stop-opacity:1")
                                }
                              )
                              Stop(
                                attrs = {
                                  attr("offset", "100%")
                                  attr("style", "stop-color:$endColor;stop-opacity:1")
                                }
                              )
                            }
                          )
                        }
                        Polyline(
                          points = arrayOf(
                            halfGridSize.value + start.x - 7, // +7 to extend line to dot
                            halfGridSize.value + start.y + 1,
                            halfGridSize.value + start.x + 15,
                            halfGridSize.value + start.y + 1,
                            halfGridSize.value + end.x - 15,
                            halfGridSize.value + end.y + 1,
                            halfGridSize.value + end.x + 7, // +7 to extend line to dot, TODO: move value?
                            halfGridSize.value + end.y + 1,
                          ),
                          attrs = {
                            attr("fill", "none")
                            if (gradient) {
                              attr("stroke", "url(#$id)")
                            } else {
                              attr("stroke", startColor.toString())
                            }
                            attr("stroke-width", 2.px.toString())
                            attr("stroke-linejoin", "round")
                          }
                        )
                      }
                      val draggedOutput = graphViewModel.draggedOutput
                      if (draggedOutput != null) {
                        val color = draggedOutput.color.toCSS()
                        drawLine(
                          id = "dragged",
                          start = draggedOutput.position,
                          end = mousePosition,
                          startColor = color,
                          endColor = color,
                        )
                      }
                      for (node in graphViewModel.nodes) {
                        for (connection in node.connections) {
                          val output = connection.output
                          val input = connection.input
                          fun NodePortViewModel.ref() = "${id.value}_${node.id.value}"
                          val id = "${input.ref()}_to_${output.ref()}"
                          drawLine(
                            id = id,
                            start = output.position,
                            end = input.position,
                            startColor = input.color.toCSS(),
                            endColor = output.color.toCSS(),
                          )
                        }
                      }
                    }
                  )
                }
              )
            }
          )
        }
      )
    }
  )
}

private fun org.lanternpowered.porygen.util.Color.toCSS() = rgba(red, green, blue, alpha / 255.0)

private fun NodeViewModel.move(event: SyntheticMouseEvent, scale: Double) {
  if (dragging) {
    val movement = Vec2d(event.movementX.toDouble(), event.movementY.toDouble()) / scale
    updatePosition(position + movement)
    updateConnectionPositions()
  }
}

private fun NodeViewModel.updateConnectionPositions() {
  for (port in inputs + outputs) {
    val element = portElement(port.port)
    updatePortBounds(port.id, elementBounds(element))
  }
}

private fun elementBounds(element: dynamic): Rectangled {
  val offsetTop = element.offsetTop as Double
  val offsetLeft = element.offsetLeft as Double
  val offsetWidth = element.offsetWidth as Double
  val offsetHeight = element.offsetHeight as Double
  return Rectangled(
    minX = offsetLeft,
    minY = offsetTop,
    maxX = offsetLeft + offsetWidth,
    maxY = offsetTop + offsetHeight,
  )
}

private fun portElement(port: Port<*>): dynamic {
  return document.getElementById(portElementId(port))
}

private fun portElementId(port: Port<*>): String =
  "node_${port.node.id}_port_${port.id.value}"

@Composable
fun Node(
  editorSpec: NodeGraphEditorSpec,
  node: NodeViewModel,
  scale: Double,
  updateMousePosition: (SyntheticMouseEvent) -> Unit,
) {
  val borderWidth = 1.px
  val borderRadius = 5.px
  Div(
    attrs = {
      style {
        position(Position.Absolute)
        val position = node.position
        left(position.x.px)
        top(position.y.px)
        backgroundColor(EditorColors.Node)
        border(
          width = borderWidth,
          style = LineStyle.Solid,
          color = EditorColors.NodeBorder,
        )
        borderRadius(borderRadius)
        width(Width.MaxContent)
        if (node.dragging) {
          property("z-index", 1)
        }
      }
      onMouseUp { event ->
        event.stopPropagation()
      }
      onMouseDown { event ->
        event.stopPropagation()
      }
      onDoubleClick {  event ->
        event.stopPropagation()
      }
    },
    content = {
      Div(
        attrs = {
          style {
            padding(8.px)
            border(
              style = LineStyle.Solid,
              color = EditorColors.NodeBorder,
            )
            borderWidth(
              top = 0.px,
              bottom = 1.px,
              left = 0.px,
              right = 0.px,
            )
          }
          onMouseDown {
            node.dragging = true
          }
          onMouseUp {
            node.dragging = false
          }
          onMouseMove { event ->
            event.stopPropagation()
            updateMousePosition(event)
            node.move(event, scale)
          }
        },
        content = {
          Text(node.title)
        }
      )
      val spec = node.node.spec!!
      val preview = editorSpec.spec(spec).preview
      Div(
        attrs = {
          style {
            display(DisplayStyle.Flex)
            cursor(if (node.dragging) Cursor.Grabbing else Cursor.Auto)
            if (preview != null) {
              border(
                style = LineStyle.Solid,
                color = EditorColors.NodeBorder,
              )
              borderWidth(
                top = 0.px,
                bottom = 1.px,
                left = 0.px,
                right = 0.px,
              )
            }
          }
        },
        content = {
          if (node.inputs.isNotEmpty()) {
            Div(
              attrs = {
                style {
                  flexBasis(30.percent)
                }
              },
              content = {
                for (input in node.inputs) {
                  NodePort(editorSpec, input)
                }
              }
            )
          }
          if (node.outputs.isNotEmpty()) {
            Div(
              attrs = {
                style {
                  flexBasis(30.percent)
                  flexGrow(1)
                  if (node.inputs.isNotEmpty()) {
                    border(
                      style = LineStyle.Solid,
                      color = EditorColors.NodeBorder,
                    )
                    borderWidth(
                      top = 0.px,
                      bottom = 0.px,
                      left = 1.px,
                      right = 0.px,
                    )
                  }
                  borderRadius(
                    topLeft = 0.px,
                    topRight = 0.px,
                    bottomLeft = if (node.inputs.isEmpty()) borderRadius - borderWidth else 0.px,
                    bottomRight = if (preview != null) 0.px else borderRadius - borderWidth,
                  )
                  backgroundColor(EditorColors.NodeOutputs)
                }
              },
              content = {
                for (output in node.outputs) {
                  NodePort(editorSpec, output)
                }
              }
            )
            if (!node.initialized) {
              // TODO: Is there a better way to wait for the elements to exist?
              node.initialized = true
              GlobalScope.launch {
                node.updateConnectionPositions()
              }
            }
          }
        }
      )
      if (preview != null) {
        preview(spec, NodeWrapper(node))
      }
    }
  )
}

@Composable
fun NodePort(
  editorSpec: NodeGraphEditorSpec,
  port: NodePortViewModel,
) {
  val right = port is NodeOutputViewModel
  var hovering by mutableStateOf(false)
  Div(
    attrs = {
      id(portElementId(port.port))
      style {
        if (right) {
          justifyContent(JustifyContent.End)
        }
        paddingLeft(if (right) 10.px else 0.px)
        paddingTop(4.px)
        paddingBottom(4.px)
        paddingRight(if (right) 0.px else 10.px)
        display(DisplayStyle.Flex)
        alignItems("center")
      }
    },
    content = {
      @Composable
      fun circle() {
        Div(
          attrs = {
            style {
              marginLeft(5.px)
              marginRight(5.px)
              height(10.px)
              width(10.px)
              backgroundColor(
                when (port.state) {
                  PortState.Disabled, PortState.Dragging -> EditorColors.NodePortInner
                  else -> port.color.toCSS()
                }
              )
              borderRadius(50.percent)
              display(DisplayStyle.Flex)
            }
            onMouseEnter {
              hovering = true
              port.onStartHover()
            }
            onMouseLeave {
              hovering = false
              port.onStopHover()
            }
            onMouseUp {
              port.onStopDragOutput()
            }
            if (port is NodeOutputViewModel) {
              onMouseDown {
                port.onStartDragOutput()
              }
            } else if (port is NodeInputViewModel) {
              onClick {
                port.disconnect()
              }
            }
          },
          content = {
            Div(
              attrs = {
                style {
                  height(8.px)
                  width(8.px)
                  backgroundColor(EditorColors.NodePortInner)
                  borderRadius(50.percent)
                  display(DisplayStyle.Flex)
                  property("margin", "auto")
                }
              },
              content = {
                val connected = when (port) {
                  is NodeInputViewModel -> port.connection != null
                  is NodeOutputViewModel -> port.connected
                  else -> false
                }
                if ((hovering && port.state != PortState.Disabled && (port.node.graphViewModel.draggedOutput != null || port is NodeOutputViewModel)) ||
                  port.state == PortState.Dragging || connected
                ) {
                  Div(
                    attrs = {
                      style {
                        height(4.px)
                        width(4.px)
                        backgroundColor(port.color.toCSS())
                        borderRadius(50.percent)
                        display(DisplayStyle.Flex)
                        property("margin", "auto")
                      }
                    }
                  )
                }
              }
            )
          }
        )
      }
      if (!right) {
        circle()
        NodePortContent(editorSpec, port.port as InputPort<*>, port as NodeInputViewModel)
      }
      Text(port.name)
      if (right) {
        circle()
      }
    }
  )
}

@Composable
fun <T> NodePortContent(
  editorSpec: NodeGraphEditorSpec,
  port: InputPort<T>,
  portViewModel: NodeInputViewModel,
) {
  val content = editorSpec.constantContent(port.dataType)
  if (content != null) {
    port.node.spec!!.content(InputPortUpdateWrapper(port, portViewModel))
  }
}

internal class InputPortUpdateWrapper<T>(
  private val delegate: InputPort<T>,
  private val inputViewModel: NodeInputViewModel,
) : InputPort<T> by delegate {

  override var value: T?
    get() = delegate.value
    set(value) {
      delegate.value = value
      inputViewModel.node.rebuildTree()
      inputViewModel.node.save()
    }
}

class NodeWrapper(
  private val nodeViewModel: NodeViewModel,
) : Node by nodeViewModel.node {

  override fun <T> port(spec: OutputPortSpec<T>): OutputPort<T>? =
    nodeViewModel.node.port(spec)?.let { OutputPortEditorWrapper(nodeViewModel.output(it.id)) }

  override fun <T> requirePort(spec: OutputPortSpec<T>): OutputPort<T> =
    nodeViewModel.node.requirePort(spec).let { OutputPortEditorWrapper(nodeViewModel.output(it.id)) }
}

internal class OutputPortEditorWrapper<T>(
  private val outputViewModel: NodeOutputViewModel,
) : OutputPort<T> by outputViewModel.port.unsafeCast<OutputPort<T>>() {

  override fun buildTree(): T? = outputViewModel.tree.unsafeCast<T?>()
}
