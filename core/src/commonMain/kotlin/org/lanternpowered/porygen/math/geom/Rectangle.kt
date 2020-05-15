/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.math.geom

interface Rectangle<P> : Shape {

  /**
   * The minimum coordinates of this rectangle.
   */
  val min: P

  /**
   * The maximum coordinates of this rectangle.
   */
  val max: P

  /**
   * The size of this rectangle.
   */
  val size: P

  /**
   * Converts this [Rectangle] into a [Rectanglei].
   *
   * @return The int rectangle
   */
  fun toInt(): Rectanglei

  /**
   * Converts this [Rectangle] into a [Rectangled].
   *
   * @return The double rectangle
   */
  fun toDouble(): Rectangled
}
