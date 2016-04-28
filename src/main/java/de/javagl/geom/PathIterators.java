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

import java.awt.Shape;
import java.awt.geom.PathIterator;

/**
 * Utility methods related to path iterators
 */
public class PathIterators
{
    /**
     * Creates a path iterator over a flattening path iterator of the given
     * shape, but ensures that no two consecutive points have a distance
     * that is larger than the given delta.<br>
     * <br>
     * Note that there is no constraint on the <i>minimum</i> distance between
     * the points that are returned. This may, however, be implicitly determined
     * by the given flatness. 
     * 
     * @param shape The shape
     * @param flatness The flatness for the iterator 
     * @param maxDelta The maximum distance between two points
     * @return The path iterator
     */
    public static PathIterator createDeltaPathIterator(
        Shape shape, double flatness, double maxDelta)
    {
        PathIterator delegate = shape.getPathIterator(null, flatness);
        return new DeltaPathIterator(delegate, maxDelta);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private PathIterators()
    {
        // Private constructor to prevent instantiation
    }
    
}
