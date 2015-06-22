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
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * A class for creating shapes of arrows
 */
public final class ArrowCreator
{
    /**
     * The length of the head of the arrow. This may either be
     * a relative length (referring to the total length of the
     * arrow) when {@link #relativeHeadLength} is <code>true</code>,
     * or an absolute length, when {@link #relativeHeadLength} 
     * is <code>false</code>,
     */
    private double headLength;
    
    /**
     * Whether the {@link #headLength} is relative (referring to 
     * the length of the arrow)
     */
    private boolean relativeHeadLength;

    /**
     * The minimum absolute head length
     */
    private double minHeadLength;
    
    /**
     * The maximum absolute head length
     */
    private double maxHeadLength;

    /**
     * The width of the head of the arrow. This may either be
     * a relative width (referring to the total length of the
     * arrow) when {@link #relativeHeadWidth} is <code>true</code>,
     * or an absolute width, when {@link #relativeHeadWidth} 
     * is <code>false</code>,
     */
    private double headWidth;

    /**
     * Whether the {@link #headWidth} is relative (referring to 
     * the length of the arrow)
     */
    private boolean relativeHeadWidth;
    
    /**
     * The minimum absolute head width
     */
    private double minHeadWidth;
    
    /**
     * The maximum absolute head width
     */
    private double maxHeadWidth;
    
    /**
     * The position of the "base" of the head. This is a value 
     * between 0 and 1. When it is 0, then the base will be at 
     * the same position as the tips of the head. If it is 1, 
     * then the base will be at the arrow end  
     */
    private double headBasePosition;

    /**
     * Default constructor
     */
    ArrowCreator()
    {
        setRelativeHeadLength(0.2);
        setRelativeHeadWidth(0.15);
        setHeadBasePosition(0.15);
    }
    
    /**
     * Set the absolute length of the arrow head
     * 
     * @param headLength The head length
     * @return This instance
     */
    public ArrowCreator setAbsoluteHeadLength(double headLength)
    {
        this.headLength = headLength;
        this.minHeadLength = headLength;
        this.maxHeadLength = headLength;
        this.relativeHeadLength = false;
        return this;
    }
    
    /**
     * Set the head length, relative to the length of the arrow,
     * usually as a value between 0 and 1
     * 
     * @param headLength The head length
     * @return This instance
     */
    public ArrowCreator setRelativeHeadLength(double headLength)
    {
        return setRelativeHeadLength(headLength, 0, Double.MAX_VALUE);
    }

    /**
     * Set the head length, relative to the length of the arrow,
     * usually as a value between 0 and 1, but with the given 
     * absolute bounds
     * 
     * @param headLength The head length
     * @param minAbsolute The minimum head length
     * @param maxAbsolute The maximum head length
     * @return This instance
     */
    public ArrowCreator setRelativeHeadLength(
        double headLength, double minAbsolute, double maxAbsolute)
    {
        this.headLength = headLength;
        this.minHeadLength = minAbsolute;
        this.maxHeadLength = maxAbsolute;
        this.relativeHeadLength = true;
        return this;
    }
    
    /**
     * Set the absolute width of the arrow head
     * 
     * @param headWidth The head width
     * @return This instance
     */
    public ArrowCreator setAbsoluteHeadWidth(double headWidth)
    {
        this.headWidth = headWidth;
        this.minHeadWidth = headWidth;
        this.maxHeadWidth = headWidth;
        this.relativeHeadWidth = false;
        return this;
    }

    /**
     * Set the head width, relative to the length of the arrow,
     * usually as a value between 0 and 1
     * 
     * @param headWidth The head width
     * @return This instance
     */
    public ArrowCreator setRelativeHeadWidth(double headWidth)
    {
        return setRelativeHeadWidth(headWidth, 0, Double.MAX_VALUE);
    }

    /**
     * Set the head width, relative to the width of the arrow,
     * usually as a value between 0 and 1, but with the given 
     * absolute bounds
     * 
     * @param headWidth The head width
     * @param minAbsolute The minimum head width
     * @param maxAbsolute The maximum head width
     * @return This instance
     */
    public ArrowCreator setRelativeHeadWidth(
        double headWidth, double minAbsolute, double maxAbsolute)
    {
        this.headWidth = headWidth;
        this.minHeadWidth = minAbsolute;
        this.maxHeadWidth = maxAbsolute;
        this.relativeHeadWidth = true;
        return this;
    }
    
