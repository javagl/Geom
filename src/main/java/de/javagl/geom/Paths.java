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

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * Utility methods related to paths
 */
public class Paths
{
    /**
     * Creates a new path that consists of line segments between the
     * given points
     * 
     * @param points The points
     * @param close Whether the path should be closed
     * @return The path
     */
    public static Path2D fromPoints(
        Iterable<? extends Point2D> points, boolean close)
    {
        Path2D path = new Path2D.Double();
        Iterator<? extends Point2D> iterator = points.iterator();
        boolean hasPoints = false;
        if (iterator.hasNext())
        {
            Point2D point = iterator.next();
            path.moveTo(point.getX(), point.getY());
            hasPoints = true;
        }
        while (iterator.hasNext())
        {
            Point2D point = iterator.next();
            path.lineTo(point.getX(), point.getY());
        }
        if (close && hasPoints)
        {
            path.closePath();
        }
        return path;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Paths()
    {
        // Private constructor to prevent instantiation
    }
    
}
