/*
 * www.javagl.de - Geom - Geometry utilities
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.geom;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Methods to compute the convex hull of a set of points
 */
public class ConvexHull 
{
    
    /**
     * Compute the list of points that form the convex hull of the given
     * input points. If there are less than 4 input points, a list 
     * containing the input points will be returned (regardless of
     * whether these points are degenerate, i.e. even when they are
     * on one line or at the same location)
     * 
     * @param inputPoints The input points
     * @return The convex hull points
     */
    public static List<Point2D> compute(List<? extends Point2D> inputPoints)
    {
        if (inputPoints.size() <= 3)
        {
            return new ArrayList<Point2D>(inputPoints);
        }
        List<Point2D> points = new ArrayList<Point2D>(inputPoints);
        
        // Compute the reference point, using a YX_COMPARATOR: It sorts points 
        // by their y-coordinate. If two points have the same y-coordinate, 
        // it sorts them by the x-coordinate.
        Point2D referencePoint = Collections.min(points, Points.YX_COMPARATOR);

        // Sort the points. The primary sorting criterion is the angle that
        // the line from the reference point to the point has to the x-axis. 
        // The secondary sorting criterion will be the YX_COMPARATOR.
        Comparator<Point2D> byAngleComparator = 
            Points.byAngleComparator(referencePoint);
        Comparator<Point2D> comparator = 
            composeComparators(byAngleComparator, Points.YX_COMPARATOR);
        Collections.sort(points, comparator);
        
        List<Point2D> newPoints = makeAnglesUnique(points);
        List<Point2D> result = scan(newPoints);
        return result;
    }
    
    /**
     * Perform the actual Graham Scan on the given points
     * 
     * @param points The input points
     * @return The points of the convex hull
     */
    private static List<Point2D> scan(List<Point2D> points)
    {
        List<Point2D> result = new ArrayList<Point2D>();
        result.add(points.get(0));
        result.add(points.get(1));
        for (int i=2; i<points.size(); i++)
        {
            Point2D p0 = result.get(result.size()-1);
            Point2D p1 = result.get(result.size()-2);
            Point2D p = points.get(i);
            
            double x0 = p0.getX();
            double y0 = p0.getY();
            double x1 = p1.getX();
            double y1 = p1.getY();
            double x = p.getX();
            double y = p.getY();
            int r = Line2D.relativeCCW(x0, y0, x1, y1, x, y);
            //System.out.println("relative "+r+" for "+p0+" "+p1+" "+p);
            // A check for r>0 should be sufficient, but
            // may cause problems for equal points. So
            // doing the conservative r>=0 check here.
            if (r >= 0)
            {
                result.add(p);
            }
            else
            {
                result.remove(result.size()-1);
                i--;
            }
        }
        return result;
    }
    
    /**
     * Given the sorted list of points, with point 0 being the reference
     * point: Create a list containing the points that have a unique
     * angle referring to the reference point. When two points have
     * the same angle, then the point that is further away from the
     * reference point will be kept.
     * 
     * @param points The input points
     * @return The points with unique angles
     */
    private static List<Point2D> makeAnglesUnique(List<Point2D> points)
    {
        Point2D referencePoint = points.get(0);
        List<Point2D> newPoints = new ArrayList<Point2D>();
        newPoints.add(referencePoint);
        double previousAngle = Math.PI * 2;
        double previousDistanceSquared = Double.MAX_VALUE;
        for (int i=1; i<points.size(); i++)
        {
            Point2D p = points.get(i);
            double angle = Lines.angleToX(referencePoint, p);
            if (Math.abs(angle - previousAngle) > Geom.DOUBLE_EPSILON)
            {
                newPoints.add(p);
            }
            else
            {
                double distanceSquared = referencePoint.distanceSq(p);
                if (distanceSquared > previousDistanceSquared)
                {
                    newPoints.set(newPoints.size()-1, p);
                }
            }
            previousAngle = angle;
            previousDistanceSquared = referencePoint.distanceSq(p);
        }
        return newPoints;
    }
    
    
    /**
     * Creates a comparator that combines the given comparators. They are
     * applied in the given order, and if any of them returns a 
     * non-<code>0</code> result for a pair of values, then
     * this result is returned. Otherwise, the comparator returns 
     * <code>0</code>.
     *  
     * @param comparators The delegate comparators
     * @return The composed comparator
     */
    @SafeVarargs
    private static <T> Comparator<T> composeComparators(
        Comparator<? super T> ... comparators)
    {
        return new Comparator<T>()
        {
            @Override
            public int compare(T t0, T t1)
            {
                for (Comparator<? super T> comparator : comparators)
                {
                    int result = comparator.compare(t0, t1);
                    if (result != 0)
                    {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ConvexHull()
    {
        // Private constructor to prevent instantiation
    }

}
