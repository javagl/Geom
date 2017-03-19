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
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Utility methods related to points
 */
public class Points
{
    /**
     * A comparator that compares points colexicographically
     * by their y- and x coordinate
     */
    static final Comparator<Point2D> YX_COMPARATOR = 
        new Comparator<Point2D>() 
    {
        @Override
        public int compare(Point2D p0, Point2D p1) 
        {
            int cy = Double.compare(p0.getY(), p1.getY());
            if (cy != 0)
            {
                return cy;
            }
            return Double.compare(p0.getX(), p1.getX());
        }
    };
    
    /**
     * Returns a comparator that compares points colexicographically.
     * That means that it compares the points by their y-coordinate,
     * and, if these are equal, by their x-coordinate
     * 
     * @return The comparator
     */
    public static Comparator<Point2D> colexicographicalComparator()
    {
        return YX_COMPARATOR;
    }

    
    /**
     * A comparator that compares points lexicographically
     * by their x- and y coordinate
     */
    static final Comparator<Point2D> XY_COMPARATOR = 
        new Comparator<Point2D>() 
    {
        @Override
        public int compare(Point2D p0, Point2D p1) 
        {
            int cx = Double.compare(p0.getX(), p1.getX());
            if (cx != 0)
            {
                return cx;
            }
            return Double.compare(p0.getY(), p1.getY());
        }
    };
    
    /**
     * Returns a comparator that compares points lexicographically.
     * That means that it compares the points by their x-coordinate,
     * and, if these are equal, by their y-coordinate
     * 
     * @return The comparator
     */
    public static Comparator<Point2D> lexicographicalComparator()
    {
        return XY_COMPARATOR;
    }
    
    /**
     * Creates a comparator that compares points by the angle that the line
     * between the given center and the point has to the x-axis.
     * 
     * @param center The center
     * @return The comparator
     */
    public static Comparator<Point2D> byAngleComparator(
        Point2D center)
    {
        return byAngleComparator(center.getX(),  center.getY());
    }
    
    /**
     * Creates a comparator that compares points by the angle that the line
     * between the specified center and the point has to the x-axis.
     * 
     * @param centerX The x-coordinate of the center
     * @param centerY The y-coordinate of the center
     * @return The comparator
     */
    public static Comparator<Point2D> byAngleComparator(
        double centerX, double centerY)
    {
        return new Comparator<Point2D>()
        {
            @Override
            public int compare(Point2D p0, Point2D p1)
            {
                double angle0 = Lines.normalizeAngle(Lines.angleToX(
                    centerX, centerY, p0.getX(), p0.getY()));
                double angle1 = Lines.normalizeAngle(Lines.angleToX(
                    centerX, centerY, p1.getX(), p1.getY()));
                //System.out.println("Angle " 
                //    + Math.toDegrees(angle0) + " and "
                //    + Math.toDegrees(angle1));
                return Double.compare(angle0, angle1);
            }
        };
    }    
    
    
    /**
     * Returns a comparator that compares points by their distance to
     * the specified reference point
     * 
     * @param x The x-coordinate of the reference point
     * @param y The y-coordinate of the reference point
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceComparator(
        double x, double y)
    {
        return new Comparator<Point2D>()
        {
            @Override
            public int compare(Point2D p0, Point2D p1)
            {
                double dx0 = p0.getX() - x;
                double dy0 = p0.getY() - y;
                double dx1 = p1.getX() - x;
                double dy1 = p1.getY() - y;
                double d0 = dx0 * dx0 + dy0 * dy0;
                double d1 = dx1 * dx1 + dy1 * dy1;
                return Double.compare(d0, d1);
            }
        };
    }
    
    /**
     * Returns a comparator that compares points by their distance to
     * the given reference point
     * 
     * @param reference The reference point
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceComparator(Point2D reference)
    {
        return byDistanceComparator(reference.getX(), reference.getY());
    }

    /**
     * Returns a comparator that compares points by their distance to
     * the origin
     * 
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceToOriginComparator()
    {
        return byDistanceComparator(0, 0);
    }
    

    /**
     * Returns a comparator that compares points by their distance to
     * the specified line
     * 
     * @param line The line
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceToLineComparator(
        Line2D line)
    {
        return byDistanceToLineComparator(
            line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }


    /**
     * Returns a comparator that compares points by their distance to
     * the specified line
     * 
     * @param p0 The start point of the line
     * @param p1 The end point of the line
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceToLineComparator(
        Point2D p0, Point2D p1)
    {
        return byDistanceToLineComparator(
            p0.getX(), p0.getY(), p1.getX(), p1.getY());
    }
    
    /**
     * Returns a comparator that compares points by their distance to
     * the specified line
     * 
     * @param x0 The x-coordinate of the start point of the line
     * @param y0 The y-coordinate of the start point of the line
     * @param x1 The x-coordinate of the end point of the line
     * @param y1 The y-coordinate of the end point of the line
     * @return The comparator
     */
    public static Comparator<Point2D> byDistanceToLineComparator(
        double x0, double y0, double x1, double y1)
    {
        return new Comparator<Point2D>()
        {
            @Override
            public int compare(Point2D p0, Point2D p1)
            {
                double d0 = Line2D.ptLineDistSq(
                    x0, y0, x1, y1, p0.getX(), p0.getY());
                double d1 = Line2D.ptLineDistSq(
                    x0, y0, x1, y1, p1.getX(), p1.getY());
                return Double.compare(d0, d1);
            }
        };
    }
    
    
    
