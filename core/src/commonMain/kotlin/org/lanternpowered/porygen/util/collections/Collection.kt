/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.util.collections

fun <E> List<E>.asUnmodifiableList(): List<E> =
  if (this is UnmodifiableList<*>) this else UnmodifiableList(this)

fun <E> Set<E>.asUnmodifiableSet(): Set<E> =
  if (this is UnmodifiableSet<*>) this else UnmodifiableSet(this)

fun <E> Collection<E>.asUnmodifiableCollection(): Collection<E> =
  if (this is UnmodifiableCollection<*>) this else UnmodifiableCollection(this)

private class UnmodifiableSet<E>(
  private val delegate: Set<E>
) : Set<E> by delegate {
  override fun iterator(): Iterator<E> =
    UnmodifiableIterator(delegate.iterator())
}

private class UnmodifiableCollection<E>(
  private val delegate: Collection<E>
) : Collection<E> by delegate {
  override fun iterator(): Iterator<E> =
    UnmodifiableIterator(delegate.iterator())
}

private class UnmodifiableIterator<T>(
  private val delegate: Iterator<T>
) : Iterator<T> by delegate

private class UnmodifiableList<E>(
  private val delegate: List<E>
) : List<E> by delegate {
  override fun iterator(): Iterator<E> =
    UnmodifiableIterator(delegate.iterator())
  override fun listIterator(): ListIterator<E> =
    UnmodifiableListIterator(delegate.listIterator())
  override fun listIterator(index: Int): ListIterator<E> =
    UnmodifiableListIterator(delegate.listIterator(index))
}

private class UnmodifiableListIterator<T>(
  private val delegate: ListIterator<T>
) : ListIterator<T> by delegate
