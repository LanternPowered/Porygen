/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName", "NOTHING_TO_INLINE", "DEPRECATION")

package org.lanternpowered.porygen.util

import kotlin.jvm.JvmInline
import kotlin.math.roundToInt

/**
 * Represents a color.
 */
@JvmInline
value class Color(val rgba: Int) {

  /**
   * Constructs a new color from the rgb values.
   */
  constructor(red: Byte, green: Byte, blue: Byte) :
    this(red.toInt(), green.toInt(), blue.toInt())

  /**
   * Constructs a new color from the rgba values.
   */
  constructor(red: Byte, green: Byte, blue: Byte, alpha: Byte) :
    this(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())

  /**
   * Constructs a new color from the rgb values. Each component with range 0 - 255.
   */
  constructor(red: Int, green: Int, blue: Int) :
    this(red, green, blue, 255)

  /**
   * Constructs a new color from the rgba values. Each component with range 0 - 255.
   */
  constructor(red: Int, green: Int, blue: Int, alpha: Int) :
    this((alpha shl 24) or (red shl 16) or (green shl 8) or blue)

  /**
   * The rgb value.
   */
  val rgb: Int
    get() = this.rgba and 0x00ffffff

  /**
   * The alpha component. 0 - 255
   */
  val alpha: Int
    get() = (this.rgba ushr 24) and 0xff

  /**
   * The red component. 0 - 255
   */
  val red: Int
    get() = (this.rgba ushr 16) and 0xff

  /**
   * The green component. 0 - 255
   */
  val green: Int
    get() = (this.rgba ushr 8) and 0xff

  /**
   * The blue component. 0 - 255
   */
  val blue: Int
    get() = this.rgba and 0xff

  /**
   * Darkens the color with the given factor (0.0..1.0).
   */
  fun darken(factor: Double = 0.1): Color {
    val f = 1 - factor.coerceIn(0.0, 1.0)
    return Color(
      red = (red.toFloat() * f).roundToInt().coerceIn(0, 255),
      green = (green.toFloat() * f).roundToInt().coerceIn(0, 255),
      blue = (blue.toFloat() * f).roundToInt().coerceIn(0, 255),
      alpha
    )
  }

  fun alpha(value: Double): Color =
    Color(red, green, blue, (value.coerceIn(0.0..1.0) * 255).toInt())

  override fun toString(): String {
    val alpha = alpha
    val alphaText = if (alpha > 0) ", alpha=$alpha" else ""
    return "Color(red=$red, green=$green, blue=$blue$alphaText)"
  }
}

fun Collection<Color>.mix(): Color {
  var red = 0
  var green = 0
  var blue = 0
  var alpha = 0
  for (color in this) {
    red += color.red
    green += color.green
    blue += color.blue
    alpha += color.alpha
  }
  return Color(
    red = red / size,
    green = green / size,
    blue = blue / size,
    alpha = alpha / size
  )
}

object Colors {

  val Red = Color(255, 0, 0)

  val Lime = Color(0, 255, 0)

  val Green = Color(0, 128, 0)

  val Blue = Color(0, 0, 255)

  val Black = Color(0, 0, 0)

  val Gray = Color(128, 128, 128)

  val Orange = Color(255, 165, 0)

  val Yellow = Color(255, 255, 0)

  val Gold = Color(255, 215, 0)

  val Magenta = Color(255, 0, 255)

  val Purple = Color(128, 0, 128)

  val Cyan = Color(0, 255, 255)

  val White = Color(255, 255, 255)

  val LightGray = Color(211, 211, 211)
}
