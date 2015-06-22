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
 * Utility methods related to projections
 */
public class Projections 
{
    /**
     * Drop the perpendicular on the line from the point p and store 
     * the result in the given point and return it. If the given result
     * point is <code>null</code>, then a new point will be created
     * and returned
     * 
     * @param line The line
     * @param p The point to drop from
     * @param result The point that will store the result
     * @return The result
     */
    public static Point2D dropPerpendicular(
        Line2D line, Point2D p, Point2D result)
    {
        return dropPerpendicular(
            line.getX1(), line.getY1(), 
            line.getX2(), line.getY2(),
            p.getX(), p.getY(), result);
    }
    
    
    /**
     * Drop the perpendicular on the line p0-p1 from the point p 
     * and store the result in the given point and return it. 
     * If the given result point is <code>null</code>, then a new 
     * point will be created and returned
     * 
     * @param p0 The start point of the line
     * @param p1 The end point of the line
     * @param p The point to drop from
     * @param result The point that will store the result
     * @return The result
     */
    public static Point2D dropPerpendicular(
        Point2D p0, Point2D p1, Point2D p, Point2D result)
    {
        return dropPerpendicular(
            p0.getX(), p0.getY(),
            p1.getX(), p1.getY(),
            p.getX(), p.getY(), result);
    }

    /**
     * Drop the perpendicular on the line (x0,y0)-(x1,y1) from the point (x,y) 
     * and store the result in the given point and return it. If the given 
     * result point is <code>null</code>, then a new point will be created
     * and returned
     * 
     * @param x0 The x-coordinate of the start point of the line
     * @param y0 The y-coordinate of the start point of the line
     * @param x1 The x-coordinate of the end point of the line
     * @param y1 The y-coordinate of the end point of the line
     * @param x The x-coordinate of the point to drop from
     * @param y The y-coordinate of the point to drop from
     * @param result The point that will store the result
     * @return The result
     */
    public static Point2D dropPerpendicular(
        double x0, double y0, 
        double x1, double y1,
        double x, double y,
        Point2D result)
    {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double invLength = 1.0 / Math.sqrt(dx*dx+dy*dy);
        double dirX = dx * invLength;
        double dirY = dy * invLength;
        double pdx = x - x0;
        double pdy = y - y0;
        double dot = dirX * pdx + dirY * pdy;
        double resultX = x0 + dirX * dot;
        double resultY = y0 + dirY * dot;
        if (result == null)
        {
            return new Point2D.Double(resultX, resultY);
        }
        result.setLocation(resultX, resultY);
        return result;
    }

//    /**
//     * Computes the relative location of the projection of the point (x,y)
//     * on the line (x0,y0)-(x1,y1). It will be the <i>relative</i> location,
//     * meaning that if the projection is at (x0,y0), then the result will
//     * be 0.0, and if the projection is at (x1,y1), then the result will
//     * be 1.0.
//     * 
//     * @param x0 The x-coordinate of the start point of the line
//     * @param y0 The y-coordinate of the start point of the line
//     * @param x1 The x-coordinate of the end point of the line
//     * @param y1 The y-coordinate of the end point of the line
//     * @param x The x-coordinate of the point to drop from
//     * @param y The y-coordinate of the point to drop from
//     * @return The relative projection location
//     */
//    public static double computeRelativeProjectionLocation(
//        double x0, double y0, 
//        double x1, double y1,
//        double x, double y)
//    {
//        double dx = x1 - x0;
//        double dy = y1 - y0;
//        double invLength = 1.0 / Math.sqrt(dx*dx+dy*dy);
//        double dirX = dx * invLength;
//        double dirY = dy * invLength;
//        double pdx = x - x0;
//        double pdy = y - y0;
//        double dot = dirX * pdx + dirY * pdy;
//        return dot;
//    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private Projections()
    {
        // Private constructor to prevent instantiation
    }
}
