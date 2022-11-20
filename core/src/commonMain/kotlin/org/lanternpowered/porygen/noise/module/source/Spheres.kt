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
package org.lanternpowered.porygen.noise.module.source

import org.lanternpowered.porygen.math.floorToInt
import org.lanternpowered.porygen.noise.NoiseModule
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A sphere generator.
 *
 * @property frequency The frequency of the concentric spheres
 */
class Spheres(
  val frequency: Double = DefaultFrequency,
) : NoiseModule {

  override fun get(x: Double, y: Double, z: Double): Double {
    var x1 = x
    var y1 = y
    var z1 = z
    x1 *= frequency
    y1 *= frequency
    z1 *= frequency
    val distFromCenter = sqrt(x1 * x1 + y1 * y1 + z1 * z1)
    val distFromSmallerSphere = distFromCenter - floorToInt(distFromCenter)
    val distFromLargerSphere = 1.0 - distFromSmallerSphere
    val nearestDist = min(distFromSmallerSphere, distFromLargerSphere)
    return 1.0 - nearestDist * 2.0 // Puts it in the 0 to 1 range.
  }

  companion object {

    /**
     * The default frequency.
     */
    const val DefaultFrequency = 1.0
  }
}
