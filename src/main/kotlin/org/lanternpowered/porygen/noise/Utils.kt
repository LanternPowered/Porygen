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

import kotlin.math.cos
import kotlin.math.sin

object Utils {

  /**
   * Performs cubic interpolation between two values bound between two other values
   *
   * @param n0 the value before the first value
   * @param n1 the first value
   * @param n2 the second value
   * @param n3 the value after the second value
   * @param a the alpha value
   * @return the interpolated value
   */
  fun cubicInterp(n0: Double, n1: Double, n2: Double, n3: Double, a: Double): Double {
    val p = n3 - n2 - (n0 - n1)
    val q = n0 - n1 - p
    val r = n2 - n0
    return p * a * a * a + q * a * a + r * a + n1
  }

  /**
   * Performs linear interpolation between two values
   *
   * @param n0 first value
   * @param n1 second value
   * @param a the alpha value. Should be between 0 and 1.
   * @return the interpolated value
   */
  fun linearInterp(n0: Double, n1: Double, a: Double): Double = (1.0 - a) * n0 + a * n1

  /**
   * Maps a value onto a cubic S-Curve
   *
   * @param a the value to map onto a S-Curve
   * @return the mapped value
   */
  fun sCurve3(a: Double): Double = a * a * (3.0 - 2.0 * a)

  /**
   * maps a value onto a quitnic S-Curve
   *
   * @param a the value to map onto a quitic S-curve
   * @return the mapped value
   */
  fun sCurve5(a: Double): Double {
    val a3 = a * a * a
    val a4 = a3 * a
    val a5 = a4 * a
    return 6.0 * a5 - 15.0 * a4 + 10.0 * a3
  }

  fun latLonToXYZ(latitude: Double, longitude: Double): DoubleArray {
    val r = cos(Math.toRadians(latitude))
    val x = r * cos(Math.toRadians(longitude))
    val y = sin(Math.toRadians(latitude))
    val z = r * sin(Math.toRadians(longitude))
    return doubleArrayOf(x, y, z)
  }

  /**
   * Modifies a floating-point value so that it can be stored in a noise::int32 variable.
   *
   * @param n A floating-point number.
   * @return The modified floating-point number.
   *
   * This function does not modify @a n.
   *
   * In libnoise, the noise-generating algorithms are all integer-based; they use variables of
   * type noise::int32. Before calling a noise function, pass the @a x, @a y, and @a z coordinates
   * to this function to ensure that these coordinates can be cast to a noise::int32 value.
   *
   * Although you could do a straight cast from double to noise::int32, the resulting value may
   * differ between platforms. By using this function, you ensure that the resulting value is
   * identical between platforms.
   */
  fun makeInt32Range(n: Double): Double {
    return when {
      n >= 1073741824.0 -> 2.0 * n % 1073741824.0 - 1073741824.0
      n <= -1073741824.0 -> 2.0 * n % 1073741824.0 + 1073741824.0
      else -> n
    }
  }

  /**
   * Clamps the value between the low and high boundaries
   *
   * @param value The value to clamp
   * @param low The low bound of the clamp
   * @param high The high bound of the clamp
   * @return the clamped value
   */
  fun clamp(value: Int, low: Int, high: Int): Int {
    if (value < low)
      return low
    return if (value > high) high else value
  }

  /**
   * Rounds x down to the closest integer
   *
   * @param x The value to floor
   * @return The closest integer
   */
  fun floor(x: Double): Int {
    val y = x.toInt()
    return if (x < y) y - 1 else y
  }

