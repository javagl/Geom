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

import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * A path iterator that iterates over a delegate, ensuring that the maximum
 * distance between two consecutive points is not larger than a certain
 * delta.
 */
class DeltaPathIterator implements PathIterator
{
    /**
     * The delegate path iterator
     */
    private final PathIterator delegate;
    
    /**
     * The maximum distance that two points may have
     */
    private final double maxDelta;

    /**
     * Whether this iterator is done
     */
    private boolean done;

    /**
     * Internal storage for the coordinates provided by the delegate
     */
    private final double delegateCoords[] = new double[6];
    
    /**
     * The coordinates that will be used for the next call to 
     * <code>currentSegment</code>
     */
    private final double currentCoords[] = new double[6];
    
    /**
     * The return value for the next call to <code>currentSegment</code>
     */
    private int currentSegment;
    
    /**
     * The previous position that the delegate moved to with a SEG_MOVE
     */
    private final Point2D.Double prevDelegateMove = new Point2D.Double();
    
    /**
     * The previous point that was provided by the delegate
     */
    private final Point2D.Double prevDelegatePoint = new Point2D.Double();
    
    /**
     * The next point that was provided by the delegate, and which
     * this iterator will head for (in steps that are smaller than
     * the maxDelta)
     */
    private final Point2D.Double nextDelegatePoint = new Point2D.Double();
    
    /**
     * The length of the current segment. That is, the distance between
     * the prevDelegatePoint and the nextDelegatePoint
     */
    private double currentSegmentLength;

    /**
     * The current segment delta in x-direction
     */
    private double currentSegmentDx;

    /**
     * The current segment delta in y-direction
     */
    private double currentSegmentDy;
    
    /**
     * The current delta (step size) that is used for interpolating between
     * the prevDelegatePoint and the nextDelegatePoint 
     */
    private double currentDelta;

    /**
     * The current position on the current segment, starting at 0.0,
     * and increased by the currentDelta in each step, until it 
     * reaches the currentSegmentLength
     */
    private double currentSegmentPosition;
    
    /**
     * Whether the current segment was finished and a new segment
     * should be started (if the delegate provides further points)
     */
    private boolean currentSegmentFinished;
    
    /**
     * Whether the last step in the iteration should be a SEG_CLOSE
     */
    private boolean closeAtEnd;
    
    /**
     * Creates a delta path iterator with the given delegate and the
     * given maximum step size.<br>
     * <br>
     * It is assumed that the given delegate is a <b>flattened</b> path
     * iterator. That is, it only provides <code>SEG_MOVETO</code>, 
     * <code>SEG_LINETO</code> and <code>SEG_CLOSE</code> segments.
     * 
     * @param delegate The delegate
     * @param maxDelta The maximum delta
     */
    DeltaPathIterator(PathIterator delegate, double maxDelta)
    {
        this.delegate = delegate;
        this.maxDelta = maxDelta;
        this.done = false;
        this.currentSegmentFinished = false;
        this.closeAtEnd = false;
        next();
    }
    
    @Override
    public int getWindingRule()
    {
        return delegate.getWindingRule();
    }

    @Override
    public boolean isDone()
    {
        return done;
    }

    @Override
    public void next()
    {
        Arrays.fill(currentCoords, 0.0);
        if (done)
        {
            return;
        }
        if (currentSegmentFinished)
        {
            closeAtEnd = false;
            if (delegate.isDone())
            {
                done = true;
                return;
            }
            int delegateSegment = delegate.currentSegment(delegateCoords);
            delegate.next();
            
            // If the delegate provides a SEG_MOVETO, this will essentially
            // be delivered by this iterator as well
            if (delegateSegment == PathIterator.SEG_MOVETO)
            {
                currentSegment = PathIterator.SEG_MOVETO;
                currentCoords[0] = delegateCoords[0];
                currentCoords[1] = delegateCoords[1];
                prevDelegatePoint.x = delegateCoords[0];
                prevDelegatePoint.y = delegateCoords[1];
                nextDelegatePoint.x = delegateCoords[0];
                nextDelegatePoint.y = delegateCoords[1];
                prevDelegateMove.x = delegateCoords[0];
                prevDelegateMove.y = delegateCoords[1];
                return;
            }
            
            // The delegate describes a line segment, either via a 
            // SEG_CLOSE or via a SEG_LINETO. Store the coordinates
            // of this line to interpolate between
            if (delegateSegment == PathIterator.SEG_CLOSE)
            {
                prevDelegatePoint.x = nextDelegatePoint.x;
                prevDelegatePoint.y = nextDelegatePoint.y;
                nextDelegatePoint.x = prevDelegateMove.x;
                nextDelegatePoint.y = prevDelegateMove.y;
                closeAtEnd = true;
            }
            else if (delegateSegment == PathIterator.SEG_LINETO)
            {
                prevDelegatePoint.x = nextDelegatePoint.x;
                prevDelegatePoint.y = nextDelegatePoint.y;
                nextDelegatePoint.x = delegateCoords[0];
                nextDelegatePoint.y = delegateCoords[1];
            }
            else
            {
                // This should never happen, according to the contract of
                // the constructor, but checked here nevertheless
                throw new IllegalStateException(
                    "Found invalid segment type in flattened path iterator");
            }
            
            // If the current segment is longer than the maximum delta, 
            // then interpolate it in multiple steps
            currentSegmentLength = 
                prevDelegatePoint.distance(nextDelegatePoint);
            currentDelta = maxDelta;
            if (currentSegmentLength > Geom.DOUBLE_EPSILON)
            {
                int steps = (int)Math.ceil(currentSegmentLength / maxDelta);
                currentDelta = currentSegmentLength / steps;
            }
            currentSegmentDx = nextDelegatePoint.x - prevDelegatePoint.x;
            currentSegmentDy = nextDelegatePoint.y - prevDelegatePoint.y;
            currentSegmentPosition = 0;
            currentSegmentFinished = false;
        }
        
        // Advance by the currentDelta, and provide the information to
        // be returned for the next currentSegment call
        currentSegmentPosition += currentDelta;
        if (currentSegmentPosition >= 
            currentSegmentLength - Geom.DOUBLE_EPSILON)
        {
            currentCoords[0] = nextDelegatePoint.x;
            currentCoords[1] = nextDelegatePoint.y;
            if (closeAtEnd)
            {
                currentSegment = PathIterator.SEG_CLOSE;
            }
            else
            {
                currentSegment = PathIterator.SEG_LINETO;
            }
            currentSegmentFinished = true;
            return;
        }
        double relativeLocation = currentSegmentPosition / currentSegmentLength;
        double x = prevDelegatePoint.x + relativeLocation * currentSegmentDx;
        double y = prevDelegatePoint.y + relativeLocation * currentSegmentDy;
        currentCoords[0] = x;
        currentCoords[1] = y;
        currentSegment = PathIterator.SEG_LINETO;
    }

    @Override
    public int currentSegment(float coords[])
    {
        for (int i=0; i<coords.length; i++)
        {
            coords[i] = (float) currentCoords[i];
        }
        return currentSegment;
    }

    @Override
    public int currentSegment(double coords[])
    {
        for (int i=0; i<coords.length; i++)
        {
            coords[i] = currentCoords[i];
        }
        return currentSegment;
    }
}