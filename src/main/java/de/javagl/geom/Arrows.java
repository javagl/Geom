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

/**
 * Methods to create shapes that represent arrows
 */
public class Arrows
{
    /**
     * Create a new {@link ArrowCreator} that allows setting different
     * properties of the arrows, and creating the shapes of the arrows.
     * The initial settings of the {@link ArrowCreator} are unspecified.
     * However, it can be assumed that the head length and width of the 
     * arrows are "reasonable" and relative to the length of the arrow.
     * 
     * @return The {@link ArrowCreator}
     */
    public static ArrowCreator create()
    {
        return new ArrowCreator();
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Arrows()
    {
        // Private constructor to prevent instantiation
    }
}
