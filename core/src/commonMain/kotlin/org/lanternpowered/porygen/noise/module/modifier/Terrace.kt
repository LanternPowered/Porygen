/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
/*
 * Copyright (c) Flow Powered <https://github.com/FlowPowered>
 * Copyright (c) SpongePowered <https://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Noise is re-licensed with permission from jlibnoise author.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen.noise.module.modifier

import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.Utils

/**
 * A terrace module modifier.
 *
 * @property source The input module that will be modified
 * @property controlPoints The control points
 * @property invertTerraces Determines if the terrace-forming curve
 *   between all control points is inverted.
 */
class Terrace(
    val source: NoiseModule,
    controlPoints: List<Double>,
    val invertTerraces: Boolean = false
) : NoiseModule {

  /**
   * The control points.
   */
  val controlPoints: List<Double> = controlPoints.toList()

  private val controlPointsArray = controlPoints.toDoubleArray()

  /**
   * Constructs the [Terrace] module.
   *
   * @param source The input module that will be modified
   * @param controlPointsCount The number of control points
   * @param invertTerraces Determines if the terrace-forming curve
   *   between all control points is inverted.
   */
  constructor(source: NoiseModule, controlPointsCount: Int, invertTerraces: Boolean = false) :
      this(source, getDefaultControlPoints(controlPointsCount), invertTerraces)

  init {
    check(controlPoints.toSet().size == controlPoints.size) {
      "Every control point value must be unique" }
  }

  override fun get(x: Double, y: Double, z: Double): Double {
    // Get the output value from the source module.
    val sourceModuleValue = source[x, y, z]

    // Find the first element in the control point array that has a value
    // larger than the output value from the source module.
    var indexPos = 0
    while (indexPos < controlPointsArray.size) {
      if (sourceModuleValue < controlPointsArray[indexPos])
        break
      indexPos++
    }

    // Find the two nearest control points so that we can map their values
    // onto a quadratic curve.
    val index0 = (indexPos - 1).coerceIn(0, controlPointsArray.size - 1)
    val index1 = indexPos.coerceIn(0, controlPointsArray.size - 1)

    // If some control points are missing (which occurs if the output value from
    // the source module is greater than the largest value or less than the
    // smallest value of the control point array), get the value of the nearest
    // control point and exit now.
    if (index0 == index1)
      return controlPointsArray[index1]

    // Compute the alpha value used for linear interpolation.
    var value0 = controlPointsArray[index0]
    var value1 = controlPointsArray[index1]
    var alpha = (sourceModuleValue - value0) / (value1 - value0)
    if (invertTerraces) {
      alpha = 1.0 - alpha
      val temp = value0
      value0 = value1
      value1 = temp
    }

    // Squaring the alpha produces the terrace effect.
    alpha *= alpha

    // Now perform the linear interpolation given the alpha value.
    return Utils.linearInterp(value0, value1, alpha)
  }

  companion object {

    private fun getDefaultControlPoints(count: Int): List<Double> {
      check(count >= 2) { "Must have more than 2 control points" }
      val controlPoints = mutableListOf<Double>()
      val terraceStep = 2.0 / (count - 1.0)
      var curValue = -1.0
      for (i in 0 until count) {
        controlPoints += curValue
        curValue += terraceStep
      }
      return controlPoints
    }
  }
}
