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

import org.lanternpowered.porygen.noise.Utils.LatticePointBCC
import org.lanternpowered.porygen.noise.Utils.linearInterp
import org.lanternpowered.porygen.noise.Utils.sCurve3
import org.lanternpowered.porygen.noise.Utils.sCurve5

object Noise {

  private const val X_NOISE_GEN = 1619
  private const val Y_NOISE_GEN = 31337
  private const val Z_NOISE_GEN = 6971
  private const val SEED_NOISE_GEN = 1013
  private const val SHIFT_NOISE_GEN = 8

  /**
   * Generates a simplex-style gradient coherent noise value from the coordinates of a three-dimensional input value.
   *
   * Does not use the classic Simplex noise algorithm, but an alternative. Adapted from the following URLs:
   * https://github.com/KdotJPG/New-Simplex-Style-Gradient-Noise/blob/master/java/FastSimplexStyleNoise.java
   * https://github.com/KdotJPG/New-Simplex-Style-Gradient-Noise/blob/master/java/SuperSimplexNoise.java
   *
   * The return value ranges from 0 to 1.
   *
   * @param x The x coordinate of the input value.
   * @param y The y coordinate of the input value.
   * @param z The z coordinate of the input value.
   * @param seed The random number seed.
   * @param orientation The lattice orientation of the simplex-style coherent noise. See documentation for [LatticeOrientation].
   * @param quality The quality of the simplex-style coherent noise.
   * @return The generated gradient-coherent-noise value.
   */
  fun simplexStyleGradientCoherentNoise3D(
      x: Double, y: Double, z: Double, seed: Int, orientation: LatticeOrientation, quality: SimplexNoiseQuality
  ): Double {
    val squaredRadius = quality.kernelSquaredRadius
    val randomVectors = quality.randomVectors
    val lookup = quality.lookup

    // Re-orient the cubic lattices via rotation. These are orthonormal rotations, not skew transforms.
    val xr: Double
    val yr: Double
    val zr: Double
    when {
      orientation === LatticeOrientation.Classic -> {
        val r = 2.0 / 3.0 * (x + y + z)
        xr = r - x
        yr = r - y
        zr = r - z
      }
      orientation === LatticeOrientation.XYBeforeZ -> {
        val xy = x + y
        val s2 = xy * -0.211324865405187
        val zz = z * 0.577350269189626
        xr = x + s2 - zz
        yr = y + s2 - zz
        zr = xy * 0.577350269189626 + zz
      }
      else -> { // XZ_BEFORE_Y
        val xz = x + z
        val s2 = xz * -0.211324865405187
        val yy = y * 0.577350269189626
        xr = x + s2 - yy
        zr = z + s2 - yy
        yr = xz * 0.577350269189626 + yy
      }
    }

    // Get base and offsets inside cube of first lattice.
    val xrb = if (xr > 0.0) xr.toInt() else xr.toInt() - 1
    val yrb = if (yr > 0.0) yr.toInt() else yr.toInt() - 1
    val zrb = if (zr > 0.0) zr.toInt() else zr.toInt() - 1
    val xri = xr - xrb
    val yri = yr - yrb
    val zri = zr - zrb

    // Identify which octant of the cube we're in. This determines which cell
    // in the other cubic lattice we're in, and also narrows down one point on each.
    val xht = (xri + 0.5).toInt()
    val yht = (yri + 0.5).toInt()
    val zht = (zri + 0.5).toInt()
    val index = xht shl 0 or (yht shl 1) or (zht shl 2)

    // Point contributions
    var value = 0.5
    var c: LatticePointBCC? = lookup[index]
    do {
      val dxr = xri + c!!.dxr
      val dyr = yri + c.dyr
      val dzr = zri + c.dzr
      var attn = squaredRadius - dxr * dxr - dyr * dyr - dzr * dzr
      if (attn < 0) {
        c = c.nextOnFailure
      } else {
        val ix = xrb + c.xrv
        val iy = yrb + c.yrv
        val iz = zrb + c.zrv
        var vectorIndex = X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed
        vectorIndex = vectorIndex xor (vectorIndex shr SHIFT_NOISE_GEN)
        vectorIndex = vectorIndex and 0xff
        val xvGradient = randomVectors[vectorIndex shl 2]
        val yvGradient = randomVectors[(vectorIndex shl 2) + 1]
        val zvGradient = randomVectors[(vectorIndex shl 2) + 2]
        val ramped = xvGradient * dxr + yvGradient * dyr + zvGradient * dzr
        attn *= attn
        value += attn * attn * ramped
        c = c.nextOnSuccess
      }
    } while (c != null)
    return value
  }