    /**
     * Set the position of the head base, as a value between 0 and 1.
     * In this image, "B" represents the base position, between "0" and
     * "1":
     * <pre><code>
     *               __
     *               \ \__ 
     *                \   \__
     *                 \     \_
     * --------------0--B-----1
     *                 /   __/
     *                / __/
     *               /_/   
     * </code></pre>
     *                 
     * 
     * @param headBasePosition The head base position
     * @return This instance
     */
    public ArrowCreator setHeadBasePosition(double headBasePosition)
    {
        this.headBasePosition = headBasePosition;
        return this;
    }
    
    
    /**
     * Compute the actual head length, based on the absolute/relative
     * head length and the minimum/maximum head length
     * 
     * @param arrowLength The length of the arrow
     * @return The head length
     */
    private double computeActualHeadLength(double arrowLength)
    {
        double actualHeadLength = headLength;
        if (relativeHeadLength)
        {
            actualHeadLength *= arrowLength;
        }
        actualHeadLength = Math.max(minHeadLength, actualHeadLength);
        actualHeadLength = Math.min(maxHeadLength, actualHeadLength);
        return actualHeadLength;
    }
    
    /**
     * Compute the actual head width, based on the absolute/relative
     * head width and the minimum/maximum head width
     * 
     * @param arrowLength The length of the arrow
     * @return The head width
     */
    private double computeActualHeadWidth(double arrowLength)
    {
        double actualHeadWidth = headWidth;
        if (relativeHeadWidth)
        {
            actualHeadWidth *= arrowLength;
        }
        actualHeadWidth = Math.max(minHeadWidth, actualHeadWidth);
        actualHeadWidth = Math.min(maxHeadWidth, actualHeadWidth);
        return actualHeadWidth;
    }

    /**
     * Build the shape of an arrow, based on the current settings
     * of this arrow creator
     * 
     * @param line The line for the arrow
     * @param shaftWidth The width of the shaft
     * @return The arrow shape
     */
    public Shape buildShape(
        Line2D line, double shaftWidth)
    {
        return buildShape(
            line.getY1(), line.getY1(), line.getX2(), line.getY2(), 
            shaftWidth, true);
    }
    
    /**
     * Build the shape of an arrow, based on the current settings
     * of this arrow creator
     * 
     * @param p0 The start point of the arrow
     * @param p1 The end point of the arrow
     * @param shaftWidth The width of the shaft
     * @return The arrow shape
     */
    public Shape buildShape(
        Point2D p0, Point2D p1, double shaftWidth)
    {
        return buildShape(p0.getX(), p0.getY(), p1.getX(), p1.getY(), 
            shaftWidth, true);
    }
    
    /**
     * Build the shape of an arrow, based on the current settings
     * of this arrow creator
     * 
     * @param x0 The x-coordinate of the start point of the arrow
     * @param y0 The y-coordinate of the start point of the arrow
     * @param x1 The x-coordinate of the end point of the arrow
     * @param y1 The y-coordinate of the end point of the arrow 
     * @param shaftWidth The width of the shaft
     * @return The arrow shape
     */
    public Shape buildShape(
        double x0, double y0, double x1, double y1, double shaftWidth)
    {
        return buildShape(x0, y0, x1, y1, shaftWidth, true);
    }
    
    /**
     * Build the shape of an arrow head, based on the current settings
     * of this arrow creator
     * 
     * @param x0 The x-coordinate of the start point of the arrow
     * @param y0 The y-coordinate of the start point of the arrow
     * @param x1 The x-coordinate of the end point of the arrow
     * @param y1 The y-coordinate of the end point of the arrow 
     * @param shaftWidth The width of the shaft
     * @return The arrow head shape
     */
    public Shape buildHeadShape(
        double x0, double y0, double x1, double y1, double shaftWidth)
    {
        return buildShape(x0, y0, x1, y1, shaftWidth, false);
    }

