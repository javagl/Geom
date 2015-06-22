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
import java.awt.geom.Point2D;

/**
 * Utility methods related to lines
 */
public class Lines
{
    /**
     * Returns the length of the given line
     * 
     * @param line The line
     * @return The length of the line
     */
    public static double length(Line2D line)
    {
        return Math.sqrt(lengthSquared(line));
    }
    
    /**
     * Returns the squared length of the given line
     * 
     * @param line The line
     * @return The squared length
     */
    public static double lengthSquared(Line2D line)
    {
        double x0 = line.getX1();
        double y0 = line.getY1();
        double x1 = line.getX2();
        double y1 = line.getY2();
        double dx = x1-x0;
        double dy = y1-y0;
        return dx * dx + dy * dy;
    }
    
    /**
     * Transform the given line with the given transform, and store the
     * result in the given destination. If the given destination is
     * <code>null</code>, then a new line will be created and returned
     * 
     * @param at The affine transform
     * @param lineSrc The source line
     * @param lineDst The destination line
     * @return The destination line
     */
    public static Line2D transform(
        AffineTransform at, Line2D lineSrc, Line2D lineDst)
    {
        if (lineDst == null) 
        {
            lineDst = new Line2D.Double();
        }
        double sx1 = lineSrc.getX1();
        double sy1 = lineSrc.getY1();
        double sx2 = lineSrc.getX2();
        double sy2 = lineSrc.getY2();
        double dx1 = AffineTransforms.computeX(at, sx1, sy1);
        double dy1 = AffineTransforms.computeY(at, sx1, sy1);
        double dx2 = AffineTransforms.computeX(at, sx2, sy2);
        double dy2 = AffineTransforms.computeY(at, sx2, sy2);
        lineDst.setLine(dx1, dy1, dx2, dy2);
        return lineDst;
    }
    
    /**
     * Scale the given line with the given factor, about its starting point,
     * and store the result in the given destination. If the given destination 
     * is <code>null</code>, then a new line will be created and returned
     * 
     * @param factor The scaling factor
     * @param lineSrc The source line
     * @param lineDst The destination line
     * @return The destination line
     */
    public static Line2D scale(double factor, Line2D lineSrc, Line2D lineDst)
    {
        if (lineDst == null) 
        {
            lineDst = new Line2D.Double();
        }
        double x0 = lineSrc.getX1();
        double y0 = lineSrc.getY1();
        double x1 = lineSrc.getX2();
        double y1 = lineSrc.getY2();
        double dx = x1-x0;
        double dy = y1-y0;
        double newX1 = x0 + dx * factor;
        double newY1 = y0 + dy * factor;
        lineDst.setLine(x0, y0, newX1, newY1);
        return lineDst;
    }
    
    /**
     * Normalize the given line to have unit length, by scaling it with its
     * inverse length about its starting point, and store the result 
     * in the given destination. If the given destination is
     * <code>null</code>, then a new line will be created and returned
     * 
     * @param lineSrc The source line
     * @param lineDst The destination line
     * @return The destination line
     */
    public static Line2D normalize(Line2D lineSrc, Line2D lineDst)
    {
        return scale(1.0 / length(lineSrc), lineSrc, lineDst);
    }

    /**
     * Scale the given line to have the given length about its starting
     * point, and store the result in the given destination. If the given 
     * destination is <code>null</code>, then a new line will be created 
     * and returned
     * 
     * @param length The length
     * @param lineSrc The source line
     * @param lineDst The destination line
     * @return The destination line
     */
    public static Line2D scaleToLength(
        double length, Line2D lineSrc, Line2D lineDst)
    {
        return scale(length / length(lineSrc), lineSrc, lineDst);
    }
    
    
    /**
     * Rotate the given source line around its starting point, by
     * the given angle, and stores the result in the given
     * destination line. If the destination line is <code>null</code>,
     * then a new line will be created and returned.<br>
     * <br>
     * This method is equivalent to 
     * <code>
     * Lines.transform(AffineTransform.getRotateInstance(angleRad, 
     *     lineSrc.getX1(), lineSrc.getY1()), lineSrc, lineDst);
     * </code>
     * but may be more efficient and convenient
     * 
     * @param angleRad The rotation angle
     * @param lineSrc The source line
     * @param lineDst The destination line
     * @return The destination line
     */
    public static Line2D rotate(
    	double angleRad, Line2D lineSrc, Line2D lineDst)
    {
        double x0 = lineSrc.getX1();
        double y0 = lineSrc.getY1();
        double x1 = lineSrc.getX2();
        double y1 = lineSrc.getY2();
        double dx = x1 - x0;
        double dy = y1 - y0;
        double sa = Math.sin(angleRad);
        double ca = Math.cos(angleRad);
        double nx = ca * dx - sa * dy;
        double ny = sa * dx + ca * dy;
        if (lineDst == null)
        {
        	lineDst = new Line2D.Double();
        }
        lineDst.setLine(x0, y0, x0+nx, y0+ny);
        return lineDst;
    }    

    
    /**
     * Computes the angle, in radians, between the given lines
     * 
     * @param line0 The first line
     * @param line1 The second line
     * @return The angle, in radians, that the line has to the x-axis
     */
    public static double angle(Line2D line0, Line2D line1)
    {
    	return angleToX(line1) - angleToX(line0);
    }
    
    /**
     * Computes the angle, in radians, that the line
     * has to the x axis
     * 
     * @param line The line
     * @return The angle, in radians, that the line has to the x-axis
     */
    public static double angleToX(Line2D line)
    {
    	return angleToX(
    	    line.getX1(), line.getY1(), 
    	    line.getX2(), line.getY2());
    }


    /**
     * Computes the angle, in radians, that the line from p0 to p1
     * has to the x axis
     * 
     * @param p0 The start point of the line
     * @param p1 The end point of the line
     * @return The angle, in radians, that the line has to the x-axis
     */
    public static double angleToX(
   		Point2D p0, Point2D p1)
    {
    	return angleToX(
    	    p0.getX(), p0.getY(), 
    	    p1.getX(), p1.getY());
    }

    /**
     * Computes the angle, in radians, that the line from (x0,y0) to (x1,y1) 
     * has to the x axis
     * 
     * @param x0 The x-coordinate of the start point of the line
     * @param y0 The y-coordinate of the start point of the line
     * @param x1 The x-coordinate of the end point of the line
     * @param y1 The y-coordinate of the end point of the line
     * @return The angle, in radians, that the line has to the x-axis
     */
    public static double angleToX(
   		double x0, double y0, double x1, double y1)
    {
    	double dx = x1 - x0;
        double dy = y1 - y0;
        double angleRad = Math.atan2(dy, dx); 
        return angleRad;
    }
    
    /**
     * Creates a simple string representation of the given line
     * 
     * @param line The line
     * @return The string
     */
    public static String toString(Line2D line)
    {
        return toString(line, "%f");
    }

    /**
     * Creates a simple string representation of the given line, using
     * the given format for the coordinates
     * 
     * @param line The line
     * @param format The format
     * @return The string
     */
    public static String toString(Line2D line, String format)
    {
        String sx1 = String.format(format, line.getX1());
        String sy1 = String.format(format, line.getY1());
        String sx2 = String.format(format, line.getX2());
        String sy2 = String.format(format, line.getY2());
        return "("+sx1+","+sy1+")-("+sx2+","+sy2+")";
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Lines()
    {
        // Private constructor to prevent instantiation
    }
    
}
