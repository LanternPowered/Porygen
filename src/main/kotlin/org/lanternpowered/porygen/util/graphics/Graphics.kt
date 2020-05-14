/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.graphics

import org.lanternpowered.porygen.math.geom.Line2i
import org.lanternpowered.porygen.math.geom.Rectanglei
import org.spongepowered.math.vector.Vector2i
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Draws a line.
 */
fun Graphics.draw(line: Line2i): Unit =
    drawLine(line.start, line.end)

/**
 * Draws a line.
 */
fun Graphics.drawLine(min: Vector2i, max: Vector2i): Unit =
    drawLine(min.x, min.y, max.x, max.y)

/**
 * Draws a rectangle.
 */
fun Graphics.draw(rectangle: Rectanglei): Unit =
    drawRect(rectangle.min, rectangle.max)

/**
 * Draws a rectangle.
 */
fun Graphics.fill(rectangle: Rectanglei): Unit =
    drawRect(rectangle.min, rectangle.max)

/**
 * Draws a rectangle.
 */
fun Graphics.drawRect(min: Vector2i, max: Vector2i): Unit =
    drawRect(min.x, min.y, max.x, max.y)

/**
 * Draws a rectangle.
 */
fun Graphics.fillRect(min: Vector2i, max: Vector2i): Unit =
    fillRect(min.x, min.y, max.x, max.y)

/**
 * Applies the given stroke during the given [block].
 */
fun <R> Graphics2D.with(
    stroke: Stroke = this.stroke,
    color: Color = this.color,
    block: () -> R
): R {
  val oldStroke = this.stroke
  val oldColor = this.color
  this.stroke = stroke
  this.color = color
  try {
    return block()
  } finally {
    this.stroke = oldStroke
    this.color = oldColor
  }
}

fun showGraphics(width: Int = 1000, height: Int = 1000, fn: (bounds: Vector2i, graphics: Graphics2D) -> Unit) {
  val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  val graphics = image.graphics
  fn(Vector2i(width, height), graphics as Graphics2D)

  val canvas = object : JPanel() {
    override fun paintComponent(g: Graphics) {
      super.paintComponent(g)
      g.drawImage(image, 0, 0, this)
    }
  }

  val frame = JFrame()
  frame.layout = BorderLayout()
  frame.add(canvas, BorderLayout.CENTER)
  frame.setSize(width, height)
  frame.isVisible = true

  canvas.repaint()
}
