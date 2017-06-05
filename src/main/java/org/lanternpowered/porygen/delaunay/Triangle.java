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
/*
 * Copyright (c) 2007 by L. Paul Chew.
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package org.lanternpowered.porygen.delaunay;

import org.lanternpowered.porygen.util.ArraySet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

/**
 * A Triangle is an immutable Set of exactly three Pnts.
 *
 * All Set operations are available. Individual vertices can be accessed via
 * iterator() and also via triangle.get(index).
 *
 * Note that, even if two triangles have the same vertex set, they are
 * *different* triangles. Methods equals() and hashCode() are consistent with
 * this rule.
 *
 * @author Paul Chew
 *
 * Created December 2007. Replaced general simplices with geometric triangle.
 *
 */
class Triangle extends ArraySet<Pnt> {

    private int idNumber;                   // The id number
    @Nullable private Pnt circumcenter = null;        // The triangle's circumcenter

    private static int idGenerator = 0;     // Used to create id numbers
    public static boolean moreInfo = false; // True iff more info in toString

    /**
     * @param vertices the vertices of the Triangle.
     * @throws IllegalArgumentException if there are not three distinct vertices
     */
    public Triangle(Pnt... vertices) {
        this(Arrays.asList(vertices));
    }

    /**
     * @param collection a Collection holding the Simplex vertices
     * @throws IllegalArgumentException if there are not three distinct vertices
     */
    public Triangle(Collection<? extends Pnt> collection) {
        super(collection);
        idNumber = idGenerator++;
        if (this.size() != 3) {
            throw new IllegalArgumentException("Triangle must have 3 vertices");
        }
    }

    @Override
    public String toString() {
        if (!moreInfo) {
            return "Triangle" + idNumber;
        }
        return "Triangle" + idNumber + super.toString();
    }

    /**
     * Get arbitrary vertex of this triangle, but not any of the bad vertices.
     * @param badVertices one or more bad vertices
     * @return a vertex of this triangle, but not one of the bad vertices
     * @throws NoSuchElementException if no vertex found
     */
    public Pnt getVertexButNot(Pnt... badVertices) {
        final Collection<Pnt> bad = Arrays.asList(badVertices);
        for (Pnt v : this) {
            if (!bad.contains(v)) {
                return v;
            }
        }
        throw new NoSuchElementException("No vertex found");
    }

    /**
     * True iff triangles are neighbors. Two triangles are neighbors if they
     * share a facet.
     * @param triangle the other Triangle
     * @return true iff this Triangle is a neighbor of triangle
     */
    public boolean isNeighbor(Triangle triangle) {
        int count = 0;
        for (Pnt vertex : this) {
            if (!triangle.contains(vertex)) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Report the facet opposite vertex.
     *
     * @param vertex A vertex of this Triangle
     * @return The facet opposite vertex
     * @throws IllegalArgumentException If the vertex is not in triangle
     */
    public ArraySet<Pnt> facetOpposite(Pnt vertex) {
        final ArraySet<Pnt> facet = new ArraySet<>(this);
        if (!facet.remove(vertex)) {
            throw new IllegalArgumentException("Vertex not in triangle");
        }
        return facet;
    }

    /**
     * @return the triangle's circumcenter
     */
    public Pnt getCircumcenter() {
        if (this.circumcenter == null) {
            this.circumcenter = Pnt.circumcenter(toArray(new Pnt[0]));
        }
        return this.circumcenter;
    }

    /* The following two methods ensure that a Triangle is immutable */

    @Override
    public boolean add(Pnt vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Pnt> iterator () {
        return new Iterator<Pnt>() {
            private Iterator<Pnt> it = Triangle.super.iterator();
            public boolean hasNext() {return it.hasNext();}
            public Pnt next() {return it.next();}
            public void remove() {throw new UnsupportedOperationException();}
        };
    }

    /* The following two methods ensure that all triangles are different. */

    @Override
    public int hashCode () {
        return (int)(idNumber^(idNumber>>>32));
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

}
