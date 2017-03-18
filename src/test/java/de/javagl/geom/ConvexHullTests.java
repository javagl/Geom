package de.javagl.geom;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * A regression test for the {@link ConvexHull} class
 */
@SuppressWarnings("javadoc")
public class ConvexHullTests
{
    public static void main(String[] args)
    {
        List<Point2D> points = Arrays.asList(
            new Point2D.Double(100.0, 200.0),
            new Point2D.Double( 50.0, 200.0),
            new Point2D.Double(250.0, 250.0),
            new Point2D.Double(300.0, 300.0));
        ConvexHull.compute(points);
    }
}
