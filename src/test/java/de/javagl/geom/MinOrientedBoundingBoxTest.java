package de.javagl.geom;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link OrientedBoundingBoxes} class
 */
@SuppressWarnings({"javadoc"})
public class MinOrientedBoundingBoxTest
{
    public static void main(String[] args) throws IOException
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
        f.getContentPane().add(new MinOrientedBoundingBoxTestPanel());
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class MinOrientedBoundingBoxTestPanel extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private final List<Point2D> points;
    private Point2D draggedPoint = null;

    MinOrientedBoundingBoxTestPanel()
    {
        points = new ArrayList<Point2D>();

        Random r = new Random(0);
        for (int i=0; i<8; i++)
        {
            double x = 200 + r.nextDouble() * 200;
            double y = 200 + r.nextDouble() * 200;
            points.add(new Point2D.Double(x,y));
        }

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,  
            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.BLACK);
        drawPoints(g, points);

        List<Point2D> minObbCorners = 
            OrientedBoundingBoxes.computeMinimumOrientedBoundingBoxCorners(
                points);

        Path2D p = createPath(minObbCorners);
        g.setColor(Color.BLUE);
        g.draw(p);
    }

    static Path2D createPath(List<Point2D> points)
    {
        Path2D path = new Path2D.Double();
        for (int i=0; i<points.size(); i++)
        {
            Point2D p = points.get(i);
            double x = p.getX();
            double y = p.getY();
            if (i == 0)
            {
                path.moveTo(x, y);
            }
            else
            {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }
    
    static void drawPoints(Graphics2D g, List<Point2D> points)
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


