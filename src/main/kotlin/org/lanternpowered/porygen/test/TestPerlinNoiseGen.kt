/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.test

import org.spongepowered.noise.module.source.Perlin

object TestPerlinNoiseGen {

  @JvmStatic
  fun main(args: Array<String>) {
    val perlin = Perlin()
    perlin.frequency = 0.5
    perlin.persistence = 1.0
    perlin.seed = java.lang.Long.hashCode(1845414548963412154L)
    perlin.octaveCount = 1

    //val noise = SimplexNoise3d()

    //println("Expected max: " + (Math.pow(perlin.persistence, perlin.octaveCount.toDouble()) - 1) / (perlin.persistence - 1))

    var minValue: Double = Double.MAX_VALUE
    var maxValue: Double = Double.MIN_VALUE

    println("Test perlin generator")

    val iter = 100

    for (x in 0..iter) {
      // println("x: $x")
      for (y in 0..iter) {
        //println("y: $y")
        for (z in 0..iter) {
          //println("z: $z")
          val value = perlin.getValue(x.toDouble() * 0.13498782121, y.toDouble() * 0.3453412347868, z.toDouble() * 0.754542131)
          if (value < minValue) {
            minValue = value
          }
          if (value > maxValue) {
            maxValue = value
          }
          //println(value)
        }
      }
    }

    println("min: $minValue")
    println("max: $maxValue")
  }
}
