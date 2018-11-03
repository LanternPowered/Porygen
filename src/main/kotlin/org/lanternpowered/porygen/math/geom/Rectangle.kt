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
   * Gets the minimum coordinates of this rectangle.
   *
   * @return The minimum
   */
  val min: P

  /**
   * Gets the maximum coordinates of this rectangle.
   *
   * @return The maximum
   */
  val max: P

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
