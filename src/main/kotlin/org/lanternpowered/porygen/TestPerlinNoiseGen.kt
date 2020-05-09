/*
 * This file is part of Porygen, licensed under the MIT License (MIT).
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lanternpowered.porygen

import com.flowpowered.noise.module.source.Perlin
import org.lanternpowered.porygen.api.util.dsi.SplitMix64Random

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