    /**
     * Computes the difference between the given points and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param p0 The first point
     * @param p1 The second point
     * @param result The result
     * @return The result
     */
    public static Point2D sub(Point2D p0, Point2D p1, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        result.setLocation(p0.getX()-p1.getX(), p0.getY()-p1.getY());
        return result;
    }

    /**
     * Computes the sum the given points and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param p0 The first point
     * @param p1 The second point
     * @param result The result
     * @return The result
     */
    public static Point2D add(Point2D p0, Point2D p1, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        result.setLocation(p0.getX()+p1.getX(), p0.getY()+p1.getY());
        return result;
    }
    
    /**
     * Scales the given point with the given factor and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param p0 The point
     * @param factor The scaling factor
     * @param result The result
     * @return The result
     */
    public static Point2D scale(Point2D p0, double factor, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        result.setLocation(p0.getX()*factor, p0.getY()*factor);
        return result;
    }

    /**
     * Computes <code>p0 + factor * p1</code> and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param p0 The first point
     * @param factor The scaling factor for the second point
     * @param p1 The second point
     * @param result The result
     * @return The result
     */
    public static Point2D addScaled(
        Point2D p0, double factor, Point2D p1, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        result.setLocation(
            p0.getX()+factor*p1.getX(), 
            p0.getY()+factor*p1.getY());
        return result;
    }
    
    
    
    /**
     * Interpolates linearly between the given points, and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param p0 The first point
     * @param p1 The second point
     * @param alpha The position between the points (usually between 0 and 1)
     * @param result The result
     * @return The result
     */
    public static Point2D interpolate(
        Point2D p0, Point2D p1, double alpha, Point2D result)
    {
        return interpolate(p0.getX(), p0.getY(), p1.getX(), p1.getY(), 
            alpha, result);
    }    
    
    /**
     * Interpolates linearly between the specified points, and stores the
     * result in the given result point. If the given result point 
     * is <code>null</code>, a new point will be created and returned.
     * 
     * @param x0 The x-coordinate of the first point
     * @param y0 The y-coordinate of the first point
     * @param x1 The x-coordinate of the second point
     * @param y1 The y-coordinate of the second point
     * @param alpha The position between the points (usually between 0 and 1)
     * @param result The result
     * @return The result
     */
    public static Point2D interpolate(
        double x0, double y0, double x1, double y1, 
        double alpha, Point2D result)
    {
        if (result == null)
        {
            result = new Point2D.Double();
        }
        double dx = x1 - x0;
        double dy = y1 - y0;
        result.setLocation(x0 + alpha * dx, y0 + alpha * dy);
        return result;
    }    
    
   
    /**
     * Transforms the given point with the inverse of the given transform,
     * and stores the result in the given destination point. If the given
     * destination point is <code>null</code>, a new point will be created
     * and returned.
     * 
     * @param at The affine transform
     * @param pSrc The source point
     * @param pDst The destination point
     * @return The destination point
     * @throws IllegalArgumentException If the given transform is not
     * invertible
     */
    public static Point2D inverseTransform(
        AffineTransform at, Point2D pSrc, Point2D pDst)
    {
        try
        {
            return at.inverseTransform(pSrc, pDst);
        }
        catch (NoninvertibleTransformException e)
        {
            throw new IllegalArgumentException(
                "Non-invertible transform", e);
        }
    }
    
    /**
     * Transforms all the given points with the given affine transform,
     * and returns the results
     * 
     * @param at The affine transform
     * @param points The input points
     * @return The transformed points
     */
    public static List<Point2D> transform(
        AffineTransform at, Iterable<? extends Point2D> points)
    {
        List<Point2D> result = new ArrayList<Point2D>();
        for (Point2D p : points)
        {
            Point2D tp = at.transform(p, null);
            result.add(tp);
        }
        return result;
    }
    
    
    /**
     * Compute the bounding box of the given points
     * 
     * @param points The input points
     * @return The bounding box of the given points
     */
    public static Rectangle2D computeBounds(
        Iterable<? extends Point2D> points)
    {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (Point2D p : points)
        {
            double x = p.getX();
            double y = p.getY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        return new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY);
    }
    
    /**
     * Computes the center of gravity of the given sequence of points. This
     * is simply the average of all points. If the given sequence is empty,
     * then <code>null</code> is returned.
     * 
     * @param points The points
     * @return The center of gravity
     */
    public static Point2D computeCenterOfGravity(
        Iterable<? extends Point2D> points)
    {
        int counter = 0;
        double sumX = 0.0;
        double sumY = 0.0;
        for (Point2D point : points)
        {
            sumX += point.getX();
            sumY += point.getY();
            counter++;
        }
        if (counter == 0)
        {
            return null;
        }
        return new Point2D.Double(sumX / counter, sumY / counter);
    }
    

    /**
     * Creates a short string representation of the given point
     * 
     * @param point The point
     * @return The string
     */
    public static String toString(Point2D point)
    {
        return toString(point, "%f");
    }

    /**
     * Creates a short string representation of the given point, using
     * the given format for the coordinates
     * 
     * @param point The point
     * @param format The format
     * @return The string
     */
    public static String toString(Point2D point, String format)
    {
        return toString(point, Locale.getDefault(), format);
    }
    
    /**
     * Creates a short string representation of the given point, using
     * the given format for the coordinates
     * 
     * @param point The point
     * @param locale The locale
     * @param format The format
     * @return The string
     */
    public static String toString(Point2D point, Locale locale, String format)
    {
        String sx = String.format(format, point.getX());
        String sy = String.format(format, point.getY());
        return "("+sx+","+sy+")";
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Points()
    {
        // Private constructor to prevent instantiation
    }
    

}
