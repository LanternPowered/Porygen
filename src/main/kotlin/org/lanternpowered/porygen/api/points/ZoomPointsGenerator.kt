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
package org.lanternpowered.porygen.api.points

import com.flowpowered.math.vector.Vector2d
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.util.geom.Rectangled
import kotlin.streams.toList

/**
 * Zooms to a specific area on points from the
 * underlying [PointsGenerator].
 */
class ZoomPointsGenerator(private val parent: PointsGenerator, private val zoomFactor: Vector2d) : PointsGenerator {

    override fun generatePoints(context: GeneratorContext, rectangle: Rectangled): List<Vector2d> {
        val min = rectangle.min
        val max = rectangle.max
        val zoomOutRectangle = Rectangled(min, max.sub(min).mul(this.zoomFactor).add(min))
        return this.parent.generatePoints(context, zoomOutRectangle).stream()
                .filter { rectangle.contains(it) }
                .toList()
    }
}
