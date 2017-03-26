package de.javagl.geom;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * A regression test for the {@link ConvexHull} class
 */
@SuppressWarnings("javadoc")
public class ConvexHullTests
{
    @Test
    public void testConvexHullWhenFirstPointsHaveEqualY()
    {
        List<Point2D> points = Arrays.asList(
            new Point2D.Double(100.0, 200.0),
            new Point2D.Double( 50.0, 200.0),
            new Point2D.Double(250.0, 250.0),
            new Point2D.Double(300.0, 300.0));
        ConvexHull.compute(points);
    }
}
