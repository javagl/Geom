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

/**
* Utility class for computing intersections
* <br>
* NOTE: Preliminary. The inner classes might be moved to different
* packages, or their methods might be moved to different classes
* (but they will likely remain available with the same signature)
*/
public class Intersections
{

    /**
     * Class for computing intersections between line segments
     */
    public static class SegmentSegment
    {
        /**
         * Computes the intersection of the given lines segment.
         * 
         * @param line0 The first line segment
         * @param line1 The second line segment
         * @param relativeLocation Optional location that stores the 
         * relative location of the intersection point on 
         * the given line segments
         * @param absoluteLocation Optional location that stores the 
         * absolute location of the intersection point
         * @return Whether the line segments intersect. That is,
         * when they are not parallel, and the relative location 
         * is in [0,1] for both lines.
         */
        public static boolean intersect( 
            Line2D line0, Line2D line1,
            Point2D relativeLocation,
            Point2D absoluteLocation)
        {
            return intersect(
                line0.getX1(), line0.getY1(), 
                line0.getX2(), line0.getY2(),
                line1.getX1(), line1.getY1(), 
                line1.getX2(), line1.getY2(),
                relativeLocation, absoluteLocation);
        }
    	
        /**
         * Computes the intersection of the specified line segments.
         * 
         * @param s0x0 x-coordinate of point 0 of line segment 0
         * @param s0y0 y-coordinate of point 0 of line segment 0
         * @param s0x1 x-coordinate of point 1 of line segment 0
         * @param s0y1 y-coordinate of point 1 of line segment 0
         * @param s1x0 x-coordinate of point 0 of line segment 1
         * @param s1y0 y-coordinate of point 0 of line segment 1
         * @param s1x1 x-coordinate of point 1 of line segment 1
         * @param s1y1 y-coordinate of point 1 of line segment 1
         * @param relativeLocation Optional location that stores the 
         * relative location of the intersection point on 
         * the given line segments
         * @param absoluteLocation Optional location that stores the 
         * absolute location of the intersection point
         * @return Whether the line segments intersect. That is,
         * when they are not parallel, and the relative location 
         * is in [0,1] for both lines.
         */
        public static boolean intersect( 
            double s0x0, double s0y0,
            double s0x1, double s0y1,
            double s1x0, double s1y0,
            double s1x1, double s1y1,
            Point2D relativeLocation,
            Point2D absoluteLocation)
        {
            if (relativeLocation == null)
            {
                relativeLocation = new Point2D.Double();
            }
            boolean intersect = LineLine.intersect(
                s0x0, s0y0, s0x1, s0y1, s1x0, s1y0, s1x1, s1y1, 
                relativeLocation, absoluteLocation);
            if (intersect)
            {
                if (relativeLocation.getX() >= 0.0 && 
                    relativeLocation.getX() <= 1.0 && 
                    relativeLocation.getY() >= 0.0 && 
                    relativeLocation.getY() <= 1.0)
                {
                    return true;
                }
            }
            return false;
        }

        /**
         * Private constructor to prevent instantiation
         */
        private SegmentSegment()
        {
            // Private constructor to prevent instantiation
        }
        
    }
    
    /**
     * Class for computing intersections between lines
     */
    public static class LineLine
    {
    	
        /**
         * Computes the intersection of the given lines.
         * 
         * @param line0 The first line
         * @param line1 The second line
         * @param relativeLocation Optional location that stores the 
         * relative location of the intersection point on 
         * the given line segments
         * @param absoluteLocation Optional location that stores the 
         * absolute location of the intersection point
         * @return Whether the lines intersect
         */
        public static boolean intersect( 
            Line2D line0, Line2D line1,
            Point2D relativeLocation,
            Point2D absoluteLocation)
        {
            return intersect(
                line0.getX1(), line0.getY1(), 
                line0.getX2(), line0.getY2(),
                line1.getX1(), line1.getY1(), 
                line1.getX2(), line1.getY2(),
                relativeLocation, absoluteLocation);
        }
    	
