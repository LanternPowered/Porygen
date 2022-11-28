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

import org.lanternpowered.porygen.noise.LatticeOrientation
import org.lanternpowered.porygen.noise.Noise
import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.SimplexNoiseQuality
import org.lanternpowered.porygen.noise.Utils
import kotlin.math.abs
import kotlin.math.pow

/**
 * Generates ridged Simplex-style noise. The base Simplex uses a different formula
 * but produces a similar appearance to classic Simplex. Default lattice orientation
 * is XZ_BEFORE_Y. See [LatticeOrientation] for recommended usage.
 *
 * @property frequency The frequency of the first octave
 * @property lacunarity The frequency multiplier between successive octaves
 * @property quality The quality
 * @property latticeOrientation The lattice orientation
 * @property octaves The total number of octaves
 * @property seed The seed
 */
class SimplexRidgedMulti(
  val frequency: Double = DefaultFrequency,
  var lacunarity: Double = DefaultLacunarity,
  val quality: SimplexNoiseQuality = DefaultQuality,
  val latticeOrientation: LatticeOrientation = DefaultLatticeOrientation,
  val octaves: Int = DefaultOctaves,
  val seed: Int = DefaultSeed
) : NoiseModule {

  private val spectralWeights: DoubleArray

  init {
    check(octaves in 1..MaxOctaves) {
      "octaves must be between 1 and $MaxOctaves (inclusive)"
    }

    // Calculate the spectral weights

    // This exponent parameter should be user-defined; it may be exposed in a
    // future version of libnoise
    val h = 1.0

    var frequency = 1.0
    spectralWeights = DoubleArray(octaves)
    for (i in 0 until octaves) {
      spectralWeights[i] = frequency.pow(-h)
      frequency *= lacunarity
    }
  }

  override fun get(x: Double, y: Double, z: Double): Double {
    var x1 = x
    var y1 = y
    var z1 = z

    x1 *= frequency
    y1 *= frequency
    z1 *= frequency

    var value = 0.0
    var weight = 1.0

    // These parameters should be user-defined; they may be exposed in a
    // future version of libnoise.
    val offset = 1.0
    val gain = 2.0

    for (curOctave in 0 until octaves) {

      // Make sure that these floating-point values have the same range as a 32-
      // bit integer so that we can pass them to the coherent-noise functions.
      val nx = Utils.makeIntRange(x1)
      val ny = Utils.makeIntRange(y1)
      val nz = Utils.makeIntRange(z1)

      // Get the coherent-noise value.
      val seed = this.seed + curOctave and 0x7fffffff
      var signal = Noise.simplexStyleGradientCoherentNoise3D(nx, ny, nz, seed, latticeOrientation, quality) * 2 - 1

      // Make the ridges.
      signal = abs(signal)
      signal = offset - signal

      // Square the signal to increase the sharpness of the ridges.
      signal *= signal

      // The weighting from the previous octave is applied to the signal.
      // Larger values have higher weights, producing sharp points along the
      // ridges.
      signal *= weight

      // Weight successive contributions by the previous signal.
      weight = signal * gain
      if (weight > 1.0) {
        weight = 1.0
      }
      if (weight < 0.0) {
        weight = 0.0
      }

      // Add the signal to the output value.
      value += signal * spectralWeights[curOctave]

      // Go to the next octave.
      x1 *= lacunarity
      y1 *= lacunarity
      z1 *= lacunarity
    }
    return value / 1.6
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
     * The default seed.
     */
    const val DefaultSeed = 0

    /**
     * The default noise quality.
     */
    val DefaultQuality = SimplexNoiseQuality.Smooth

    /**
     * The default lattice orientation.
     */
    val DefaultLatticeOrientation = LatticeOrientation.XZBeforeY
  }
}
