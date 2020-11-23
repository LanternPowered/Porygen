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
package org.lanternpowered.porygen.noise.module.combiner

import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.Utils

/**
 * @property source1 The first module that can be selected
 * @property source2 The second module that can be selected
 * @property control The control module which decides which module to select
 * @property edgeFalloff The edge-falloff value
 * @property lowerBound The lower bound of the selection range
 * @property upperBound The upper bound of the selection range
 */
class Select(
  val source1: NoiseModule,
  val source2: NoiseModule,
  val control: NoiseModule,
  edgeFalloff: Double = DefaultEdgeFalloff,
  val lowerBound: Double = DefaultLowerBound,
  val upperBound: Double = DefaultUpperBound
) : NoiseModule {

  val edgeFalloff: Double

  init {
    require(lowerBound <= upperBound) { "lower must be less than upper" }
    // Make sure that the edge falloff curves do not overlap.
    val boundSize = upperBound - lowerBound
    this.edgeFalloff = if (edgeFalloff > boundSize / 2) boundSize / 2 else edgeFalloff
  }

  override fun get(x: Double, y: Double, z: Double): Double {
    val controlValue = control[x, y, z]
    return if (edgeFalloff > 0.0) {
      when {
        controlValue < lowerBound - edgeFalloff -> {
          // The output value from the control module is below the selector
          // threshold; return the output value from the first source module.
          source1[x, y, z]
        }
        controlValue < lowerBound + edgeFalloff -> {
          // The output value from the control module is near the lower end of the
          // selector threshold and within the smooth curve. Interpolate between
          // the output values from the first and second source modules.
          val lowerCurve = lowerBound - edgeFalloff
          val upperCurve = lowerBound + edgeFalloff
          val alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve))
          Utils.linearInterp(source1[x, y, z], source2[x, y, z], alpha)
        }
        controlValue < upperBound - edgeFalloff -> {
          // The output value from the control module is within the selector
          // threshold; return the output value from the second source module.
          source2[x, y, z]
        }
        controlValue < upperBound + edgeFalloff -> {
          // The output value from the control module is near the upper end of the
          // selector threshold and within the smooth curve. Interpolate between
          // the output values from the first and second source modules.
          val lowerCurve = upperBound - edgeFalloff
          val upperCurve = upperBound + edgeFalloff
          val alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve))
          Utils.linearInterp(source2[x, y, z], source1[x, y, z], alpha)
        }
        else -> {
          // Output value from the control module is above the selector threshold;
          // return the output value from the first source module.
          source1[x, y, z]
        }
      }
    } else {
      if (controlValue < lowerBound || controlValue > upperBound) {
        source1[x, y, z]
      } else {
        source2[x, y, z]
      }
    }
  }

  companion object {

    /**
     * The default edge-falloff value.
     */
    const val DefaultEdgeFalloff = 0.0

    /**
     * The default lower bound of the selection range.
     */
    const val DefaultLowerBound = -1.0

    /**
     * The default upper bound of the selection range.
     */
    const val DefaultUpperBound = 1.0
  }
}
