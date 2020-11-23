/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.data

import org.lanternpowered.porygen.noise.NoiseModule
import org.lanternpowered.porygen.util.Colors
import org.lanternpowered.porygen.value.DoubleSupplier
import org.lanternpowered.porygen.value.Vec2dToDouble
import org.lanternpowered.porygen.value.Vec3dToDouble

object PorygenDataTypes {

  fun init(registry: DataTypeRegistry) {
    registry.createFromClass<Vec2dToDouble>("vec2d_to_double") { color(Colors.Yellow) }
    registry.createFromClass<Vec3dToDouble>("vec3d_to_double") { color(Colors.Purple) }
    registry.createFromClass<DoubleSupplier>("double") {
      color(Colors.Blue)
      supertype<Vec2dToDouble>()
      supertype<Vec3dToDouble>()
    }
    registry.createFromClass<NoiseModule>("noise_module") {
      color(Colors.Green)
      supertype<Vec2dToDouble>()
      supertype<Vec3dToDouble>()
    }
  }
}
