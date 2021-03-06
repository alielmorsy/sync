/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.util.math;

import java.util.Collection;

/**
 * Represents a graph of a two-dimensional function that passes across a collection of marker points.
 * Marker points are interpolated by a specific algorithm to generate the function. The graph also
 * supports jump discontinuities. The graph f(x) method will return {@link Double#NaN} if the
 * function is undefined at the given x.
 *
 * @author Vincenzo Fortunato
 */
public interface Graph2D extends MathFunction<Double, Double> {
    /**
     * Add a marker point at the given x. Marker points are interpolated to create
     * a function by the interpolation algorithm of the graph specific implementation.
     * If there is already a marker point at the given x, it will be replaced by the new one
     * and any jump at the given x will be removed.
     * @param x the marker point x.
     * @param y the marker point y.
     * @return the previous marker point if there was one, null otherwise.
     */
    Graph2DPoint putPoint(Double x, Double y);

    /**
     * Add a marker point. Marker points are interpolated to create
     * a function by the interpolation algorithm of the graph specific implementation.
     * If there is already a marker point at the given x, it will be replaced by the new one
     * and any jump at the given x will be removed.
     * @param point the marker point.
     * @return the previous marker point if there was one, null otherwise.
     */
    Graph2DPoint putPoint(Graph2DPoint point);

    /**
     * Remove the marker point at the given x if there is one. Removing a marker point
     * will also remove any jump associated with it.
     * @param x the marker point x.
     * @return the removed marker point if there was one, or null otherwise.
     */
    Graph2DPoint removePoint(Double x);

    /**
     * Remove a marker point if there is one. Removing a marker point
     * will also remove any jump associated with it.
     * @param point the marker point .
     * @return the removed marker point if there was one, or null otherwise.
     */
    Graph2DPoint removePoint(Graph2DPoint point);

    /**
     * @return a collection of graph marker points ordered by using their x values.
     */
    Collection<Graph2DPoint> getPoints();

    /**
     * @return the count of marker points.
     */
    int getPointCount();

    /**
     * Add a jump discontinuity at the given x. Adding a jump where there isn't a marker point
     * will automatically add a marker point at the given x.
     * When the jump is left defined the image of the given x is the left limit at that x.
     * When the jump isn't left defined the image of the given x is the right limit at that x.
     * @param x the jump x value.
     * @param amount the jump amount. Can be negative.
     * @param leftDefined if true the image of the given x will be the left limit.
     * @return the previous jump amount if there was one, or 0.0D (no jump).
     */
    Double putJump(Double x, Double amount, boolean leftDefined);

    /**
     * Remove a jump discontinuity at the given x, if there is one.
     * @param x the jump x value.
     * @return the amount of the removed jump if there was one, or 0.0D otherwise (no jump).
     */
    Double removeJump(Double x);

    /**
     * Check if there is a jump discontinuity at the given x.
     * @param x the x value to check.
     * @return true if there is a jump at the given x, false otherwise.
     */
    boolean isJump(Double x);

    /**
     * Get jump discontinuity amount at the given x. Will return 0.0D if there is no
     * jump at the given x value.
     * @param x the jump x value.
     * @return the jump amount, or 0.0D if there's no jump at the given x value.
     */
    Double getJumpAmount(Double x);

    /**
     * @return the count of jump discontinuities.
     */
    int getJumpCount();

    @Override
    Graph2D invert();

    /**
     * {@inheritDoc}
     * Returns {@link Double#NaN} if the
     * function is undefined at the given x.
     * @param x the x variable of the function.
     * @return the image at the given x, or {@link Double#NaN} if the
     * image is undefined.
     */
    @Override
    Double f(Double x);
}