    /**
     * Build the shape of an arrow, based on the current settings
     * of this arrow creator
     * <pre><code>
     *               TL__
     *                 \ \__ 
     *                  \   \__
     *                   \     \_
     * SL-----------------BL     \
     *  |                         (x1,y1)
     * SR-----------------BR     /
     *                   /   __/
     *                  / __/
     *               TR/_/
     *              
     * TL : headTipL 
     * TR : headTipR
     * BL : headBaseL              
     * BR : headBaseR              
     * SL : shaftBaseL              
     * SR : shaftBaseR              
     * </code></pre>
     * 
     * 
     * @param x0 The x-coordinate of the start point of the arrow
     * @param y0 The y-coordinate of the start point of the arrow
     * @param x1 The x-coordinate of the end point of the arrow
     * @param y1 The y-coordinate of the end point of the arrow 
     * @param shaftWidth The width of the shaft
     * @param includeShaft Whether the shaft should be part of the shape
     * @return The shape
     */
    private Shape buildShape(
        double x0, double y0, double x1, double y1, 
        double shaftWidth, boolean includeShaft)
    {
        // To do: Avoid code duplication here and in buildLines

        double dx = x1-x0;
        double dy = y1-y0;
        double length = Math.sqrt(dx*dx+dy*dy);
        double invLength = 1.0 / length;
        double dirX = dx * invLength;
        double dirY = dy * invLength;
        double perpDirX = dirY;
        double perpDirY = -dirX;
        double perpDirOffsetX = perpDirX * 0.5;
        double perpDirOffsetY = perpDirY * 0.5;
        
        double actualHeadLength = computeActualHeadLength(length);
        double actualHeadWidth = computeActualHeadWidth(length);
        
        double headTipsX = x1 - actualHeadLength * dirX;
        double headTipsY = y1 - actualHeadLength * dirY;
        double headTipOffsetX = actualHeadWidth * perpDirOffsetX;
        double headTipOffsetY = actualHeadWidth * perpDirOffsetY;
        double headTipLx = headTipsX + headTipOffsetX;
        double headTipLy = headTipsY + headTipOffsetY;
        double headTipRx = headTipsX - headTipOffsetX;
        double headTipRy = headTipsY - headTipOffsetY;

        double headBaseOffsetFromEnd = 
            (actualHeadLength * (1-headBasePosition));
        double headBaseX = x1 - headBaseOffsetFromEnd * dirX;
        double headBaseY = y1 - headBaseOffsetFromEnd * dirY;
        
        double shaftWidthOffsetX = shaftWidth * perpDirOffsetX;
        double shaftWidthOffsetY = shaftWidth * perpDirOffsetY;
        
        double headBaseLx = headBaseX + shaftWidthOffsetX;
        double headBaseLy = headBaseY + shaftWidthOffsetY;
        double headBaseRx = headBaseX - shaftWidthOffsetX;
        double headBaseRy = headBaseY - shaftWidthOffsetY;

        double shaftBaseLx = x0 + shaftWidthOffsetX;
        double shaftBaseLy = y0 + shaftWidthOffsetY;
        double shaftBaseRx = x0 - shaftWidthOffsetX;
        double shaftBaseRy = y0 - shaftWidthOffsetY;

        Path2D p = new Path2D.Double();
        if (includeShaft)
        {
            p.moveTo(shaftBaseLx, shaftBaseLy);
            p.lineTo(headBaseLx, headBaseLy);
        }
        else
        {
            p.moveTo(headBaseLx, headBaseLy);
        }
        p.lineTo(headBaseLx, headBaseLy);
        p.lineTo(headTipLx, headTipLy);
        p.lineTo(x1, y1);
        p.lineTo(headTipRx, headTipRy);
        p.lineTo(headBaseRx, headBaseRy);
        if (includeShaft)
        {
            p.lineTo(shaftBaseRx, shaftBaseRy);
        }
        p.closePath();
        return p;
    }
    
    /**
     * Build a simple arrow that only consists of lines, from the start
     * to the end, and from the head tips to the end.
     * 
     * @param line The line for the arrow
     * @return The shape, only consisting of lines
     */
    public Shape buildLines(Line2D line)
    {
        return buildLines(
            line.getY1(), line.getY1(), line.getX2(), line.getY2());
    }

    /**
     * Build a simple arrow that only consists of lines, from the start
     * to the end, and from the head tips to the end.
     * 
     * @param p0 The start point of the arrow
     * @param p1 The end point of the arrow
     * @return The shape, only consisting of lines
     */
    public Shape buildLines(
        Point2D p0, Point2D p1)
    {
        return buildLines(p0.getX(), p0.getY(), p1.getX(), p1.getY());
    }

    /**
     * Build a simple arrow that only consists of lines, from the start
     * to the end, and from the head tips to the end.
     * 
     * @param x0 The x-coordinate of the start point of the arrow
     * @param y0 The y-coordinate of the start point of the arrow
     * @param x1 The x-coordinate of the end point of the arrow
     * @param y1 The y-coordinate of the end point of the arrow 
     * @return The shape, only consisting of lines
     */
    public Shape buildLines(
        double x0, double y0, double x1, double y1)
    {
        // To do: Avoid code duplication here and in buildShape
        
        double dx = x1-x0;
        double dy = y1-y0;
        double length = Math.sqrt(dx*dx+dy*dy);
        double invLength = 1.0 / length;
        double dirX = dx * invLength;
        double dirY = dy * invLength;
        double perpDirX = dirY;
        double perpDirY = -dirX;
        double perpDirOffsetX = perpDirX * 0.5;
        double perpDirOffsetY = perpDirY * 0.5;
        
        double actualHeadLength = computeActualHeadLength(length);
        double actualHeadWidth = computeActualHeadWidth(length);
        
        double headTipsX = x1 - actualHeadLength * dirX;
        double headTipsY = y1 - actualHeadLength * dirY;
        double headTipOffsetX = actualHeadWidth * perpDirOffsetX;
        double headTipOffsetY = actualHeadWidth * perpDirOffsetY;
        double headTipLx = headTipsX + headTipOffsetX;
        double headTipLy = headTipsY + headTipOffsetY;
        double headTipRx = headTipsX - headTipOffsetX;
        double headTipRy = headTipsY - headTipOffsetY;

        Path2D p = new Path2D.Double();
        p.moveTo(x0, y0);
        p.lineTo(x1, y1);
        p.moveTo(headTipLx, headTipLy);
        p.lineTo(x1, y1);
        p.moveTo(headTipRx, headTipRy);
        p.lineTo(x1, y1);
        return p;
    }
    
    
    

}
