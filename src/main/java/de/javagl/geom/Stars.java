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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to create star shapes
 */
public class Stars
{
    /**
     * Create a star shape from the given parameters.
     * 
     * @param centerX The x coordinate of the center
     * @param centerY The y coordinate of the center
     * @param innerRadius The inner radius of the star
     * @param outerRadius The outer radius of the star
     * @param numRays The number of rays that the star should have
     * @return The star shape
     */
    public static Shape createStarShape(
        double centerX, double centerY,
        double innerRadius, double outerRadius, 
        int numRays)
    {
        return createStarShape(centerX, centerY, 
            innerRadius, outerRadius, 
            numRays, 0.0,  0.0,  0.0);
    }
    
    /**
     * Create a star shape from the given parameters.
     * 
     * @param centerX The x coordinate of the center
     * @param centerY The y coordinate of the center
     * @param innerRadius The inner radius of the star
     * @param outerRadius The outer radius of the star
     * @param numRays The number of rays that the star should have
     * @param startAngleRad The angle, in radians, where the
     * first ray should start (referring to the positive x-axis)
     * @return The star shape
     */
    public static Shape createStarShape(
        double centerX, double centerY,
        double innerRadius, double outerRadius, 
        int numRays, double startAngleRad)
    {
        return createStarShape(centerX, centerY, 
            innerRadius, outerRadius, 
            numRays, startAngleRad,  0.0,  0.0);
    }
    
    /**
     * Create a star shape from the given parameters.
     * 
     * @param centerX The x coordinate of the center
     * @param centerY The y coordinate of the center
     * @param innerRadius The inner radius of the star
     * @param outerRadius The outer radius of the star
     * @param numRays The number of rays that the star should have
     * @param startAngleRad The angle, in radians, where the
     * first ray should start (referring to the positive x-axis)
     * @param innerRoundness A roundness value between 0.0 and
     * 1.0, for the inner corners of the star.
     * @param outerRoundness A roundness value between 0.0 and
     * 1.0, for the outer corners (ray tips) of the star.
     * @return The star shape
     */
    public static Shape createStarShape(
        double centerX, double centerY,
        double innerRadius, double outerRadius, 
        int numRays, double startAngleRad,
        double innerRoundness, double outerRoundness)
    {
        if (numRays < 2)
        {
            throw new IllegalArgumentException(
                "The number of rays must be at least 2, but is " + numRays);
        }
        
        // Create the list containing the inner and outer tip points
        List<Point2D> points = new ArrayList<Point2D>();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++)
        {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
            double sa = Math.sin(angleRad);
            double relX = ca;
            double relY = sa;
            if ((i & 1) == 0)
            {
                relX *= outerRadius;
                relY *= outerRadius;
            }
            else
            {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            Point2D p = new Point2D.Double(centerX + relX, centerY + relY);
            points.add(p);
        }
        
        // Create a path based on the inner and outer tip points,
        // based on the roundness values
        Point2D prevCenter = new Point2D.Double();
        Point2D nextCenter = new Point2D.Double();
        Point2D step0 = new Point2D.Double();
        Point2D step1 = new Point2D.Double();
        Path2D path = new Path2D.Double();
        for (int i = 0; i < numRays * 2; i++)
        {
            // For the current point, compute the previous and next point
            int iPrev = (i - 1 + points.size()) % points.size();
            int iCurr = i;
            int iNext = (i + 1) % points.size();
            Point2D pPrev = points.get(iPrev);
            Point2D pCurr = points.get(iCurr);
            Point2D pNext = points.get(iNext);
            
            // Compute the center between the previous and the current
            // point, and between the current and the next
            Points.interpolate(pPrev, pCurr, 0.5, prevCenter);
            Points.interpolate(pCurr, pNext, 0.5, nextCenter);

            if (i == 0)
            {
                path.moveTo(prevCenter.getX(), prevCenter.getY());
            }
            
            // Pick the roundness, depending on whether the current
            // point is an inner or an outer point
            double roundness = innerRoundness;
            if ((i & 1) == 0)
            {
                roundness = outerRoundness;
            }
            
            if (Math.abs(roundness) < Geom.DOUBLE_EPSILON)
            {
                // For a roundness of 0.0, just walk from the previous center 
                // to the current point, and then to the next center
                path.lineTo(pCurr.getX(), pCurr.getY());
                path.lineTo(nextCenter.getX(), nextCenter.getY());
            }
            else if (Math.abs(roundness - 1.0) < Geom.DOUBLE_EPSILON)
            {
                // For a roundness of 1.0, just draw a quad from the 
                // previous center to the next, using the current 
                // point as the control point
                path.quadTo(
                    pCurr.getX(), pCurr.getY(),
                    nextCenter.getX(), nextCenter.getY());
            }
            else
            {
                // Compute interpolated points on the segment between
                // the previous center and the current point, and the
                // current point and the next center, based on the
                // roundness
                Points.interpolate(prevCenter, pCurr, 1.0 - roundness, step0);
                Points.interpolate(pCurr, nextCenter, roundness, step1);

                // Connect the interpolated points using a quad
                path.lineTo(step0.getX(), step0.getY());
                path.quadTo(
                    pCurr.getX(), pCurr.getY(),
                    step1.getX(), step1.getY());
                path.lineTo(nextCenter.getX(), nextCenter.getY());
            }
        }
        path.closePath();
        return path;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Stars()
    {
        // Private constructor to prevent instantiation
    }

}
