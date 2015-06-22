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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility methods related to rectangles
 */
public class Rectangles
{
    /**
     * Compute the union of the given rectangles, and store it in the given
     * result rectangle.<br>
     * <br>
     * If the given result rectangle is <code>null</code>, then a new 
     * rectangle will be created and returned.
     * 
     * @param rectangles The rectangles
     * @param result The rectangle that will store the result
     * @return The result. If the given sequence of rectangles is
     * empty, then a rectangle at (0,0) with size (0,0) will be returned.
     */
    public static Rectangle2D union(
        Iterable<? extends Rectangle2D> rectangles, Rectangle2D result)
    {
        if (result == null)
        {
            result = new Rectangle2D.Double();
        }
        boolean first = true;
        for (Rectangle2D r : rectangles)
        {
            if (first)
            {
                result.setRect(r);
                first = false;
            }
            else
            {
                Rectangle2D.union(result, r, result);
            }
        }
        return result;
    }

    /**
     * Return a copy of the given rectangle
     * 
     * @param r The rectangle
     * @return The copy of the given rectangle
     */
    public static Rectangle2D copy(Rectangle2D r)
    {
        return new Rectangle2D.Double(
            r.getX(), r.getY(), r.getWidth(),r.getHeight());
    }

    /**
     * Move the center of the given rectangle to the given position,
     * and store the result in the given result rectangle.<br>
     * <br>
     * If the given result rectangle is <code>null</code>, then a new 
     * rectangle will be created and returned.
     * 
     * @param r The rectangle
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param result The rectangle that will store the result
     * @return The result rectangle
     */
    public static Rectangle2D moveCenterTo(
        Rectangle2D r, double x, double y, Rectangle2D result)
    {
        if (result == null)
        {
            result = new Rectangle2D.Double();
        }
        result.setRect(
            x - r.getWidth() * 0.5, 
            y - r.getHeight() * 0.5,
            r.getWidth(), 
            r.getHeight());
        return result;
    }

    /**
     * Translate the given rectangle by the specified amount,
     * and store the result in the given result rectangle.<br>
     * <br>
     * If the given result rectangle is <code>null</code>, then a new 
     * rectangle will be created and returned.
     * 
     * @param r The rectangle
     * @param dx The movement in x-direction
     * @param dy The movement in y-direction
     * @param result The rectangle that will store the result
     * @return The result rectangle
     */
    public static Rectangle2D translate(
        Rectangle2D r, double dx, double dy, Rectangle2D result)
    {
        if (result == null)
        {
            result = new Rectangle2D.Double();
        }
        result.setRect(
            r.getX() + dx, 
            r.getY() + dy, 
            r.getWidth(),
            r.getHeight());
        return result;
    }

    /**
     * Scale the given rectangle by the given factor, about its center,
     * and store the result in the given result rectangle.<br>
     * <br>
     * If the given result rectangle is <code>null</code>, then a new 
     * rectangle will be created and returned.
     * 
     * @param r The rectangle
     * @param factor The scaling factor
     * @param result The rectangle that will store the result
     * @return The result rectangle
     */
    public static Rectangle2D scale(
        Rectangle2D r, double factor, Rectangle2D result)
    {
        if (result == null)
        {
            result = new Rectangle2D.Double();
        }
        double w = factor * r.getWidth();
        double h = factor * r.getHeight();
        result.setRect(
            r.getCenterX() - w * 0.5, 
            r.getCenterY() - h * 0.5, 
            w, h);
        return r;
    }

