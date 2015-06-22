package de.javagl.geom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link Arrows} class
 */
@SuppressWarnings({"javadoc"})
public class ArrowsTest
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
        f.getContentPane().add(new ArrowsTestPanel());
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class ArrowsTestPanel extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private final List<Point2D> points;
    private Point2D draggedPoint = null;

    ArrowsTestPanel()
    {
        points = new ArrayList<Point2D>();
        points.add(new Point2D.Double(50,50));
        points.add(new Point2D.Double(250,50));
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

        g.setColor(Color.BLACK);
        boolean drawPoints = false;
        drawPoints = true;
        if (drawPoints)
        {
            drawPoints(g, points);
        }
        
        g.setColor(Color.BLUE);
        
        Point2D p0 = points.get(0);
        Point2D p1 = points.get(1);
        double x0 = p0.getX();
        double y0 = p0.getY();
        double x1 = p1.getX();
        double y1 = p1.getY();
        
        //g.setStroke(new BasicStroke(8.0f));
        //g.draw(new Line2D.Double(p0,p1));
        g.setStroke(new BasicStroke(1.0f));
        ArrowCreator arrowCreator = Arrows.create();
        //arrowCreator.setAbsoluteHeadWidth(40);
        //arrowCreator.setRelativeHeadLength(0.1, 3, 20);
        //g.draw(arrowCreator.setAbsoluteHeadWidth(48).buildShape(x0, y0, x1, y1, 24.0));
        
        //g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(arrowCreator.buildShape(x0, y0, x1, y1, 3));
        //g.draw(arrowCreator.buildLines(x0, y0, x1, y1));

    }
    
    /**
     * Draw the given list of points to the given graphics 
     * 
     * @param g The graphics
     * @param points The points
     */
    private static void drawPoints(Graphics2D g, List<Point2D> points)
    {
        double r = 3;
        for (Point2D point : points)
        {
            double x = point.getX();
            double y = point.getY();
            g.fill(new Ellipse2D.Double(
                x-r, y-r, r+r, r+r));
        }
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
