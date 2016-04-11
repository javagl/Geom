package de.javagl.geom;

import static org.junit.Assert.assertEquals;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ShapesAreaTest
{
    @Test
    public void testBasic()
    {
        final double epsilon = 1e-8;
        final double flatness = 1.0;
        Shape s0 = new Rectangle2D.Double(-100, 50, 200, 100);
        double area = Shapes.computeSignedArea(s0, flatness);
        assertEquals(200*100, area, epsilon);
    }

    @Test
    public void testNegative()
    {
        final double epsilon = 1e-8;
        final double flatness = 1.0;
        
        Path2D s0 = new Path2D.Double();
        s0.moveTo(-100, 50);
        s0.lineTo(100, 50);
        s0.lineTo(100, -50);
        s0.lineTo(-100, -50);
        s0.closePath();
        
        double area = Shapes.computeSignedArea(s0, flatness);
        assertEquals(-200*100, area, epsilon);
    }

    @Test
    public void testMultipleRegions()
    {
        final double epsilon = 1e-8;
        final double flatness = 1.0;
        Shape s0 = new Rectangle2D.Double(-100, 50, 200, 100);
        Shape s1 = new Rectangle2D.Double(-50,   0, 100, 200);
        Area a0 = new Area(s0);
        Area a1 = new Area(s1);
        a0.subtract(a1);
        double area = Math.abs(Shapes.computeSignedArea(a0, flatness));
        assertEquals(50*100+50*100, area, epsilon);
    }
}
