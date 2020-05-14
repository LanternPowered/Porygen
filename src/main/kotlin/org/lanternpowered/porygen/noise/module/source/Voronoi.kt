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
import org.lanternpowered.porygen.noise.Noise
import org.lanternpowered.porygen.noise.NoiseModule
import kotlin.math.sqrt

/**
 * @property displacement The scale of the random displacement to apply for each voronoi cell
 * @property useSeedPointDistance Determines if the distance from the nearest seed point is
 *   applied to the output value
 * @property frequency The frequency of the seed points
 * @property seed The seed value used by the coherent-noise function to determine the positions
 *   of the seed points.
 */
class Voronoi(
    val displacement: Double = DEFAULT_DISPLACEMENT,
    var frequency: Double = DEFAULT_FREQUENCY,
    var seed: Int = DEFAULT_SEED,
    var useSeedPointDistance: Boolean = false
) : NoiseModule {

  override fun get(x: Double, y: Double, z: Double): Double {
    var x1 = x
    var y1 = y
    var z1 = z

    // This method could be more efficient by caching the seed values. Fix later.
    x1 *= frequency
    y1 *= frequency
    z1 *= frequency

    val xInt = if (x1 > 0.0) x1.toInt() else x1.toInt() - 1
    val yInt = if (y1 > 0.0) y1.toInt() else y1.toInt() - 1
    val zInt = if (z1 > 0.0) z1.toInt() else z1.toInt() - 1

    var minDist = 2147483647.0
    var xCandidate = 0.0
    var yCandidate = 0.0
    var zCandidate = 0.0

    // Inside each unit cube, there is a seed point at a random position.  Go
    // through each of the nearby cubes until we find a cube with a seed point
    // that is closest to the specified position.
    for (zCur in zInt - 2..zInt + 2) {
      for (yCur in yInt - 2..yInt + 2) {
        for (xCur in xInt - 2..xInt + 2) {
          // Calculate the position and distance to the seed point inside of this unit cube.
          val xPos = xCur + Noise.valueNoise3D(xCur, yCur, zCur, seed)
          val yPos = yCur + Noise.valueNoise3D(xCur, yCur, zCur, seed + 1)
          val zPos = zCur + Noise.valueNoise3D(xCur, yCur, zCur, seed + 2)

          val xDist = xPos - x1
          val yDist = yPos - y1
          val zDist = zPos - z1

          val dist = (xDist * xDist) + (yDist * yDist) + (zDist * zDist)
          if (dist < minDist) {
            // This seed point is closer to any others found so far, so record this seed point.
            minDist = dist
            xCandidate = xPos
            yCandidate = yPos
            zCandidate = zPos
          }
        }
      }
    }

    val value = if (useSeedPointDistance) {
      // Determine the distance to the nearest seed point.
      val xDist: Double = xCandidate - x1
      val yDist: Double = yCandidate - y1
      val zDist: Double = zCandidate - z1
      sqrt((xDist * xDist) + (yDist * yDist) + (zDist * zDist)) / SQRT_3
    } else {
      0.0
    }

    // Return the calculated distance with the displacement value applied.
    return value + (displacement * Noise.valueNoise3D(
        floorToInt(xCandidate), floorToInt(yCandidate), floorToInt(zCandidate), seed))
  }

  companion object {

    private val SQRT_3 = sqrt(3.0)

    /**
     * The default displacement.
     */
    const val DEFAULT_DISPLACEMENT = 1.0

    /**
     * The default frequency.
     */
    const val DEFAULT_FREQUENCY = 1.0

    /**
     * The default seed.
     */
    const val DEFAULT_SEED = 0
  }
}
