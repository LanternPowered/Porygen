/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.noise

import org.spongepowered.noise.module.Module

class Constant(private val value: Double) : Module(0), NoiseModule {

  override fun getSourceModuleCount(): Int = 0
  override fun getValue(x: Double, y: Double, z: Double): Double = value

  override fun get(x: Double, y: Double, z: Double): Double = value
}
