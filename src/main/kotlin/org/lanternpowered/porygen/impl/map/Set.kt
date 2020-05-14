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

/**
 * The sorted set makes sure that the cell elements always appear
 * in the same order, this is important for the processors.
 */
fun <E : CellMapElement> mapElementSetOf(): MutableSet<E> = sortedSetOf(Comparator.comparing { element: E -> element.id })
