package de.javagl.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link ConvexHull} class
 */
@SuppressWarnings({"javadoc"})
public class ConvexHullTest
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

        f.getContentPane().add(new ConvexHullTestPanel());
        f.setSize(500, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class ConvexHullTestPanel extends JPanel implements MouseListener
{
    private List<Point2D> points = new ArrayList<Point2D>();

    ConvexHullTestPanel()
    {
        addMouseListener(this);

        points.add(new Point2D.Double(100, 100));
        points.add(new Point2D.Double(200, 100));
        points.add(new Point2D.Double(300, 100));
        points.add(new Point2D.Double(300, 150));
        points.add(new Point2D.Double(300, 160));
        points.add(new Point2D.Double(300, 200));
        points.add(new Point2D.Double(100, 200));

    }

    @Override
    protected void paintComponent(Graphics gr)
    {

        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;

        g.setColor(Color.BLACK);
        for (Point2D p : points)
        {
            g.fill(new Ellipse2D.Double(p.getX() - 3, p.getY() - 3, 6, 6));
        }
        Shape s = Paths.fromPoints(ConvexHull.compute(points), true);
        g.setColor(Color.RED);
        g.draw(s);

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            points.add(e.getPoint());
        }
        else
        {
            points.clear();
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // Nothing to do here
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // Nothing to do here
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
