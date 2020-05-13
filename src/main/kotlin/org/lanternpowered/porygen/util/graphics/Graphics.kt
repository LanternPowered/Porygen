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
 * Draws the line.
 */
fun Graphics.drawLine(line: Line2i): Unit =
    drawLine(line.start.x, line.start.y, line.end.x, line.end.y)

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