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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods related to oriented bounding boxes
 */
public class OrientedBoundingBoxes 
{
	/**
	 * Computes the corners of the minimum oriented bounding box of the
	 * given set of points
	 * 
	 * @param points The input points
	 * @return The corners of the minimum oriented bounding box
	 */
	public static List<Point2D> computeMinimumOrientedBoundingBoxCorners(
		List<? extends Point2D> points)
	{
		List<Point2D> convexHullPoints = ConvexHull.compute(points);
		
		// Compute the points that describe the edge of the convex
		// hull that coincides with one edge of the minimum 
		// oriented bounding box
		int alignmentPointIndex = 
			computeAlignmentPointIndex(convexHullPoints);
		int nextIndex = (alignmentPointIndex+1)%convexHullPoints.size();
		Point2D p0 = convexHullPoints.get(alignmentPointIndex);
		Point2D p1 = convexHullPoints.get(nextIndex);
		
		// Compute the corners of the minimum oriented bounding
		// box when the edge is aligned with the x-axis
		AffineTransform at = computeAlignmentTransform(p0, p1);
		List<Point2D> transformedPoints = Points.transform(at, points);
		Rectangle2D r = Points.computeBounds(transformedPoints);
		List<Point2D> alignedCorners = new ArrayList<Point2D>();
		alignedCorners.add(new Point2D.Double(r.getMinX(), r.getMinY()));
		alignedCorners.add(new Point2D.Double(r.getMaxX(), r.getMinY()));
		alignedCorners.add(new Point2D.Double(r.getMaxX(), r.getMaxY()));
		alignedCorners.add(new Point2D.Double(r.getMinX(), r.getMaxY()));
		
		// Transform the corners back into the original space
		AffineTransform iat = computeInverseAlignmentTransform(p0, p1);
		List<Point2D> corners = Points.transform(iat, alignedCorners);
		return corners;
	}

	/**
	 * Computes the start point of the edge that determines the
	 * direction of one edge of the minimum oriented bounding 
	 * box.
	 * 
	 * @param points The input points
	 * @return The alignment point index
	 */
	private static int computeAlignmentPointIndex(
		List<Point2D> points)
	{
		double minArea = Double.MAX_VALUE;
		int minAreaIndex = -1;
		for (int i=0; i<points.size(); i++)
		{
			Point2D p0 = points.get(i);
			Point2D p1 = points.get((i+1)%points.size());
			AffineTransform at = computeAlignmentTransform(p0, p1);
			List<Point2D> transformedPoints = Points.transform(at, points);
			Rectangle2D bounds = Points.computeBounds(transformedPoints);
			double area = bounds.getWidth() * bounds.getHeight();
			if (area < minArea)
			{
				minArea = area;
				minAreaIndex = i;
			}
		}
		return minAreaIndex;
	}

	
	/**
	 * Compute the transform that moves point p0 to the origin,
	 * and aligns the line from p0 to p1 with the x-axis
	 * 
	 * @param p0 The first point
	 * @param p1 The second point
	 * @return The alignment transform
	 */
	private static AffineTransform computeAlignmentTransform(
		Point2D p0, Point2D p1)
	{
		double angleRad = Lines.angleToX(p0, p1);
		AffineTransform at = 
			AffineTransform.getRotateInstance(-angleRad);
		at.concatenate(AffineTransform.getTranslateInstance(
			-p0.getX(), -p0.getY()));
		return at;
	}

	/**
	 * Compute the transform that moves the origin to p0,
	 * and aligns the x-axis with the line from p0 to p1 
	 * 
	 * @param p0 The first point
	 * @param p1 The second point
	 * @return The alignment transform
	 */
	private static AffineTransform computeInverseAlignmentTransform(
		Point2D p0, Point2D p1)
	{
		double angleRad = Lines.angleToX(p0, p1);
		AffineTransform at = 
			AffineTransform.getTranslateInstance(p0.getX(), p0.getY());
		at.concatenate(AffineTransform.getRotateInstance(angleRad));
		return at;
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private OrientedBoundingBoxes()
	{
		// Private constructor to prevent instantiation
	}


}