        /**
         * Computes the intersection of the specified lines.
         * 
         * Ported from 
         * http://www.geometrictools.com/LibMathematics/Intersection/
         *     Wm5IntrSegment2Segment2.cpp
         * 
         * @param s0x0 x-coordinate of point 0 of line segment 0
         * @param s0y0 y-coordinate of point 0 of line segment 0
         * @param s0x1 x-coordinate of point 1 of line segment 0
         * @param s0y1 y-coordinate of point 1 of line segment 0
         * @param s1x0 x-coordinate of point 0 of line segment 1
         * @param s1y0 y-coordinate of point 0 of line segment 1
         * @param s1x1 x-coordinate of point 1 of line segment 1
         * @param s1y1 y-coordinate of point 1 of line segment 1
         * @param relativeLocation Optional location that stores the 
         * relative location of the intersection point on 
         * the given line segments
         * @param absoluteLocation Optional location that stores the 
         * absolute location of the intersection point
         * @return Whether the lines intersect
         */
        public static boolean intersect( 
            double s0x0, double s0y0,
            double s0x1, double s0y1,
            double s1x0, double s1y0,
            double s1x1, double s1y1,
            Point2D relativeLocation,
            Point2D absoluteLocation)
        {
            double dx0 = s0x1 - s0x0;
            double dy0 = s0y1 - s0y0;
            double dx1 = s1x1 - s1x0;
            double dy1 = s1y1 - s1y0;
    
            double invLen0 = 1.0 / Math.sqrt(dx0*dx0+dy0*dy0); 
            double invLen1 = 1.0 / Math.sqrt(dx1*dx1+dy1*dy1); 
    
            double dir0x = dx0 * invLen0;
            double dir0y = dy0 * invLen0;
            double dir1x = dx1 * invLen1;
            double dir1y = dy1 * invLen1;
    
            double dot = dotPerp(dir0x, dir0y, dir1x, dir1y);
            if (Math.abs(dot) > Geom.DOUBLE_EPSILON)
            {
                if (relativeLocation != null || absoluteLocation != null)
                {
                    double c0x = s0x0 + dx0 * 0.5;
                    double c0y = s0y0 + dy0 * 0.5;
                    double c1x = s1x0 + dx1 * 0.5;
                    double c1y = s1y0 + dy1 * 0.5;
            
                    double cdx = c1x - c0x;
                    double cdy = c1y - c0y;
                	
                    double dot0 = dotPerp(cdx, cdy, dir0x, dir0y);
                    double dot1 = dotPerp(cdx, cdy, dir1x, dir1y);
                    double invDot = 1.0/dot;
                    double s0 = dot1*invDot;
                    double s1 = dot0*invDot;
                    if (relativeLocation != null)
                    {
                        double n0 = (s0 * invLen0) + 0.5;
                        double n1 = (s1 * invLen1) + 0.5;
                        relativeLocation.setLocation(n0, n1);
                    }
                    if (absoluteLocation != null)
                    {
                        double x = c0x + s0 * dir0x;
                        double y = c0y + s0 * dir0y;
                        absoluteLocation.setLocation(x, y);
                    }
                }
                return true;
            }
            return false;
        }
        
        /**
         * Private constructor to prevent instantiation
         */
        private LineLine()
        {
            // Private constructor to prevent instantiation
        }
        
    }

    /**
     * Returns the perpendicular dot product, i.e. the length
     * of the vector (x0,y0,0)x(x1,y1,0).
     * 
     * @param x0 Coordinate x0
     * @param y0 Coordinate y0
     * @param x1 Coordinate x1
     * @param y1 Coordinate y1
     * @return The length of the cross product vector
     */
    private static double dotPerp(double x0, double y0, double x1, double y1)
    {
        return x0*y1 - y0*x1;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Intersections()
    {
        // Private constructor to prevent instantiation
    }
}
