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
package org.lanternpowered.porygen.api.points.random

import com.flowpowered.math.vector.Vector2d
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.points.PointsGenerator
import org.lanternpowered.porygen.api.util.geom.Rectangled
import java.util.*

/**
 * A simple [PointsGenerator] that generates random
 * points within the [Rectangled].
 */
class WhiteNoiseRandomPointsGenerator : AbstractRandomPointsGenerator() {

    override fun generatePoints(context: GeneratorContext, rectangle: Rectangled): List<Vector2d> {
        val random = context.random
        val points = ArrayList<Vector2d>()

        val min = this.points.start
        val max = this.points.endInclusive

        // Randomize the amount of points that will be generated
        val amount = min + random.nextInt(max - min + 1)

        val minX = rectangle.min.x
        val minY = rectangle.min.y
        val maxX = rectangle.max.x
        val maxY = rectangle.max.y

        val dX = maxX - minX
        val dY = maxY - minY

        for (i in 0 until amount) {
            val x = minX + random.nextDouble() * dX
            val y = minY + random.nextDouble() * dY
            points.add(Vector2d(x, y))
        }

        return points
    }
}
