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
import org.lanternpowered.porygen.noise.NoiseQuality
import org.lanternpowered.porygen.noise.Utils

/**
 * A perlin noise generator.
 *
 * @property frequency The frequency of the first octave
 * @property lacunarity The frequency multiplier between successive octaves
 * @property quality The quality
 * @property octaves The total number of octaves
 * @property persistence The persistence
 * @property seed The seed
 */
class Perlin(
  val frequency: Double = DefaultFrequency,
  val lacunarity: Double = DefaultLacunarity,
  val quality: NoiseQuality = DefaultQuality,
  val octaves: Int = DefaultOctaves,
  val persistence: Double = DefaultPersistence,
  val seed: Int = DefaultSeed
) : NoiseModule {

  init {
    check(octaves in 1..MaxOctaves) {
      "octaves must be between 1 and $MaxOctaves (inclusive)" }
  }

  override fun get(x: Double, y: Double, z: Double): Double {
    var x1 = x
    var y1 = y
    var z1 = z

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
      val signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, quality)
      value += signal * curPersistence

      // Prepare the next octave.
      x1 *= lacunarity
      y1 *= lacunarity
      z1 *= lacunarity
      curPersistence *= persistence
    }
    return value
  }

  companion object {

    /**
     * The maximum number of octaves.
     */
    const val MaxOctaves = 30

    /**
     * The default frequency.
     */
    const val DefaultFrequency = 1.0

    /**
     * The default lacunarity.
     */
    const val DefaultLacunarity = 2.0

    /**
     * The default number of octaves.
     */
    const val DefaultOctaves = 6

    /**
     * The default persistence.
     */
    const val DefaultPersistence = 0.5

    /**
     * The default seed.
     */
    const val DefaultSeed = 0

    /**
     * The default noise quality.
     */
    val DefaultQuality = NoiseQuality.Standard
  }
}
