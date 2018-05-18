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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class GridBasedRandomPointsGenerator extends AbstractRandomPointsGenerator<GridBasedRandomPointsGenerator> {

    // The amount of columns
    private int columns = 10;
    // The amount of rows
    private int rows = 10;

    // A array that is reused
    private final ThreadLocal<boolean[]> usedPoints = new ThreadLocal<>();

    public int getColumns() {
        return this.columns;
    }

    public GridBasedRandomPointsGenerator setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    public int getRows() {
        return this.rows;
    }

    public GridBasedRandomPointsGenerator setRows(int rows) {
        this.rows = rows;
        return this;
    }

    @Override
    public List<Vector2d> generatePoints(World world, Random random, Rectangled rectangle) {
        final List<Vector2d> points = new ArrayList<>();

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

        // Calculate the distance between each grid line
        final double xLineDiff = dX / this.columns;
        final double yLineDiff = dY / this.rows;

        final int gridPointsAmount = this.columns * this.rows;
        amount = Math.min(amount, gridPointsAmount); // There cannot be more points than the amount of points in the grid

        boolean[] usedPoints = this.usedPoints.get();
        // Check if the used points are initialized, or if the grid size changed
        if (usedPoints == null || usedPoints.length != gridPointsAmount) {
            this.usedPoints.set(usedPoints = new boolean[gridPointsAmount]);
        } else {
            // Reset the used cells
            Arrays.fill(usedPoints, false);
        }

        for (int i = 0; i < amount; i++) {
            int gridPoint;
            // Keep searching until a empty cell is found
            while (true) {
                gridPoint = random.nextInt(gridPointsAmount);
                if (!usedPoints[gridPoint]) {
                    break;
                }
            }
            usedPoints[gridPoint] = true;
            // Get the coordinates from the point index
            final double px = gridPoint % this.columns;
            final double py = gridPoint / this.rows;
            // Calculate point coordinates within the cell
            final double x = minX + xLineDiff * px + xLineDiff / 2.0;
            final double y = minY + yLineDiff * py + yLineDiff / 2.0;
            // Add the point
            points.add(new Vector2d(x, y));
        }

        return points;
    }
}