  /**
   * Generates a gradient-coherent-noise value from the coordinates of a three-dimensional input value.
   *
   * The return value ranges from 0 to 1.
   *
   * For an explanation of the difference between *gradient* noise and *value* noise, see the
   * comments for the GradientNoise3D() function.
   *
   * @param x The x coordinate of the input value.
   * @param y The y coordinate of the input value.
   * @param z The z coordinate of the input value.
   * @param seed The random number seed.
   * @param quality The quality of the coherent-noise.
   * @return The generated gradient-coherent-noise value.
   */
  fun gradientCoherentNoise3D(x: Double, y: Double, z: Double, seed: Int, quality: NoiseQuality): Double {
    // Create a unit-length cube aligned along an integer boundary.  This cube
    // surrounds the input point.
    val x0 = if (x > 0.0) x.toInt() else x.toInt() - 1
    val x1 = x0 + 1
    val y0 = if (y > 0.0) y.toInt() else y.toInt() - 1
    val y1 = y0 + 1
    val z0 = if (z > 0.0) z.toInt() else z.toInt() - 1
    val z1 = z0 + 1

    // Map the difference between the coordinates of the input value and the
    // coordinates of the cube's outer-lower-left vertex onto an S-curve.
    val xs: Double
    val ys: Double
    val zs: Double
    when (quality) {
      NoiseQuality.Fast -> {
        xs = x - x0.toDouble()
        ys = y - y0.toDouble()
        zs = z - z0.toDouble()
      }
      NoiseQuality.Standard -> {
        xs = sCurve3(x - x0.toDouble())
        ys = sCurve3(y - y0.toDouble())
        zs = sCurve3(z - z0.toDouble())
      }
      else -> {
        xs = sCurve5(x - x0.toDouble())
        ys = sCurve5(y - y0.toDouble())
        zs = sCurve5(z - z0.toDouble())
      }
    }

    // Now calculate the noise values at each vertex of the cube.  To generate
    // the coherent-noise value at the input point, interpolate these eight
    // noise values using the S-curve value as the interpolant (trilinear
    // interpolation.)
    var n0 = gradientNoise3D(x, y, z, x0, y0, z0, seed)
    var n1 = gradientNoise3D(x, y, z, x1, y0, z0, seed)
    var ix0 = linearInterp(n0, n1, xs)
    n0 = gradientNoise3D(x, y, z, x0, y1, z0, seed)
    n1 = gradientNoise3D(x, y, z, x1, y1, z0, seed)
    var ix1 = linearInterp(n0, n1, xs)
    val iy0 = linearInterp(ix0, ix1, ys)
    n0 = gradientNoise3D(x, y, z, x0, y0, z1, seed)
    n1 = gradientNoise3D(x, y, z, x1, y0, z1, seed)
    ix0 = linearInterp(n0, n1, xs)
    n0 = gradientNoise3D(x, y, z, x0, y1, z1, seed)
    n1 = gradientNoise3D(x, y, z, x1, y1, z1, seed)
    ix1 = linearInterp(n0, n1, xs)
    val iy1 = linearInterp(ix0, ix1, ys)
    return linearInterp(iy0, iy1, zs)
  }

  /**
   * Generates a gradient-noise value from the coordinates of a three-dimensional input value and the integer
   * coordinates of a nearby three-dimensional value.
   *
   * The difference between fx and ix must be less than or equal to one. The difference between [fy] and [iy]
   * must be less than or equal to one. The difference between [fz] and [iz] must be less than or equal to one.
   *
   * A *gradient*-noise function generates better-quality noise than a *value*-noise function. Most noise modules
   * use gradient noise for this reason, although it takes much longer to calculate.
   *
   * The return value ranges from 0 to 1.
   *
   * This function generates a gradient-noise value by performing the following steps:
   * - It first calculates a random normalized vector based on the nearby integer value passed to this function.
   * - It then calculates a new value by adding this vector to the nearby integer value passed to this function.
   * - It then calculates the dot product of the above-generated value and the floating-point input value passed
   * to this function.
   *
   * A noise function differs from a random-number generator because it always returns the same output value if
   * the same input value is passed to it.
   *
   * @param fx The floating-point @a x coordinate of the input value.
   * @param fy The floating-point @a y coordinate of the input value.
   * @param fz The floating-point @a z coordinate of the input value.
   * @param ix The integer @a x coordinate of a nearby value.
   * @param iy The integer @a y coordinate of a nearby value.
   * @param iz The integer @a z coordinate of a nearby value.
   * @param seed The random number seed.
   * @return The generated gradient-noise value.
   */
  fun gradientNoise3D(fx: Double, fy: Double, fz: Double, ix: Int, iy: Int, iz: Int, seed: Int): Double {
    // Randomly generate a gradient vector given the integer coordinates of the
    // input value.  This implementation generates a random number and uses it
    // as an index into a normalized-vector lookup table.
    var vectorIndex = X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed
    vectorIndex = vectorIndex xor (vectorIndex shr SHIFT_NOISE_GEN)
    vectorIndex = vectorIndex and 0xff
    val xvGradient = Utils.RANDOM_VECTORS_PERLIN[vectorIndex shl 2]
    val yvGradient = Utils.RANDOM_VECTORS_PERLIN[(vectorIndex shl 2) + 1]
    val zvGradient = Utils.RANDOM_VECTORS_PERLIN[(vectorIndex shl 2) + 2]

    // Set up us another vector equal to the distance between the two vectors
    // passed to this function.
    val xvPoint = fx - ix
    val yvPoint = fy - iy
    val zvPoint = fz - iz

    // Now compute the dot product of the gradient vector with the distance
    // vector. The resulting value is gradient noise. Apply a scaling and
    // offset value so that this noise value ranges from 0 to 1.
    return xvGradient * xvPoint + yvGradient * yvPoint + zvGradient * zvPoint + 0.5
  }

