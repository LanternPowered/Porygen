/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.data

/**
 * Represents something that can hold data.
 */
interface DataHolder {

  /**
   * Gets the data attached for the given key.
   *
   * @param key The key
   * @param T The value type
   * @return The value if present, otherwise null
   */
  operator fun <T> get(key: DataKey<T>): T?

  /**
   * Requires the data attached for the given key.
   *
   * @param key The key
   * @param T The value type
   * @return The value
   */
  fun <T> require(key: DataKey<T>): T =
      get(key) ?: throw IllegalArgumentException("Can't find key: ${key.name} for $this.")

  /**
   * Sets the data attached for the given key.
   *
   * @param key The key
   * @param value The value
   * @param T The value type
   */
  operator fun <T> set(key: DataKey<T>, value: T)

  /**
   * Removes the data attached for the given key.
   *
   * @param key The key
   * @param T The value type
   * @return The removed value, or null if not present
   */
  fun <T> remove(key: DataKey<T>): T?
}
