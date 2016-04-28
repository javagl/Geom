/*
 * www.javagl.de - Geom - Geometry utilities
 *
 * Copyright (c) 2013-2016 Marco Hutter - http://www.javagl.de
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

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods related to Shapes
 */
public class Shapes
{
    /**
     * Create a list containing line segments that approximate the given 
     * shape.
     * 
     * @param shape The shape
     * @param flatness The allowed flatness
     * @return The list of line segments
     */
    public static List<Line2D> computeLineSegments(
        Shape shape, double flatness)
    {
        List<Line2D> result = new ArrayList<Line2D>();
        PathIterator pi = shape.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double previous[] = new double[2];
        double first[] = new double[2];
        while (!pi.isDone())
        {
            int segment = pi.currentSegment(coords);
            switch (segment)
            {
                case PathIterator.SEG_MOVETO:
                    previous[0] = coords[0];
                    previous[1] = coords[1];
                    first[0] = coords[0];
                    first[1] = coords[1];
                    break;

                case PathIterator.SEG_CLOSE:
                    result.add(new Line2D.Double(
                        previous[0], previous[1],
                        first[0], first[1]));
                    previous[0] = first[0];
                    previous[1] = first[1];
                    break;

                case PathIterator.SEG_LINETO:
                    result.add(new Line2D.Double(
                        previous[0], previous[1],
                        coords[0], coords[1]));
                    previous[0] = coords[0];
                    previous[1] = coords[1];
                    break;

                case PathIterator.SEG_QUADTO:
                    // Should never occur
                    throw new AssertionError(
                        "SEG_QUADTO in flattened path!");

                case PathIterator.SEG_CUBICTO:
                    // Should never occur
                    throw new AssertionError(
                        "SEG_CUBICTO in flattened path!");

                default:
                    // Should never occur
                    throw new AssertionError(
                        "Invalid segment in flattened path!");
            }
            pi.next();
        }
        return result;
    }