  /**
   * Generates an integer-noise value from the coordinates of a three-dimensional input value.
   *
   * The return value ranges from 0 to 2147483647.
   *
   * A noise function differs from a random-number generator because it always returns the same
   * output value if the same input value is passed to it.
   *
   * @param x The integer @a x coordinate of the input value.
   * @param y The integer @a y coordinate of the input value.
   * @param z The integer @a z coordinate of the input value.
   * @param seed A random number seed.
   * @return The generated integer-noise value.
   */
  fun intValueNoise3D(x: Int, y: Int, z: Int, seed: Int): Int {
    // All constants are primes and must remain prime in order for this noise
    // function to work correctly.
    var n = X_NOISE_GEN * x + Y_NOISE_GEN * y + Z_NOISE_GEN * z + SEED_NOISE_GEN * seed and 0x7fffffff
    n = n shr 13 xor n
    return n * (n * n * 60493 + 19990303) + 1376312589 and 0x7fffffff
  }

  /**
   * Generates a value-coherent-noise value from the coordinates of a three-dimensional input value.
   *
   * The return value ranges from 0 to 1.
   *
   * For an explanation of the difference between *gradient* noise and *value* noise, see the comments for
   * the GradientNoise3D() function.
   *
   * @param x The x coordinate of the input value.
   * @param y The y coordinate of the input value.
   * @param z The z coordinate of the input value.
   * @param seed The random number seed.
   * @param quality The quality of the coherent-noise.
   * @return The generated value-coherent-noise value.
   */
  fun valueCoherentNoise3D(x: Double, y: Double, z: Double, seed: Int, quality: NoiseQuality): Double {
    // Create a unit-length cube aligned along an integer boundary.  This cube
    // surrounds the input point.
    val x0 = if (x > 0.0) x.toInt() else x.toInt() - 1
    val x1 = x0 + 1
    val y0 = if (y > 0.0) y.toInt() else y.toInt() - 1
    val y1 = y0 + 1
    val z0 = if (z > 0.0) z.toInt() else z.toInt() - 1
    val z1 = z0 + 1

    // Map the difference between the coordinates of the input value and the
    // coordinates of the cube's outer-lower-left vertex onto an S-curve.
    val xs: Double
    val ys: Double
    val zs: Double
    when (quality) {
      NoiseQuality.Fast -> {
        xs = x - x0
        ys = y - y0
        zs = z - z0
      }
      NoiseQuality.Standard -> {
        xs = sCurve3(x - x0)
        ys = sCurve3(y - y0)
        zs = sCurve3(z - z0)
      }
      else -> {
        xs = sCurve5(x - x0)
        ys = sCurve5(y - y0)
        zs = sCurve5(z - z0)
      }
    }

    // Now calculate the noise values at each vertex of the cube.  To generate
    // the coherent-noise value at the input point, interpolate these eight
    // noise values using the S-curve value as the interpolant (trilinear
    // interpolation.)
    var n0 = valueNoise3D(x0, y0, z0, seed)
    var n1 = valueNoise3D(x1, y0, z0, seed)
    var ix0 = linearInterp(n0, n1, xs)
    n0 = valueNoise3D(x0, y1, z0, seed)
    n1 = valueNoise3D(x1, y1, z0, seed)
    var ix1 = linearInterp(n0, n1, xs)
    val iy0 = linearInterp(ix0, ix1, ys)
    n0 = valueNoise3D(x0, y0, z1, seed)
    n1 = valueNoise3D(x1, y0, z1, seed)
    ix0 = linearInterp(n0, n1, xs)
    n0 = valueNoise3D(x0, y1, z1, seed)
    n1 = valueNoise3D(x1, y1, z1, seed)
    ix1 = linearInterp(n0, n1, xs)
    val iy1 = linearInterp(ix0, ix1, ys)
    return linearInterp(iy0, iy1, zs)
  }

  /**
   * Generates a value-noise value from the coordinates of a three-dimensional input value.
   *
   * The return value ranges from 0 to 1.
   *
   * A noise function differs from a random-number generator because it always returns the same
   * output value if the same input value is passed to it.
   *
   * @param x The x coordinate of the input value.
   * @param y The y coordinate of the input value.
   * @param z The z coordinate of the input value.
   * @param seed A random number seed.
   * @return The generated value-noise value.
   */
  fun valueNoise3D(x: Int, y: Int, z: Int, seed: Int): Double =
      intValueNoise3D(x, y, z, seed) / 2147483647.0
}
