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
package org.lanternpowered.porygen.noise

import kotlinx.serialization.Serializable
import org.lanternpowered.porygen.noise.Utils.LatticePointBCC

@Serializable
enum class SimplexNoiseQuality(
  val kernelSquaredRadius: Double,
  val randomVectors: DoubleArray,
  val lookup: Array<LatticePointBCC>
) {

  /**
   * Generates simplex-style noise using the four nearest lattice vertices and smaller kernels. The
   * appearance might be more bubbly, and there might be more straight line segments in the ridged
   * noise. However, Ridged noise using this setting may still be more favorable than the
   * Perlin / non-Simplex Ridged noise.
   */
  Standard(
    kernelSquaredRadius = 0.5,
    randomVectors = Utils.RANDOM_VECTORS_SIMPLEXSTYLE_STANDARD,
    lookup = Utils.LOOKUP_SIMPLEXSTYLE_STANDARD
  ),

  /**
   * Generates simplex-style using the eight nearest lattice vertices and larger kernels. The
   * appearance will be smoother, and there will be fewer to no straight line segments in the ridged
   * noise.
   */
  Smooth(
    kernelSquaredRadius = 0.75,
    randomVectors = Utils.RANDOM_VECTORS_SIMPLEXSTYLE_SMOOTH,
    lookup = Utils.LOOKUP_SIMPLEXSTYLE_SMOOTH
  )
}
