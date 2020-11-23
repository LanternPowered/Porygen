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
import org.lanternpowered.porygen.noise.module.source.Perlin

class Turbulence(
  val source: NoiseModule,
  val power: Double = DefaultPower,
  val frequency: Double = DefaultFrequency,
  val roughness: Int = DefaultRoughness,
  val seed: Int = DefaultSeed
) : NoiseModule {

  private var xDistortModule = Perlin(octaves = roughness, frequency = frequency, seed = seed)
  private var yDistortModule = Perlin(octaves = roughness, frequency = frequency, seed = seed + 1)
  private var zDistortModule = Perlin(octaves = roughness, frequency = frequency, seed = seed + 2)

  override fun get(x: Double, y: Double, z: Double): Double {
    // Get the values from the three perlin noise modules and add each value to
    // each coordinate of the input value. There are also some offsets added to
    // the coordinates of the input values. This prevents  the distortion modules
    // from returning zero if the (x, y, z) coordinates, when multiplied by the
    // frequency, are near an integer boundary. This is due to a property of
    // gradient coherent noise, which returns zero at integer boundaries.
    val x0 = x + 12414.0 / 65536.0
    val y0 = y + 65124.0 / 65536.0
    val z0 = z + 31337.0 / 65536.0
    val x1 = x + 26519.0 / 65536.0
    val y1 = y + 18128.0 / 65536.0
    val z1 = z + 60493.0 / 65536.0
    val x2 = x + 53820.0 / 65536.0
    val y2 = y + 11213.0 / 65536.0
    val z2 = z + 44845.0 / 65536.0
    val xDistort = x + xDistortModule[x0, y0, z0] * power
    val yDistort = y + yDistortModule[x1, y1, z1] * power
    val zDistort = z + zDistortModule[x2, y2, z2] * power

    // Retrieve the output value at the offset input value instead of the
    // original input value.
    return source[xDistort, yDistort, zDistort]
  }

  companion object {

    /**
     * The default turbulence power.
     */
    const val DefaultPower = 1.0

    /**
     * The default seed.
     */
    const val DefaultSeed = 0

    /**
     * The default frequency.
     */
    const val DefaultFrequency = Perlin.DefaultFrequency

    /**
     * The default roughness.
     */
    const val DefaultRoughness = Perlin.DefaultOctaves
  }
}
