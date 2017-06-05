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
package org.lanternpowered.porygen.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Straightforward undirected graph implementation.
 * Nodes are generic type N.
 *
 * @author Paul Chew
 *
 * Created November, December 2007. For use in Delaunay/Voronoi code.
 */
public final class Graph<N> {

    private Map<N, Set<N>> theNeighbors = new HashMap<>(); // Node -> adjacent nodes
    private Set<N> theNodeSet = Collections.unmodifiableSet(this.theNeighbors.keySet()); // Set view of all nodes

    /**
     * Add a node. If node is already in graph then no change.
     *
     * @param node The node to add
     */
    public void add(N node) {
        if (this.theNeighbors.containsKey(node)) {
            return;
        }
        this.theNeighbors.put(node, new ArraySet<>());
    }

    /**
     * Add a link. If the link is already in graph then no change.
     *
     * @param nodeA One end of the link
     * @param nodeB The other end of the link
     * @throws NullPointerException If either endpoint is not in graph
     */
    public void add(N nodeA, N nodeB) throws NullPointerException {
        this.theNeighbors.get(nodeA).add(nodeB);
        this.theNeighbors.get(nodeB).add(nodeA);
    }

    /**
     * Remove node and any links that use node. If node not in graph, nothing
     * happens.
     *
     * @param node The node to remove.
     */
    public void remove(N node) {
        if (!this.theNeighbors.containsKey(node)) {
            return;
        }
        for (N neighbor : this.theNeighbors.get(node)) {
            this.theNeighbors.get(neighbor).remove(node); // Remove "to" links
        }
        this.theNeighbors.remove(node).clear(); // Remove "from" links and remove the node
    }

    /**
     * Remove the specified link. If link not in graph, nothing happens.
     *
     * @param nodeA One end of the link
     * @param nodeB The other end of the link
     * @throws NullPointerException If either endpoint is not in graph
     */
    public void remove(N nodeA, N nodeB) throws NullPointerException {
        this.theNeighbors.get(nodeA).remove(nodeB);
        this.theNeighbors.get(nodeB).remove(nodeA);
    }

    /**
     * Report all the neighbors of node.
     *
     * @param node The node
     * @return The neighbors of node
     * @throws NullPointerException If node does not appear in graph
     */
    public Set<N> neighbors(N node) throws NullPointerException {
        return Collections.unmodifiableSet(this.theNeighbors.get(node));
    }

    /**
     * Returns an unmodifiable Set view of the nodes contained in this graph.
     * The set is backed by the graph, so changes to the graph are reflected in
     * the set.
     *
     * @return A Set view of the graph's node set
     */
    public Set<N> nodeSet() {
        return this.theNodeSet;
    }

}
