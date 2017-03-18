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

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Package-private utilities
 */
class Geom 
{
    /**
     * Epsilon for floating point computations
     */
    static final double DOUBLE_EPSILON = 1e-8f;

    /**
     * Utility method for debugging: Print the code for creating a list
     * containing the given points.
     * 
     * @param name The name of the list
     * @param points The points
     */
    @Deprecated
    static void debugPrintCreation(
        String name, List<? extends Point2D> points)
    {
        System.out.println("List<Point2D> " + name + " = Arrays.asList(");
        for (int i = 0; i < points.size(); i++)
        {
            Point2D p = points.get(i);
            if (i > 0)
            {
                System.out.println(",");
            }
            System.out.print("    new Point2D.Double(" 
                + p.getX() + ", " + p.getY() + ")");
        }
        System.out.println(");");
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Geom()
    {
        // Private constructor to prevent instantiation
    }

}
