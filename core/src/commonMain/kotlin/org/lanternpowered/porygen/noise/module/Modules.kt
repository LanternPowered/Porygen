/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.noise.module

import org.lanternpowered.porygen.math.vector.Vector3d
import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.noise.module.source.Constant

fun NoiseModule.translatePoint(
    x: NoiseModule = Constant(0.0),
    y: NoiseModule = Constant(0.0),
    z: NoiseModule = Constant(0.0)
): NoiseModule = TranslatePoint(this, x, y, z)

fun NoiseModule.translatePoint(
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0
): NoiseModule = TranslatePoint(this, x, y, z)

fun NoiseModule.translatePoint(offset: Vector3d): NoiseModule = TranslatePoint(this, offset)

fun NoiseModule.scalePoint(
    x: Double = 1.0,
    y: Double = 1.0,
    z: Double = 1.0
): NoiseModule = ScalePoint(this, x, y, z)

fun NoiseModule.scalePoint(scale: Vector3d): NoiseModule = ScalePoint(this, scale)

operator fun NoiseModule.plus(other: NoiseModule): NoiseModule = Add(this, other)
operator fun NoiseModule.plus(other: Double): NoiseModule = Add(this, other)

operator fun NoiseModule.times(other: NoiseModule): NoiseModule = Multiply(this, other)
operator fun NoiseModule.times(other: Double): NoiseModule = this * Constant(other)

operator fun NoiseModule.minus(other: NoiseModule): NoiseModule = Add(this, other * -1.0)
operator fun NoiseModule.minus(other: Double): NoiseModule = Add(this, -other)

operator fun NoiseModule.unaryMinus(): NoiseModule = this * -1.0
operator fun NoiseModule.unaryPlus(): NoiseModule = this

fun NoiseModule.pow(exponent: NoiseModule): NoiseModule = Power(this, exponent)
fun NoiseModule.pow(exponent: Double): NoiseModule = Power(this, exponent)

fun abs(module: NoiseModule): NoiseModule = Abs(module)
