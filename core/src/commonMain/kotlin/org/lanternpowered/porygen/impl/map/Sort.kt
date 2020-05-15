/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.impl.map

import org.lanternpowered.porygen.map.CellMapElement

fun <E : CellMapElement> LinkedHashSet<E>.sort() {
  val sorted = toList().sortedBy { element -> element.id }
  clear()
  addAll(sorted)
}

fun <E : CellMapElement> Sequence<E>.sortToSet(): Set<E> = sortedBy { element -> element.id }.toSet()
fun <E : CellMapElement> Sequence<E>.sortToMap(): Map<Long, E> = sortedBy { element -> element.id }.associateBy { it.id }
