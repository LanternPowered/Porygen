/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("RemoveExplicitTypeArguments")

package org.lanternpowered.porygen.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.height
import org.jetbrains.compose.web.attributes.width
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.lanternpowered.porygen.editor.spec.NodeGraphEditorSpec
import org.lanternpowered.porygen.editor.spec.constant
import org.lanternpowered.porygen.graph.node.port.InputPort
import org.lanternpowered.porygen.graph.specs.noise.PerlinSpec
import org.lanternpowered.porygen.noise.module.source.Perlin
import org.lanternpowered.porygen.util.Color
import org.lanternpowered.porygen.util.Colors
import kotlin.math.min

object WebNodeGraphEditorSpec {

  val Default = NodeGraphEditorSpec {
    type(PerlinSpec) {
      preview { node ->
        Div(
          attrs = {
            style {
//              width(300.px)
//              height(300.px)
              padding(5.px)
            }
          },
          content = {
            val size = 300
            var context: dynamic by remember { mutableStateOf(null) }
            var image: dynamic by remember { mutableStateOf(null) }
            var lastPerlin: Perlin? by remember { mutableStateOf(null) }
            Canvas(
              attrs = {
                width(size)
                height(size)
              },
              content = {
                val perlin = node.requirePort(output).buildTree()
                fun render() {
                  if (lastPerlin !== perlin) { // prevent dragging from rendering too many times
                    lastPerlin = perlin
                    if (image != null) {
                      if (perlin != null) {
                        for (y in 0..<size) {
                          for (x in 0..<size) {
                            val value = perlin[x.toDouble(), y.toDouble()]
                            val component = (value.coerceIn(0.0, 1.0) * 255).toInt()
                            val color = Color(component, component, component)
                            setPixel(image, y * size + x, color)
                          }
                        }
                      } else {
                        for (y in 0..<size) {
                          for (x in 0..<size) {
                            setPixel(image, y * size + x, Colors.Transparent)
                          }
                        }
                      }
                      context.putImageData(image, 0, 0)
                    }
                  }
                }
                DisposableEffect("image") {
                  context = scopeElement.getContext("2d").asDynamic()
                  image = context.createImageData(size, size)
                  lastPerlin = null
                  render()
                  onDispose {
                    image = null
                  }
                }
                render()
              }
            )
          }
        )
      }
    }
    constant<Double> { port ->
      ConstantTextInput<Double>(
        port = port,
        toString = Double?::toString,
        toValueOrNull = String::toDoubleOrNull
      )
    }
    constant<Float> { port ->
      ConstantTextInput<Float>(
        port = port,
        toString = Float?::toString,
        toValueOrNull = String::toFloatOrNull
      )
    }
    constant<Int> { port ->
      ConstantTextInput<Int>(
        port = port,
        toString = Int?::toString,
        toValueOrNull = String::toIntOrNull
      )
    }
    constant<Long> { port ->
      ConstantTextInput<Long>(
        port = port,
        toString = Long?::toString,
        toValueOrNull = String::toLongOrNull
      )
    }
  }
}

private fun setPixel(image: dynamic, index: Int, color: Color) {
  val i = index * 4
  image.data[i + 0] = color.red
  image.data[i + 1] = color.green
  image.data[i + 2] = color.blue
  image.data[i + 3] = color.alpha
}

@Composable
private fun <T> ConstantTextInput(
  port: InputPort<T>,
  toString: (T?) -> String,
  toValueOrNull: (String) -> T?,
) {
  val default = port.value ?: port.default
  var before = toString(default)
  Input(
    type = InputType.Text,
    attrs = {
      defaultValue(toString(default))
      onChange { event ->
        val element = event.target
        port.value = toValueOrNull(event.value) ?: default
        element.value = toString(port.value)
      }
      onInput { event ->
        val element = event.target
        val parsed = toValueOrNull(event.value)
        if (event.value.isNotEmpty() && parsed == null) {
          val diffRange = diffRange(before, event.value)
          element.value = before
          element.setSelectionRange(diffRange.first, diffRange.last)
        } else {
          before = element.value
          port.value = parsed
        }
      }
    }
  )
}

/**
 * Returns a range where a characters where inserted into/removed from the original string.
 */
private fun diffRange(original: String, updated: String): IntRange {
  var start = -1
  var end = -1
  for (index in 0..<min(original.length, updated.length)) {
    if (original[index] != updated[index]) {
      start = index
      end = start
      break
    }
  }
  for (index in 0..<min(original.length, updated.length)) {
    val originalIndex = original.length - index - 1
    val updatedIndex = updated.length - index - 1
    if (original[originalIndex] != updated[updatedIndex]) {
      end = originalIndex + 1
      if (start == -1)
        start = end
      break
    }
  }
  return start..end
}