  /**
   * A table of 256 random normalized vectors. Each row is an (x, y, z, 0) coordinate. The 0 is used as padding so we can use bit shifts to index any
   * row in the table. These vectors have an even statistical distribution, which improves the quality of the coherent noise generated by these
   * vectors. For more information, see "GPU Gems", Chapter 5 - Implementing Improved Perlin Noise by Ken Perlin, specifically page 76.
   */
  private val randomVectors = doubleArrayOf(
      -0.763874, -0.596439, -0.246489, 0.0, 0.396055, 0.904518, -0.158073, 0.0, -0.499004, -0.8665, -0.0131631, 0.0, 0.468724, -0.824756, 0.316346, 0.0,
      0.829598, 0.43195, 0.353816, 0.0, -0.454473, 0.629497, -0.630228, 0.0, -0.162349, -0.869962, -0.465628, 0.0, 0.932805, 0.253451, 0.256198, 0.0,
      -0.345419, 0.927299, -0.144227, 0.0, -0.715026, -0.293698, -0.634413, 0.0, -0.245997, 0.717467, -0.651711, 0.0, -0.967409, -0.250435, -0.037451, 0.0,
      0.901729, 0.397108, -0.170852, 0.0, 0.892657, -0.0720622, -0.444938, 0.0, 0.0260084, -0.0361701, 0.999007, 0.0, 0.949107, -0.19486, 0.247439, 0.0,
      0.471803, -0.807064, -0.355036, 0.0, 0.879737, 0.141845, 0.453809, 0.0, 0.570747, 0.696415, 0.435033, 0.0, -0.141751, -0.988233, -0.0574584, 0.0,
      -0.58219, -0.0303005, 0.812488, 0.0, -0.60922, 0.239482, -0.755975, 0.0, 0.299394, -0.197066, -0.933557, 0.0, -0.851615, -0.220702, -0.47544, 0.0,
      0.848886, 0.341829, -0.403169, 0.0, -0.156129, -0.687241, 0.709453, 0.0, -0.665651, 0.626724, 0.405124, 0.0, 0.595914, -0.674582, 0.43569, 0.0,
      0.171025, -0.509292, 0.843428, 0.0, 0.78605, 0.536414, -0.307222, 0.0, 0.18905, -0.791613, 0.581042, 0.0, -0.294916, 0.844994, 0.446105, 0.0,
      0.342031, -0.58736, -0.7335, 0.0, 0.57155, 0.7869, 0.232635, 0.0, 0.885026, -0.408223, 0.223791, 0.0, -0.789518, 0.571645, 0.223347, 0.0,
      0.774571, 0.31566, 0.548087, 0.0, -0.79695, -0.0433603, -0.602487, 0.0, -0.142425, -0.473249, -0.869339, 0.0, -0.0698838, 0.170442, 0.982886, 0.0,
      0.687815, -0.484748, 0.540306, 0.0, 0.543703, -0.534446, -0.647112, 0.0, 0.97186, 0.184391, -0.146588, 0.0, 0.707084, 0.485713, -0.513921, 0.0,
      0.942302, 0.331945, 0.043348, 0.0, 0.499084, 0.599922, 0.625307, 0.0, -0.289203, 0.211107, 0.9337, 0.0, 0.412433, -0.71667, -0.56239, 0.0,
      0.87721, -0.082816, 0.47291, 0.0, -0.420685, -0.214278, 0.881538, 0.0, 0.752558, -0.0391579, 0.657361, 0.0, 0.0765725, -0.996789, 0.0234082, 0.0,
      -0.544312, -0.309435, -0.779727, 0.0, -0.455358, -0.415572, 0.787368, 0.0, -0.874586, 0.483746, 0.0330131, 0.0, 0.245172, -0.0838623, 0.965846, 0.0,
      0.382293, -0.432813, 0.81641, 0.0, -0.287735, -0.905514, 0.311853, 0.0, -0.667704, 0.704955, -0.239186, 0.0, 0.717885, -0.464002, -0.518983, 0.0,
      0.976342, -0.214895, 0.0240053, 0.0, -0.0733096, -0.921136, 0.382276, 0.0, -0.986284, 0.151224, -0.0661379, 0.0, -0.899319, -0.429671, 0.0812908, 0.0,
      0.652102, -0.724625, 0.222893, 0.0, 0.203761, 0.458023, -0.865272, 0.0, -0.030396, 0.698724, -0.714745, 0.0, -0.460232, 0.839138, 0.289887, 0.0,
      -0.0898602, 0.837894, 0.538386, 0.0, -0.731595, 0.0793784, 0.677102, 0.0, -0.447236, -0.788397, 0.422386, 0.0, 0.186481, 0.645855, -0.740335, 0.0,
      -0.259006, 0.935463, 0.240467, 0.0, 0.445839, 0.819655, -0.359712, 0.0, 0.349962, 0.755022, -0.554499, 0.0, -0.997078, -0.0359577, 0.0673977, 0.0,
      -0.431163, -0.147516, -0.890133, 0.0, 0.299648, -0.63914, 0.708316, 0.0, 0.397043, 0.566526, -0.722084, 0.0, -0.502489, 0.438308, -0.745246, 0.0,
      0.0687235, 0.354097, 0.93268, 0.0, -0.0476651, -0.462597, 0.885286, 0.0, -0.221934, 0.900739, -0.373383, 0.0, -0.956107, -0.225676, 0.186893, 0.0,
      -0.187627, 0.391487, -0.900852, 0.0, -0.224209, -0.315405, 0.92209, 0.0, -0.730807, -0.537068, 0.421283, 0.0, -0.0353135, -0.816748, 0.575913, 0.0,
      -0.941391, 0.176991, -0.287153, 0.0, -0.154174, 0.390458, 0.90762, 0.0, -0.283847, 0.533842, 0.796519, 0.0, -0.482737, -0.850448, 0.209052, 0.0,
      -0.649175, 0.477748, 0.591886, 0.0, 0.885373, -0.405387, -0.227543, 0.0, -0.147261, 0.181623, -0.972279, 0.0, 0.0959236, -0.115847, -0.988624, 0.0,
      -0.89724, -0.191348, 0.397928, 0.0, 0.903553, -0.428461, -0.00350461, 0.0, 0.849072, -0.295807, -0.437693, 0.0, 0.65551, 0.741754, -0.141804, 0.0,
      0.61598, -0.178669, 0.767232, 0.0, 0.0112967, 0.932256, -0.361623, 0.0, -0.793031, 0.258012, 0.551845, 0.0, 0.421933, 0.454311, 0.784585, 0.0,
      -0.319993, 0.0401618, -0.946568, 0.0, -0.81571, 0.551307, -0.175151, 0.0, -0.377644, 0.00322313, 0.925945, 0.0, 0.129759, -0.666581, -0.734052, 0.0,
      0.601901, -0.654237, -0.457919, 0.0, -0.927463, -0.0343576, -0.372334, 0.0, -0.438663, -0.868301, -0.231578, 0.0, -0.648845, -0.749138, -0.133387, 0.0,
      0.507393, -0.588294, 0.629653, 0.0, 0.726958, 0.623665, 0.287358, 0.0, 0.411159, 0.367614, -0.834151, 0.0, 0.806333, 0.585117, -0.0864016, 0.0,
      0.263935, -0.880876, 0.392932, 0.0, 0.421546, -0.201336, 0.884174, 0.0, -0.683198, -0.569557, -0.456996, 0.0, -0.117116, -0.0406654, -0.992285, 0.0,
      -0.643679, -0.109196, -0.757465, 0.0, -0.561559, -0.62989, 0.536554, 0.0, 0.0628422, 0.104677, -0.992519, 0.0, 0.480759, -0.2867, -0.828658, 0.0,
      -0.228559, -0.228965, -0.946222, 0.0, -0.10194, -0.65706, -0.746914, 0.0, 0.0689193, -0.678236, 0.731605, 0.0, 0.401019, -0.754026, 0.52022, 0.0,
      -0.742141, 0.547083, -0.387203, 0.0, -0.00210603, -0.796417, -0.604745, 0.0, 0.296725, -0.409909, -0.862513, 0.0, -0.260932, -0.798201, 0.542945, 0.0,
      -0.641628, 0.742379, 0.192838, 0.0, -0.186009, -0.101514, 0.97729, 0.0, 0.106711, -0.962067, 0.251079, 0.0, -0.743499, 0.30988, -0.592607, 0.0,
      -0.795853, -0.605066, -0.0226607, 0.0, -0.828661, -0.419471, -0.370628, 0.0, 0.0847218, -0.489815, -0.8677, 0.0, -0.381405, 0.788019, -0.483276, 0.0,
      0.282042, -0.953394, 0.107205, 0.0, 0.530774, 0.847413, 0.0130696, 0.0, 0.0515397, 0.922524, 0.382484, 0.0, -0.631467, -0.709046, 0.313852, 0.0,
      0.688248, 0.517273, 0.508668, 0.0, 0.646689, -0.333782, -0.685845, 0.0, -0.932528, -0.247532, -0.262906, 0.0, 0.630609, 0.68757, -0.359973, 0.0,
      0.577805, -0.394189, 0.714673, 0.0, -0.887833, -0.437301, -0.14325, 0.0, 0.690982, 0.174003, 0.701617, 0.0, -0.866701, 0.0118182, 0.498689, 0.0,
      -0.482876, 0.727143, 0.487949, 0.0, -0.577567, 0.682593, -0.447752, 0.0, 0.373768, 0.0982991, 0.922299, 0.0, 0.170744, 0.964243, -0.202687, 0.0,
      0.993654, -0.035791, -0.106632, 0.0, 0.587065, 0.4143, -0.695493, 0.0, -0.396509, 0.26509, -0.878924, 0.0, -0.0866853, 0.83553, -0.542563, 0.0,
      0.923193, 0.133398, -0.360443, 0.0, 0.00379108, -0.258618, 0.965972, 0.0, 0.239144, 0.245154, -0.939526, 0.0, 0.758731, -0.555871, 0.33961, 0.0,
      0.295355, 0.309513, 0.903862, 0.0, 0.0531222, -0.91003, -0.411124, 0.0, 0.270452, 0.0229439, -0.96246, 0.0, 0.563634, 0.0324352, 0.825387, 0.0,
      0.156326, 0.147392, 0.976646, 0.0, -0.0410141, 0.981824, 0.185309, 0.0, -0.385562, -0.576343, -0.720535, 0.0, 0.388281, 0.904441, 0.176702, 0.0,
      0.945561, -0.192859, -0.262146, 0.0, 0.844504, 0.520193, 0.127325, 0.0, 0.0330893, 0.999121, -0.0257505, 0.0, -0.592616, -0.482475, -0.644999, 0.0,
      0.539471, 0.631024, -0.557476, 0.0, 0.655851, -0.027319, -0.754396, 0.0, 0.274465, 0.887659, 0.369772, 0.0, -0.123419, 0.975177, -0.183842, 0.0,
      -0.223429, 0.708045, 0.66989, 0.0, -0.908654, 0.196302, 0.368528, 0.0, -0.95759, -0.00863708, 0.288005, 0.0, 0.960535, 0.030592, 0.276472, 0.0,
      -0.413146, 0.907537, 0.0754161, 0.0, -0.847992, 0.350849, -0.397259, 0.0, 0.614736, 0.395841, 0.68221, 0.0, -0.503504, -0.666128, -0.550234, 0.0,
      -0.268833, -0.738524, -0.618314, 0.0, 0.792737, -0.60001, -0.107502, 0.0, -0.637582, 0.508144, -0.579032, 0.0, 0.750105, 0.282165, -0.598101, 0.0,
      -0.351199, -0.392294, -0.850155, 0.0, 0.250126, -0.960993, -0.118025, 0.0, -0.732341, 0.680909, -0.0063274, 0.0, -0.760674, -0.141009, 0.633634, 0.0,
      0.222823, -0.304012, 0.926243, 0.0, 0.209178, 0.505671, 0.836984, 0.0, 0.757914, -0.56629, -0.323857, 0.0, -0.782926, -0.339196, 0.52151, 0.0,
      -0.462952, 0.585565, 0.665424, 0.0, 0.61879, 0.194119, -0.761194, 0.0, 0.741388, -0.276743, 0.611357, 0.0, 0.707571, 0.702621, 0.0752872, 0.0,
      0.156562, 0.819977, 0.550569, 0.0, -0.793606, 0.440216, 0.42, 0.0, 0.234547, 0.885309, -0.401517, 0.0, 0.132598, 0.80115, -0.58359, 0.0,
      -0.377899, -0.639179, 0.669808, 0.0, -0.865993, -0.396465, 0.304748, 0.0, -0.624815, -0.44283, 0.643046, 0.0, -0.485705, 0.825614, -0.287146, 0.0,
      -0.971788, 0.175535, 0.157529, 0.0, -0.456027, 0.392629, 0.798675, 0.0, -0.0104443, 0.521623, -0.853112, 0.0, -0.660575, -0.74519, 0.091282, 0.0,
      -0.0157698, -0.307475, -0.951425, 0.0, -0.603467, -0.250192, 0.757121, 0.0, 0.506876, 0.25006, 0.824952, 0.0, 0.255404, 0.966794, 0.00884498, 0.0,
      0.466764, -0.874228, -0.133625, 0.0, 0.475077, -0.0682351, -0.877295, 0.0, -0.224967, -0.938972, -0.260233, 0.0, -0.377929, -0.814757, -0.439705, 0.0,
      -0.305847, 0.542333, -0.782517, 0.0, 0.26658, -0.902905, -0.337191, 0.0, 0.0275773, 0.322158, -0.946284, 0.0, 0.0185422, 0.716349, 0.697496, 0.0,
      -0.20483, 0.978416, 0.0273371, 0.0, -0.898276, 0.373969, 0.230752, 0.0, -0.00909378, 0.546594, 0.837349, 0.0, 0.6602, -0.751089, 0.000959236, 0.0,
      0.855301, -0.303056, 0.420259, 0.0, 0.797138, 0.0623013, -0.600574, 0.0, 0.48947, -0.866813, 0.0951509, 0.0, 0.251142, 0.674531, 0.694216, 0.0,
      -0.578422, -0.737373, -0.348867, 0.0, -0.254689, -0.514807, 0.818601, 0.0, 0.374972, 0.761612, 0.528529, 0.0, 0.640303, -0.734271, -0.225517, 0.0,
      -0.638076, 0.285527, 0.715075, 0.0, 0.772956, -0.15984, -0.613995, 0.0, 0.798217, -0.590628, 0.118356, 0.0, -0.986276, -0.0578337, -0.154644, 0.0,
      -0.312988, -0.94549, 0.0899272, 0.0, -0.497338, 0.178325, 0.849032, 0.0, -0.101136, -0.981014, 0.165477, 0.0, -0.521688, 0.0553434, -0.851339, 0.0,
      -0.786182, -0.583814, 0.202678, 0.0, -0.565191, 0.821858, -0.0714658, 0.0, 0.437895, 0.152598, -0.885981, 0.0, -0.92394, 0.353436, -0.14635, 0.0,
      0.212189, -0.815162, -0.538969, 0.0, -0.859262, 0.143405, -0.491024, 0.0, 0.991353, 0.112814, 0.0670273, 0.0, 0.0337884, -0.979891, -0.196654, 0.0
  )

