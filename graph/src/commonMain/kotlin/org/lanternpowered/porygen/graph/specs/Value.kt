/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs

import org.lanternpowered.porygen.value.DoubleSupplier
import org.lanternpowered.porygen.value.Vec2dToDouble
import org.lanternpowered.porygen.value.Vec3dToDouble

abstract class I1Value(id: String, title: String) : I1Transform<DoubleSupplier>(id, title, DoubleSupplier::class)
abstract class I1Value2(id: String, title: String) : I1Transform<Vec2dToDouble>(id, title, Vec2dToDouble::class)
abstract class I1Value3(id: String, title: String) : I1Transform<Vec3dToDouble>(id, title, Vec3dToDouble::class)

abstract class I2Value(id: String, title: String) : I2Transform<DoubleSupplier>(id, title, DoubleSupplier::class)
abstract class I2Value2(id: String, title: String) : I2Transform<Vec2dToDouble>(id, title, Vec2dToDouble::class)
abstract class I2Value3(id: String, title: String) : I2Transform<Vec3dToDouble>(id, title, Vec3dToDouble::class)
