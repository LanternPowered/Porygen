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
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
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
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.transform
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.svg.Line
import org.jetbrains.compose.web.svg.Svg
import org.lanternpowered.porygen.editor.web.css.Cursor
import org.lanternpowered.porygen.editor.web.css.Overflow
import org.lanternpowered.porygen.editor.web.css.Width
import org.lanternpowered.porygen.graph.node.NodeGraph
import org.lanternpowered.porygen.graph.node.port.Port
import org.lanternpowered.porygen.math.geom.Rectangled
import org.lanternpowered.porygen.math.vector.Vec2d

private val gridSize = 1000000.px
private val halfGridSize = gridSize / 2
private val startPosition = Pair(-halfGridSize, -halfGridSize)

@Composable
fun NodeGrid(graph: NodeGraph) {
  val graphViewModel by remember { mutableStateOf(NodeGraphViewModel(graph)) }
  var scale by remember { mutableStateOf(1.0) }
  var translate by remember { mutableStateOf(startPosition) }
  var dragging by mutableStateOf(false)

  Div(
    attrs = {
      style {
        flexGrow(1)
        overflow(Overflow.Hidden)
        cursor(if (dragging || graphViewModel.nodes.any { node -> node.dragging }) Cursor.Grabbing else Cursor.Grab)
      }
      onWheel { event ->
        scale *= 1.0 + -event.deltaY * 0.0008
        event.preventDefault()
      }
      onDoubleClick {
        scale = 1.0
        translate = startPosition
      }
      onMouseDown {
        dragging = true
      }
      onMouseUp {
        dragging = false
        graphViewModel.nodes.forEach { node -> node.dragging = false }
      }
      onMouseLeave {
        dragging = false
        graphViewModel.nodes.forEach { node -> node.dragging = false }
      }
      onMouseMove { event ->
        if (dragging) {
          val (tx, ty) = translate
          translate = Pair(tx + event.movementX.px, ty + event.movementY.px)
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
              translate(tx, ty)
              scale(scale)
            }
          }
        },
        content = {
          Div(
            attrs = {
              style {
                position(Position.Relative)
                left(halfGridSize)
                top(halfGridSize)
              }
            },
            content = {
              Text("TEST")

              for (node in graphViewModel.nodes) {
                Node(node, scale)
              }
              Div(
                attrs = {
                  style {
                    position(Position.Absolute)
                    left(-halfGridSize)
                    top(-halfGridSize)
                    width(gridSize)
                    height(gridSize)
                    property("z-index", -1)
                  }
                },
                content = {
                  Svg(
                    attrs = {
                      style {
                        overflow(Overflow.Visible)
                      }
                    },
                    content = {
                      for (node in graphViewModel.nodes) {
                        for (connection in node.connections) {
                          val output = connection.output
                          val input = connection.input

                          Line(
                            x1 = halfGridSize + input.position.x.px,
                            y1 = halfGridSize + input.position.y.px,
                            x2 = halfGridSize + output.position.x.px,
                            y2 = halfGridSize + output.position.y.px,
                            attrs = {
                              attr("stroke", Color.red.toString())
                              attr("stroke-width", 5.px.toString())
                            }
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

private fun NodeViewModel.move(event: SyntheticMouseEvent, scale: Double) {
  if (dragging) {
    val movement = Vec2d(event.movementX.toDouble(), event.movementY.toDouble()) / scale
    updatePosition(position + movement)
    updateConnectionPositions()
    save()
  }
}

private fun NodeViewModel.updateConnectionPositions() {
  for (port in inputs + outputs) {
    val element = portElement(this, port.port)
    updatePortBounds(port.id, elementBounds(element))
  }
}

private fun NodeViewModel.save() {
  save(graphViewModel.graph)
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

private fun portElement(node: NodeViewModel, port: Port<*>): dynamic {
  return document.getElementById(portElementId(node, port))
}

private fun portElementId(node: NodeViewModel, port: Port<*>): String =
  "node_${node.id}_port_${port.id.value}"

@Composable
fun Node(node: NodeViewModel, scale: Double) {
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
            node.move(event, scale)
          }
        },
        content = {
          Text(node.title)
        }
      )
      Div(
        attrs = {
          style {
            display(DisplayStyle.Flex)
            cursor(if (node.dragging) Cursor.Grabbing else Cursor.Auto)
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
                  Div(
                    attrs = {
                      id(portElementId(node, input.port))
                      style {
                        padding(3.px, 5.px)
                      }
                    },
                    content = {
                      Text(input.name)
                    }
                  )
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
                    bottomRight = borderRadius - borderWidth,
                  )
                  backgroundColor(EditorColors.NodeOutputs)
                }
              },
              content = {
                for (output in node.outputs) {
                  Div(
                    attrs = {
                      id(portElementId(node, output.port))
                      style {
                        textAlign("right")
                        padding(3.px, 5.px)
                      }
                    },
                    content = {
                      Text(output.name)
                    }
                  )
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
    }
  )
}