  // These were computed by gradient ascent using the above gradient set.
  private const val NORMALIZER_SIMPLEXSTYLE_STANDARD = 0.0185703274687564875
  private const val NORMALIZER_SIMPLEXSTYLE_SMOOTH = 0.17315490496873540625
  private const val NORMALIZER_PERLIN = 1.7252359327388492

  val RANDOM_VECTORS_SIMPLEXSTYLE_STANDARD: DoubleArray
  val RANDOM_VECTORS_SIMPLEXSTYLE_SMOOTH: DoubleArray
  val RANDOM_VECTORS_PERLIN: DoubleArray

  /**
   * Lookup tables to 8 decision trees for the simplex-style noise. Two cubic lattices offset by (0.5, 0.5, 0.5).
   * The current octant of the current cubic cell of the first lattice corresponds to one cell on the second.
   * From there, at most four ("standard") or eight ("smooth") lattice vertices are found which are in range.
   */
  val LOOKUP_SIMPLEXSTYLE_STANDARD: Array<LatticePointBCC>
  val LOOKUP_SIMPLEXSTYLE_SMOOTH: Array<LatticePointBCC>

  /**
   * Represents one lattice vertex on the simplex-style noise.
   */
  class LatticePointBCC(xrv: Int, yrv: Int, zrv: Int, lattice: Int) {
    val dxr = -xrv + lattice * 0.5
    val dyr = -yrv + lattice * 0.5
    val dzr = -zrv + lattice * 0.5
    val xrv = xrv + lattice * 0x8000
    val yrv = yrv + lattice * 0x8000
    val zrv = zrv + lattice * 0x8000
    var nextOnFailure: LatticePointBCC? = null
    var nextOnSuccess: LatticePointBCC? = null
  }