    /**
     * Create a list containing line points that approximate the given 
     * shape.
     * 
     * @param shape The shape
     * @param flatness The allowed flatness
     * @param storeOnClose Whether a point should be stored when the 
     * shape is closed. (This will result in points being stored twice
     * in the returned list). 
     * @return The list of points
     */
    public static List<Point2D> computePoints(
        Shape shape, double flatness, boolean storeOnClose)
    {
        List<Point2D> result = new ArrayList<Point2D>();
        PathIterator pi = shape.getPathIterator(null, flatness);
        double[] coords = new double[6];
        Point2D previousMove = null;
        while (!pi.isDone())
        {
            int segment = pi.currentSegment(coords);
            switch (segment)
            {
                case PathIterator.SEG_MOVETO:
                    result.add(new Point2D.Double(coords[0], coords[1]));
                    if (storeOnClose)
                    {
                        previousMove = new Point2D.Double(coords[0], coords[1]);
                    }
                    break;

                case PathIterator.SEG_CLOSE:
                    if (storeOnClose)
                    {
                        result.add(previousMove);
                    }
                    break;

                case PathIterator.SEG_LINETO:
                    result.add(new Point2D.Double(coords[0], coords[1]));
                    break;

                case PathIterator.SEG_QUADTO:
                    // Should never occur
                    throw new AssertionError(
                        "SEG_QUADTO in flattened path!");

                case PathIterator.SEG_CUBICTO:
                    // Should never occur
                    throw new AssertionError(
                        "SEG_CUBICTO in flattened path!");

                default:
                    // Should never occur
                    throw new AssertionError(
                        "Invalid segment in flattened path!");
            }
            pi.next();
        }
        return result;
    }
    
    
    /**
     * Create a shape that is created from interpolating between the 
     * given shapes, according to the given interpolation value
     * 
     * @param shape0 The first shape
     * @param shape1 The second shape
     * @param alpha The interpolation value, usually between 0.0 and 1.0
     * @return The interpolated shape
     * @throws IllegalArgumentException If the given shapes do not consist
     * of the same segments (that is, when they are not structurally equal)
     */
    public static Shape interpolate(Shape shape0, Shape shape1, double alpha)
    {
        Path2D path = new Path2D.Double();
        
        PathIterator pi0 = shape0.getPathIterator(null);
        PathIterator pi1 = shape1.getPathIterator(null);
        
        double coords0[] = new double[6];
        double coords1[] = new double[6];
        double coords[] = new double[6];
        while (!pi0.isDone())
        {
            if (pi1.isDone())
            {
                throw new IllegalArgumentException(
                    "Iterator 1 is done, but not iterator 0");
            }
            int segment0 = pi0.currentSegment(coords0);
            int segment1 = pi1.currentSegment(coords1);
            if (segment0 != segment1)
            {
                throw new IllegalArgumentException(
                    "Incompatible segments: "+segment0+" vs. "+segment1);
            }
            switch (segment0)
            {
                case PathIterator.SEG_MOVETO:
                    interpolate(coords0, coords1, coords, alpha, 2);
                    path.moveTo(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_LINETO:
                    interpolate(coords0, coords1, coords, alpha, 2);
                    path.lineTo(coords[0], coords[1]);
                    break;
                    
                case PathIterator.SEG_QUADTO:
                    interpolate(coords0, coords1, coords, alpha, 4);
                    path.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                    
                case PathIterator.SEG_CUBICTO:
                    interpolate(coords0, coords1, coords, alpha, 6);
                    path.curveTo(
                        coords[0], coords[1], 
                        coords[2], coords[3], 
                        coords[4], coords[5]);
                    break;
                    
                case PathIterator.SEG_CLOSE:
                    path.closePath();
                    break;
                
                default:
                    throw new AssertionError("Unknown segment type");
            }
            pi0.next();
            pi1.next();
        }
        if (!pi1.isDone())
        {
            throw new IllegalArgumentException(
                "Iterator 0 is done, but not iterator 1");
        }
        
        return path;
    }
 
    /**
     * Interpolate between <code>c0</code> and <code>c1</code> according
     * to the given alpha value, and place the result in <code>c</code>.
     * This assumes that none of the arrays is <code>null</code>, and
     * that all arrays have the size <code>n</code>.
     * 
     * @param c0 The first values
     * @param c1 The second values
     * @param c The result
     * @param alpha The interpolation value, usually between 0.0 and 1.0
     * @param n The size of the arrays
     */
    private static void interpolate(
        double c0[], double c1[], double c[], double alpha, int n)
    {
        for (int i=0; i<n; i++)
        {
            c[i] = c0[i] + (c1[i]-c0[i]) * alpha;
        }
    }
    
    /**
     * Compute all closed regions that occur in the given shape, as
     * lists of points, each describing one polygon
     * 
     * @param shape The shape
     * @param flatness The flatness for the shape path iterator
     * @return The regions
     */
    static List<List<Point2D>> computeRegions(
        Shape shape, double flatness)
    {
        List<List<Point2D>> regions = new ArrayList<List<Point2D>>();
        PathIterator pi = shape.getPathIterator(null, flatness);
        double coords[] = new double[6];
        List<Point2D> region = Collections.emptyList();
        while (!pi.isDone())
        {
            switch (pi.currentSegment(coords))
            {
                case PathIterator.SEG_MOVETO:
                    region = new ArrayList<Point2D>();
                    region.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                    
                case PathIterator.SEG_LINETO:
                    region.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                    
                case PathIterator.SEG_CLOSE:
                    regions.add(region);
                    break;
                    
                case PathIterator.SEG_CUBICTO:
                case PathIterator.SEG_QUADTO:
                default:
                    throw new AssertionError(
                        "Invalid segment in flattened path");
            }
            pi.next();
        }
        return regions;
    }
    
    /**
     * Computes the (signed) area enclosed by the given point list.
     * The area will be positive if the points are ordered 
     * counterclockwise, and and negative if the points are ordered 
     * clockwise.
     * 
     * @param points The points
     * @return The signed area
     */
    static double computeSignedArea(List<? extends Point2D> points)
    {
        double sum0 = 0;
        double sum1 = 0;
        for (int i=0; i<points.size()-1; i++)
        {
            int i0 = i;
            int i1 = i + 1;
            Point2D p0 = points.get(i0);
            Point2D p1 = points.get(i1);
            double x0 = p0.getX();
            double y0 = p0.getY();
            double x1 = p1.getX();
            double y1 = p1.getY();
            sum0 += x0 * y1;
            sum1 += x1 * y0;
        }
        Point2D p0 = points.get(0);
        Point2D pn = points.get(points.size()-1);
        double x0 = p0.getX();
        double y0 = p0.getY();
        double xn = pn.getX();
        double yn = pn.getY();
        sum0 += xn * y0;
        sum1 += x0 * yn;
        double area = 0.5 * (sum0 - sum1);
        return area;
    }
    
    /**
     * Compute the (signed) area that is covered by the given shape.<br>
     * <br>
     * The area will be positive for regions where the points are 
     * ordered counterclockwise, and and negative for regions where 
     * the points are ordered clockwise.
     * 
     * @param shape The shape
     * @param flatness The flatness for the path iterator
     * @return The signed area
     */ 
    public static double computeSignedArea(Shape shape, double flatness)
    {
        double area = 0;
        List<List<Point2D>> regions = computeRegions(shape, flatness);
        for (List<Point2D> region : regions)
        {
            double signedArea = computeSignedArea(region);
            //System.out.println("got "+signedArea+" for "+region);
            area += signedArea;
        }
        return area;
    }
    
    
    /**
     * Computes the length of the given shape (i.e. its border)
     * 
     * @param shape The shape
     * @param flatness The flatness for the iteration
     * @return The length of the given shape
     */
    public static double computeLength(
        Shape shape, double flatness)
    {
        PathIterator pi = shape.getPathIterator(null, flatness);
        double coords[] = new double[6];
        double previousMoveX = 0;
        double previousMoveY = 0;
        double length = 0;
        while (!pi.isDone())
        {
            switch (pi.currentSegment(coords))
            {
                case PathIterator.SEG_MOVETO:
                    previousMoveX = coords[0];
                    previousMoveY = coords[1];
                    break;
                    
                case PathIterator.SEG_LINETO:
                case PathIterator.SEG_CLOSE:
                {
                    double dx = coords[0] - previousMoveX;
                    double dy = coords[1] - previousMoveY;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    length += distance;
                    break;
                }
                    
                case PathIterator.SEG_CUBICTO:
                case PathIterator.SEG_QUADTO:
                default:
                    throw new AssertionError(
                        "Invalid segment in flattened path");
            }
            pi.next();
        }
        return length;
    }
    
    /**
     * Computes the list of sub-shapes of the given shape. These are the
     * shapes that are separated in the given shape via SEG_MOVETO or 
     * SEG_CLOSE operations
     * 
     * @param shape The input shape
     * @return The sub-shapes
     */
    public static List<Shape> computeSubShapes(Shape shape)
    {
        List<Shape> result = new ArrayList<Shape>();
        PathIterator pi = shape.getPathIterator(null);
        double[] coords = new double[6];
        double previous[] = new double[2];
        double first[] = new double[2];
        Path2D currentShape = null;
        while (!pi.isDone())
        {
            int segment = pi.currentSegment(coords);
            switch (segment)
            {
                case PathIterator.SEG_MOVETO:
                    if (currentShape != null)
                    {
                         result.add(currentShape);
                         currentShape = null;
                    }
                    previous[0] = coords[0];
                    previous[1] = coords[1];
                    first[0] = coords[0];
                    first[1] = coords[1];
                    break;

                case PathIterator.SEG_CLOSE:
                    if (currentShape != null)
                    {
                        currentShape.closePath();
                        result.add(currentShape);
                        currentShape = null;
                    }
                    previous[0] = first[0];
                    previous[1] = first[1];
                    break;

                case PathIterator.SEG_LINETO:
                    if (currentShape == null)
                    {
                        currentShape = new Path2D.Double();
                        currentShape.moveTo(previous[0], previous[1]);
                    }
                    currentShape.lineTo(coords[0], coords[1]);
                    previous[0] = coords[0];
                    previous[1] = coords[1];
                    break;

                case PathIterator.SEG_QUADTO:
                    if (currentShape == null)
                    {
                        currentShape = new Path2D.Double();
                        currentShape.moveTo(previous[0], previous[1]);
                    }
                    currentShape.quadTo(
                        coords[0], coords[1], 
                        coords[2], coords[3]);
                    previous[0] = coords[2];
                    previous[1] = coords[3];
                    break;

                case PathIterator.SEG_CUBICTO:
                    if (currentShape == null)
                    {
                        currentShape = new Path2D.Double();
                        currentShape.moveTo(previous[0], previous[1]);
                    }
                    currentShape.curveTo(
                        coords[0], coords[1], 
                        coords[2], coords[3],
                        coords[4], coords[5]);
                    previous[0] = coords[4];
                    previous[1] = coords[5];
                    break;

                default:
                    // Should never occur
                    throw new AssertionError(
                        "Invalid segment in path!");
            }
            pi.next();
        }
        if (currentShape != null)
        {
             result.add(currentShape);
             currentShape = null;
        }
        return result;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Shapes()
    {
        // Private constructor to prevent instantiation
    }
    
}