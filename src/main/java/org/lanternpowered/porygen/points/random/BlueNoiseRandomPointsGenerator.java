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
package org.lanternpowered.porygen.points.random;

import com.flowpowered.math.vector.Vector2d;
import org.lanternpowered.porygen.util.Rectangled;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * The algorithm is inspired by <a href="https://www.youtube.com/watch?v=lRfdN4L2SUg">
 *     Math for Game Developers - Procedural Generation (White and Blue Noise)</a>
 */
public final class BlueNoiseRandomPointsGenerator extends AbstractRandomPointsGenerator<BlueNoiseRandomPointsGenerator> {

    // The amount of cells in the x direction
    private int xCells = 10;
    // The amount of cells in the y direction
    private int yCells = 10;

    // The amount of percent (0 - 1.0) the cells should
    // cover the rectangle, based on this value will the
    // gap between the cells be calculated

    // Cell coverage in the x direction
    private double xCellCoverage = 0.8;
    // Cell coverage in the y direction
    private double yCellCoverage = 0.8;

    // A array that is reused
    private final ThreadLocal<boolean[]> usedCells = new ThreadLocal<>();

    public int getXCells() {
        return this.xCells;
    }

    public BlueNoiseRandomPointsGenerator setXCells(int xCells) {
        this.xCells = xCells;
        return this;
    }

    public int getYCells() {
        return this.yCells;
    }

    public BlueNoiseRandomPointsGenerator setYCells(int yCells) {
        this.yCells = yCells;
        return this;
    }

    public double getXCellCoverage() {
        return this.xCellCoverage;
    }

    public BlueNoiseRandomPointsGenerator setXCellCoverage(double xCellCoverage) {
        this.xCellCoverage = xCellCoverage;
        return this;
    }

    public double getYCellCoverage() {
        return this.yCellCoverage;
    }

    public BlueNoiseRandomPointsGenerator setYCellCoverage(double yCellCoverage) {
        this.yCellCoverage = yCellCoverage;
        return this;
    }

    @Override
    public void generatePoints(World world, Random random, Rectangled rectangle, List<Vector2d> points) {
        final int min = getPoints().getMin();
        final int max = getPoints().getMax();

        // Randomize the amount of points that will be generated
        int amount = min + random.nextInt(max - min + 1);

        final double minX = rectangle.getMin().getX();
        final double minY = rectangle.getMin().getY();
        final double maxX = rectangle.getMax().getX();
        final double maxY = rectangle.getMax().getY();

        final double dX = maxX - minX;
        final double dY = maxY - minY;

        // Calculate the size of each gap
        final double xGapSize = (dX * (1.0 - this.xCellCoverage)) / this.xCells;
        final double yGapSize = (dY * (1.0 - this.yCellCoverage)) / this.yCells;

        // Calculate the size of each cell
        final double xCellSize = (dX * this.xCellCoverage) / this.xCells;
        final double yCellSize = (dY * this.yCellCoverage) / this.yCells;

        final int cellAmount = this.xCells * this.yCells;
        amount = Math.min(amount, cellAmount); // There cannot be more points than the amount of cells

        boolean[] usedCells = this.usedCells.get();
        // Check if the used cells are initialized, or if the cell amount changed
        if (usedCells == null || usedCells.length != cellAmount) {
            this.usedCells.set(usedCells = new boolean[cellAmount]);
        } else {
            // Reset the used cells
            Arrays.fill(usedCells, false);
        }

        for (int i = 0; i < amount; i++) {
            int cell;
            // Keep searching until a empty cell is found
            while (true) {
                cell = random.nextInt(cellAmount);
                if (!usedCells[cell]) {
                    break;
                }
            }
            usedCells[cell] = true;
            // Get the coordinates from the cell index
            final double cx = cell % this.xCells;
            final double cy = cell / this.xCells;
            // Calculate point coordinates within the cell
            final double x = minX + cx * xCellSize + cx * xGapSize + xGapSize / 2.0;
            final double y = minY + cy * yCellSize + cy * yGapSize + yGapSize / 2.0;
            // Add the point
            points.add(new Vector2d(x, y));
        }
    }
}
