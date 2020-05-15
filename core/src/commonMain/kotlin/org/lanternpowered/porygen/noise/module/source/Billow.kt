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

import org.lanternpowered.porygen.noise.Noise
import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.Utils
import org.lanternpowered.porygen.noise.NoiseQuality
import kotlin.math.abs

/**
 * A billow noise generator.
 *
 * @property frequency The frequency of the first octave
 * @property lacunarity The frequency multiplier between successive octaves
 * @property quality The quality
 * @property octaves The total number of octaves
 * @property persistence The persistence
 */
class Billow(
    val frequency: Double = DEFAULT_FREQUENCY,
    val lacunarity: Double = DEFAULT_LACUNARITY,
    val quality: NoiseQuality = DEFAULT_QUALITY,
    val octaves: Int = DEFAULT_OCTAVES,
    val persistence: Double = DEFAULT_PERSISTENCE,
    val seed: Int = DEFAULT_SEED
) : NoiseModule {

  init {
    check(octaves in 1..Perlin.MAX_OCTAVES) {
      "octaves must be between 1 and $MAX_OCTAVES (inclusive)" }
  }

  override fun get(x: Double, y: Double, z: Double): Double {
    var z1 = z
    var y1 = y
    var x1 = x

    x1 *= frequency
    y1 *= frequency
    z1 *= frequency

    var value = 0.0
    var curPersistence = 1.0

    for (curOctave in 0 until octaves) {
      // Make sure that these floating-point values have the same range as a 32-
      // bit integer so that we can pass them to the coherent-noise functions.
      val nx = Utils.makeIntRange(x1)
      val ny = Utils.makeIntRange(y1)
      val nz = Utils.makeIntRange(z1)

      // Get the coherent-noise value from the input value and add it to the
      // final result.
      val seed = seed + curOctave
      var signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, quality) * 2 - 1
      signal = abs(signal)
      value += signal * curPersistence

      // Prepare the next octave.
      x1 *= lacunarity
      y1 *= lacunarity
      z1 *= lacunarity
      curPersistence *= persistence
    }

    value += 0.25
    return value
  }

  companion object {

    /**
     * The maximum number of octaves.
     */
    const val MAX_OCTAVES = 30

    /**
     * The default frequency.
     */
    const val DEFAULT_FREQUENCY = 1.0

    /**
     * The default lacunarity.
     */
    const val DEFAULT_LACUNARITY = 2.0

    /**
     * The default number of octaves.
     */
    const val DEFAULT_OCTAVES = 6

    /**
     * The default persistence.
     */
    const val DEFAULT_PERSISTENCE = 0.5

    /**
     * The default seed.
     */
    const val DEFAULT_SEED = 0

    /**
     * The default noise quality.
     */
    val DEFAULT_QUALITY = NoiseQuality.STANDARD
  }
}