  init {
    RANDOM_VECTORS_PERLIN = DoubleArray(randomVectors.size)
    RANDOM_VECTORS_SIMPLEXSTYLE_STANDARD = DoubleArray(randomVectors.size)
    RANDOM_VECTORS_SIMPLEXSTYLE_SMOOTH = DoubleArray(randomVectors.size)

    for (i in randomVectors.indices) {
      RANDOM_VECTORS_PERLIN[i] = randomVectors[i] * NORMALIZER_PERLIN
      RANDOM_VECTORS_SIMPLEXSTYLE_STANDARD[i] = randomVectors[i] * NORMALIZER_SIMPLEXSTYLE_STANDARD
      RANDOM_VECTORS_SIMPLEXSTYLE_SMOOTH[i] = randomVectors[i] * NORMALIZER_SIMPLEXSTYLE_SMOOTH
    }

    val simplexLookupStandard = arrayOfNulls<LatticePointBCC>(8)
    val simplexLookupSmooth = arrayOfNulls<LatticePointBCC>(8)

    for (i in 0..7) {
      val i1 = i shr 0 and 1
      val j1 = i shr 1 and 1
      val k1 = i shr 2 and 1
      val i2 = i1 xor 1
      val j2 = j1 xor 1
      val k2 = k1 xor 1

      // Quality: Standard. Resolve nearest 4 points.

      // The two points within this octant, one from each of the two cubic half-lattices.
      val csf0 = LatticePointBCC(i1, j1, k1, 0)
      val csf1 = LatticePointBCC(i1 + i2, j1 + j2, k1 + k2, 1)

      // Each single step away on the first half-lattice.
      val csf2 = LatticePointBCC(i1 xor 1, j1, k1, 0)
      val csf3 = LatticePointBCC(i1, j1 xor 1, k1, 0)
      val csf4 = LatticePointBCC(i1, j1, k1 xor 1, 0)

      // Each single step away on the second half-lattice.
      val csf5 = LatticePointBCC(i1 + (i2 xor 1), j1 + j2, k1 + k2, 1)
      val csf6 = LatticePointBCC(i1 + i2, j1 + (j2 xor 1), k1 + k2, 1)
      val csf7 = LatticePointBCC(i1 + i2, j1 + j2, k1 + (k2 xor 1), 1)

      // First two are guaranteed.
      csf0.nextOnSuccess = csf1
      csf0.nextOnFailure = csf0.nextOnSuccess
      csf1.nextOnSuccess = csf2
      csf1.nextOnFailure = csf1.nextOnSuccess

      // Once we find one on the first half-lattice, the rest are out.
      // In addition, knowing csf2 rules out csf5.
      csf2.nextOnFailure = csf3
      csf2.nextOnSuccess = csf6
      csf3.nextOnFailure = csf4
      csf3.nextOnSuccess = csf5
      csf4.nextOnSuccess = csf5
      csf4.nextOnFailure = csf4.nextOnSuccess

      // Once we find one on the second half-lattice, the rest are out.
      csf5.nextOnFailure = csf6
      csf5.nextOnSuccess = null
      csf6.nextOnFailure = csf7
      csf6.nextOnSuccess = null
      csf7.nextOnSuccess = null
      csf7.nextOnFailure = csf7.nextOnSuccess

      simplexLookupStandard[i] = csf0

      // Quality: Smooth. Resolve nearest 8 points.

      // The two points within this octant, one from each of the two cubic half-lattices.
      val css0 = LatticePointBCC(i1, j1, k1, 0)
      val css1 = LatticePointBCC(i1 + i2, j1 + j2, k1 + k2, 1)

      // (1, 0, 0) vs (0, 1, 1) away from octant.
      val css2 = LatticePointBCC(i1 xor 1, j1, k1, 0)
      val css3 = LatticePointBCC(i1, j1 xor 1, k1 xor 1, 0)

      // (1, 0, 0) vs (0, 1, 1) away from octant, on second half-lattice.
      val css4 = LatticePointBCC(i1 + (i2 xor 1), j1 + j2, k1 + k2, 1)
      val css5 = LatticePointBCC(i1 + i2, j1 + (j2 xor 1), k1 + (k2 xor 1), 1)

      // (0, 1, 0) vs (1, 0, 1) away from octant.
      val css6 = LatticePointBCC(i1, j1 xor 1, k1, 0)
      val css7 = LatticePointBCC(i1 xor 1, j1, k1 xor 1, 0)

      // (0, 1, 0) vs (1, 0, 1) away from octant, on second half-lattice.
      val css8 = LatticePointBCC(i1 + i2, j1 + (j2 xor 1), k1 + k2, 1)
      val css9 = LatticePointBCC(i1 + (i2 xor 1), j1 + j2, k1 + (k2 xor 1), 1)

      // (0, 0, 1) vs (1, 1, 0) away from octant.
      val cssA = LatticePointBCC(i1, j1, k1 xor 1, 0)
      val cssB = LatticePointBCC(i1 xor 1, j1 xor 1, k1, 0)

      // (0, 0, 1) vs (1, 1, 0) away from octant, on second half-lattice.
      val cssC = LatticePointBCC(i1 + i2, j1 + j2, k1 + (k2 xor 1), 1)
      val cssD = LatticePointBCC(i1 + (i2 xor 1), j1 + (j2 xor 1), k1 + k2, 1)

      // First two points are guaranteed.
      css0.nextOnSuccess = css1
      css0.nextOnFailure = css0.nextOnSuccess
      css1.nextOnSuccess = css2
      css1.nextOnFailure = css1.nextOnSuccess

      // If css2 is in range, then we know css3 and css4 are not.
      css2.nextOnFailure = css3
      css2.nextOnSuccess = css5
      css3.nextOnFailure = css4
      css3.nextOnSuccess = css4

      // If css4 is in range, then we know css5 is not.
      css4.nextOnFailure = css5
      css4.nextOnSuccess = css6
      css5.nextOnSuccess = css6
      css5.nextOnFailure = css5.nextOnSuccess

      // If css6 is in range, then we know css7 and css8 are not.
      css6.nextOnFailure = css7
      css6.nextOnSuccess = css9
      css7.nextOnFailure = css8
      css7.nextOnSuccess = css8

      // If css8 is in range, then we know css9 is not.
      css8.nextOnFailure = css9
      css8.nextOnSuccess = cssA
      css9.nextOnSuccess = cssA
      css9.nextOnFailure = css9.nextOnSuccess

      // If cssA is in range, then we know cssB and cssC are not.
      cssA.nextOnFailure = cssB
      cssA.nextOnSuccess = cssD
      cssB.nextOnFailure = cssC
      cssB.nextOnSuccess = cssC

      // If cssC is in range, then we know cssD is not.
      cssC.nextOnFailure = cssD
      cssC.nextOnSuccess = null
      cssD.nextOnSuccess = null
      cssD.nextOnFailure = cssD.nextOnSuccess

      simplexLookupSmooth[i] = css0
    }

    LOOKUP_SIMPLEXSTYLE_STANDARD = simplexLookupStandard.requireNoNulls()
    LOOKUP_SIMPLEXSTYLE_SMOOTH = simplexLookupSmooth.requireNoNulls()
  }
}