    /**
     * Store the center of the given rectangle in the given point,
     * and return it.<br>
     * <br>
     * If the given point is <code>null</code>, then a new point
     * will be created and returned
     * 
     * @param r The rectangle
     * @param result The point that will store the result
     * @return The result
     */
    public static Point2D center(Rectangle2D r, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        result.setLocation(r.getCenterX(), r.getCenterY());
        return result;
    }

    
    /**
     * Compute the bounding rectangle of the given rectangle after it
     * has been transformed with the given transform, and stores it
     * in the given destination. If the given destination is <code>null</code>
     * then a new rectangle will be created and returned
     * 
     * @param at The transform
     * @param rSrc The source rectangle
     * @param rDst The destination rectangle
     * @return The destination rectangle
     */
    public static Rectangle2D computeBounds(
        AffineTransform at, Rectangle2D rSrc, Rectangle2D rDst)
    {
        double xMin = rSrc.getMinX();
        double yMin = rSrc.getMinY();
        double xMax = rSrc.getMaxX();
        double yMax = rSrc.getMaxY();
        
        double x0 = AffineTransforms.computeX(at, xMin, yMin);
        double y0 = AffineTransforms.computeY(at, xMin, yMin);
        double x1 = AffineTransforms.computeX(at, xMax, yMin);
        double y1 = AffineTransforms.computeY(at, xMax, yMin);
        double x2 = AffineTransforms.computeX(at, xMax, yMax);
        double y2 = AffineTransforms.computeY(at, xMax, yMax);
        double x3 = AffineTransforms.computeX(at, xMin, yMax);
        double y3 = AffineTransforms.computeY(at, xMin, yMax);

        double minX = min(x0, x1, x2, x3);
        double minY = min(y0, y1, y2, y3);
        double maxX = max(x0, x1, x2, x3);
        double maxY = max(y0, y1, y2, y3);
        
        if (rDst == null)
        {
            return new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY);
        }
        rDst.setRect(minX, minY, maxX-minX, maxY-minY);
        return rDst;
    }

    /**
     * Returns the minimum of the given numbers 
     * 
     * @param d0 The first number
     * @param d1 The second number
     * @param d2 The third number
     * @param d3 The fourth number
     * @return The result
     */
    private static double min(double d0, double d1, double d2, double d3)
    {
        double min = d0;
        if (d1 < min)
        {
            min = d1;
        }
        if (d2 < min)
        {
            min = d2;
        }
        if (d3 < min)
        {
            min = d3;
        }
        return min;
    }
    
    /**
     * Returns the maximum of the given numbers 
     * 
     * @param d0 The first number
     * @param d1 The second number
     * @param d2 The third number
     * @param d3 The fourth number
     * @return The result
     */
    private static double max(double d0, double d1, double d2, double d3)
    {
        double max = d0;
        if (d1 > max)
        {
            max = d1;
        }
        if (d2 > max)
        {
            max = d2;
        }
        if (d3 > max)
        {
            max = d3;
        }
        return max;
    }
    
    
    
    /**
     * Computes the specified corner of the given rectangle, and stores
     * it in the given destination point. If the given destination point
     * is <code>null</code>, then a new point will be created and returned.
     * 
     * @param rectangle The rectangle
     * @param index The index. This may be an arbitrary value, and will
     * be wrapped to be in the range [0,3].
     * @param pDst The destination point
     * @return The destination point.
     */
    public static Point2D getCorner(
        Rectangle2D rectangle, int index, Point2D pDst)
    {
        if (pDst == null)
        {
            pDst = new Point2D.Double();
        }
        int i = positiveRemainder(index, 4);
        switch (i)
        {
            case 0:
                pDst.setLocation(rectangle.getMinX(), rectangle.getMinY());
                break;
            case 1:
                pDst.setLocation(rectangle.getMaxX(), rectangle.getMinY());
                break;
            case 2:
                pDst.setLocation(rectangle.getMaxX(), rectangle.getMaxY());
                break;
            case 3:
                pDst.setLocation(rectangle.getMinX(), rectangle.getMaxY());
                break;

            default:
                break;
        }
        throw new AssertionError("Remainder must be 0,1,2 or 3");
    }
    
    /**
     * Returns the positive remainder of the specified division
     * 
     * @param value The value to divide
     * @param d The value divided by
     * @return The positive remainder
     */
    private static int positiveRemainder(int value, int d)
    {
        int result = value % d;
        if (result < 0) 
        {
            return result + d;        
        }
        return result;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Rectangles()
    {
        // Private constructor to prevent instantiation
    }

}
