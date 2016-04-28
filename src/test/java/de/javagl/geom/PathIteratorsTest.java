package de.javagl.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link PathIterators} class
 */
@SuppressWarnings({"javadoc"})
public class PathIteratorsTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new PathIteratorsTestPanel());
        f.setSize(1000,1000);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class PathIteratorsTestPanel extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private final List<Point2D> points;
    private Point2D draggedPoint = null;

    PathIteratorsTestPanel()
    {
        points = new ArrayList<Point2D>();
        points.add(new Point2D.Double(100,200));
        points.add(new Point2D.Double(300,400));
        points.add(new Point2D.Double(500,600));
        points.add(new Point2D.Double(600,100));
        points.add(new Point2D.Double(700,100));
        points.add(new Point2D.Double(900,800));
        points.add(new Point2D.Double(900,900));
        points.add(new Point2D.Double(300,900));
        points.add(new Point2D.Double(400,900));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,  
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        
        
        Path2D p = new Path2D.Double();
        p.moveTo(points.get(0).getX(), points.get(0).getY());
        p.lineTo(points.get(1).getX(), points.get(1).getY());
        //p.lineTo(points.get(1).getX(), points.get(1).getY()); // Test: Twice
        p.moveTo(points.get(2).getX(), points.get(2).getY());
        p.curveTo(
            points.get(3).getX(), points.get(3).getY(),
            points.get(4).getX(), points.get(4).getY(),
            points.get(5).getX(), points.get(5).getY());
        p.lineTo(points.get(6).getX(), points.get(6).getY());
        p.closePath();
        p.moveTo(points.get(7).getX(), points.get(7).getY());
        p.lineTo(points.get(8).getX(), points.get(8).getY());

        Shape s = p;
        double flatness = 3.0;
        double maxDelta = 30.0;
        
        g.setColor(Color.BLUE);
        g.draw(s);

        g.setColor(Color.GRAY);
        drawPoints(g, Shapes.computePoints(s, flatness, false), 6);
        
        g.setColor(Color.BLACK);
        drawPoints(g, points, 5);
        
        PathIterator pi = 
            PathIterators.createDeltaPathIterator(s, flatness, maxDelta);
        double coords[] = new double[6];
        while (!pi.isDone())
        {
            switch (pi.currentSegment(coords))
            {
                case PathIterator.SEG_MOVETO:
                    System.out.println("SEG_MOVETO  "+Arrays.toString(coords));
                    g.setColor(Color.RED);
                    drawPoint(g, coords[0], coords[1], 3);
                    break;
                    
                case PathIterator.SEG_LINETO:
                    System.out.println("SEG_LINETO  "+Arrays.toString(coords));
                    g.setColor(Color.GREEN);
                    drawPoint(g, coords[0], coords[1], 3);
                    break;

                case PathIterator.SEG_CLOSE:
                    System.out.println("SEG_CLOSE   "+Arrays.toString(coords));
                    break;
                    
                case PathIterator.SEG_CUBICTO:
                case PathIterator.SEG_QUADTO:
                default:
                    throw new AssertionError(
                        "Invalid segment in flattened path");
            }
            pi.next();
        }
    }
    
    /**
     * Draw the given list of points to the given graphics 
     * 
     * @param g The graphics
     * @param points The points
     * @param r The radius
     */
    private static void drawPoints(Graphics2D g, List<Point2D> points, double r)
    {
        for (Point2D point : points)
        {
            drawPoint(g, point.getX(), point.getY(), r);
        }
    }

    /**
     * Draw the given point to the given graphics 
     * 
     * @param g The graphics
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param r The radius
     */
    private static void drawPoint(Graphics2D g, double x, double y, double r)
    {
        g.fill(new Ellipse2D.Double(
            x-r, y-r, r+r, r+r));
    }
    

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (draggedPoint != null)
        {
            draggedPoint.setLocation(e.getPoint());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        draggedPoint = null;
        double thresholdSquared = 10*10;
        double minDs = Double.MAX_VALUE;
        for (Point2D point : points)
        {
            double ds = point.distanceSq(e.getPoint());
            if (ds < thresholdSquared && ds < minDs)
            {
                minDs = ds;
                draggedPoint = point;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        draggedPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // Nothing to do here
    }
}
