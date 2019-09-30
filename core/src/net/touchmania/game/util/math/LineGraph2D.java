/*
 * Copyright 2018 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.touchmania.game.util.math;

import com.google.common.base.Preconditions;

import java.util.*;

/**
 * <p>A {@link Graph2D} where marker point are interpolated using a linear algorithm (consecutive
 * points joined by straight lines). </p>
 * <p> The image of x located before the first point, or after the last point is calculated as follows:
 * <li>The image of x before the first point is undefined if the first point is a jump, otherwise
 * it is calculated by considering it belonging to the line that passes the first two points.</li>
 * <li>The image of x located after the last point is undefined if the last point is a jump, otherwise
 * it is calculated by considering it belonging to the line that passes the last two points.</li>
 * </p>
 *
 * @author flood2d
 */
public class LineGraph2D implements Graph2D {
    private TreeMap<Double, Graph2DPoint> points = new TreeMap<>();
    private int jumpCount = 0;

    @Override
    public Graph2DPoint putPoint(Double x, Double y) {
        return putPoint(new Graph2DPoint(x, y, 0.0D, false));
    }

    @Override
    public Graph2DPoint putPoint(Graph2DPoint point) {
        Preconditions.checkNotNull(point, "Cannot add a null point.");
        Graph2DPoint oldPoint = points.get(point.x);
        if(oldPoint != null && oldPoint.isJump()) {
            jumpCount--;
        }
        points.put(point.x, point);
        if(point.isJump()) {
            jumpCount++;
        }
        return oldPoint;
    }

    @Override
    public Graph2DPoint removePoint(Double x) {
        Graph2DPoint point = points.remove(x);
        if(point != null) {
            if(point.isJump()) {
                jumpCount--;
            }
            return point;
        }
        return null;
    }

    @Override
    public Graph2DPoint removePoint(Graph2DPoint point) {
        return point != null ? removePoint(point.x) : null;
    }

    @Override
    public Collection<Graph2DPoint> getPoints() {
        return points.values();
    }

    @Override
    public int getPointCount() {
        return points.size();
    }

    @Override
    public Double putJump(Double x, Double amount, boolean leftDefined) {
        Graph2DPoint point = points.get(x);
        if(point != null) {
            if(point.isJump() && amount.equals(0.0D)) {
                jumpCount--;
            } else if(!point.isJump() && !amount.equals(0.0D)) {
                jumpCount++;
            }
            Double oldJump = point.jump;
            point.jump = amount;
            point.leftDefined = leftDefined;
            return oldJump;
        } else if(!amount.equals(0.0D)){
            points.put(x, new Graph2DPoint(x, f(x), amount, leftDefined));
            jumpCount++;
        }
        return 0.0D;
    }

    @Override
    public Double removeJump(Double x) {
        Graph2DPoint point = points.get(x);
        if(point != null && point.isJump()) {
            Double oldJump = point.jump;
            point.jump = 0.0D;
            jumpCount--;
            return oldJump;
        }
        return 0.0D;
    }

    @Override
    public boolean isJump(Double x) {
        Graph2DPoint point = points.get(x);
        return point != null && point.isJump();
    }

    @Override
    public Double getJumpAmount(Double x) {
        Graph2DPoint point = points.get(x);
        if(point != null) {
            return point.jump;
        }
        return 0.0D;
    }

    @Override
    public int getJumpCount() {
        return jumpCount;
    }

    @Override
    public Double f(Double x) {
        Preconditions.checkState(getPointCount() > 1, "The graph needs at least two points.");
        Map.Entry<Double, Graph2DPoint> floorEntry, ceilingEntry;

        floorEntry = points.floorEntry(x);
        if(floorEntry != null) {
            if(floorEntry.getKey().equals(x)) {
                Graph2DPoint point = floorEntry.getValue();
                if(point.leftDefined) {
                    return point.y;
                } else {
                    return point.y + point.jump;
                }
            }

            ceilingEntry = points.ceilingEntry(x);
            if(ceilingEntry == null) {//x is after the last marker point in the map
                if(floorEntry.getValue().isJump()) {
                    return Double.NaN; //The function is undefined
                }
                ceilingEntry = floorEntry;
                floorEntry = points.lowerEntry(ceilingEntry.getKey());
            }
        } else {
            //x is before the first marker point in the map
            floorEntry = points.firstEntry();
            if(floorEntry.getValue().isJump()) {
                return Double.NaN; //The function is undefined
            }
            ceilingEntry = points.higherEntry(floorEntry.getKey());
        }

        Graph2DPoint floorPoint = floorEntry.getValue();
        Graph2DPoint ceilingPoint = ceilingEntry.getValue();
        double x1 = floorPoint.x,
               x2 = ceilingPoint.x,
               y1 = floorPoint.y + floorPoint.jump,
               y2 = ceilingPoint.y,
               m = (y2 - y1) / (x2 - x1),
               q = -m * x1 + y1;
        return m * x + q;
    }

    @Override
    public boolean isDefined(Double x) {
        Preconditions.checkState(getPointCount() > 1, "The graph needs at least two points.");
        if(points.floorEntry(x) == null) {
            return !points.firstEntry().getValue().isJump();
        }
        if(points.ceilingEntry(x) == null) {
            return !points.lastEntry().getValue().isJump();
        }
        return true;
    }

    @Override
    public boolean isInvertible() {
        if(getPointCount() < 2) {
            return false; //The graph needs at least two points.
        }
        //Check if the function is monotonic, otherwise it's not invertible.
        double signum = 0.0D; //1.0 if the function is increasing, -1.0 if it's decreasing.
        Graph2DPoint prevPoint = null;
        for(Graph2DPoint point : getPoints()) {
            if(prevPoint != null) {
                double prevY = prevPoint.y + prevPoint.jump;
                double currY = point.y;
                double nextSignum = Math.signum(currY - prevY); //Signum of y variation (deltaY signum).
                if (Double.compare(signum, 0.0D) != 0 && Double.compare(nextSignum, 0.0D) != 0) {
                    if (signum < 0.0D && nextSignum > 0.0D || signum > 0.0D && nextSignum < 0.0D) {
                        //the function isn't monotonic.
                        return false;
                    }
                }
                signum = nextSignum;
            }
            prevPoint = point;
        }
        return true; //the function is monotonic.
    }

    @Override
    public LineGraph2D invert() {
        if(!isInvertible()) {
            throw new IllegalStateException("Cannot invert the graph, it's not monotonic.");
        }
        LineGraph2D invertedGraph = new LineGraph2D();
        Graph2DPoint prevPoint = null;
        for(Graph2DPoint point : getPoints()) {
            if(prevPoint != null && Double.compare(point.y, prevPoint.y) == 0) {
                invertedGraph.putJump(point.y, point.x - prevPoint.x + invertedGraph.getJumpAmount(point.y), false);
            } else {
                invertedGraph.putPoint(point.y, point.x);
            }
            if(point.isJump()) {
                invertedGraph.putPoint(point.y + point.jump, point.x);
            }
            prevPoint = point;
        }
        return invertedGraph;
    }
}
