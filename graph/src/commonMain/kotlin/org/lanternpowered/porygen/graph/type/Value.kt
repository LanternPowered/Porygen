/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.type

import org.lanternpowered.porygen.value.Value
import org.lanternpowered.porygen.value.Value2
import org.lanternpowered.porygen.value.Value3

abstract class I1Value(id: String, title: String) : I1Transform<Value>(id, title, Value::class)
abstract class I1Value2(id: String, title: String) : I1Transform<Value2>(id, title, Value2::class)
abstract class I1Value3(id: String, title: String) : I1Transform<Value3>(id, title, Value3::class)

abstract class I2Value(id: String, title: String) : I2Transform<Value>(id, title, Value::class)
abstract class I2Value2(id: String, title: String) : I2Transform<Value2>(id, title, Value2::class)
abstract class I2Value3(id: String, title: String) : I2Transform<Value3>(id, title, Value3::class)
