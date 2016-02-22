package de.javagl.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Integration test for the {@link CatmullRomSpline} class
 */
@SuppressWarnings({"javadoc"})
public class CatmullRomSplineTest
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
        f.getContentPane().add(new CatmullRomSplineTestPanel());
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}

@SuppressWarnings({"javadoc", "serial"})
class CatmullRomSplineTestPanel extends JPanel 
    implements MouseListener, MouseMotionListener
{
    private final List<Point2D> pointList;
    private CatmullRomSpline spline;
    private Point2D draggedPoint;
    private int draggedPointIndex;
    private JSlider slider;
    
    public CatmullRomSplineTestPanel()
    {
        this.pointList = new ArrayList<Point2D>();
        
        pointList.add(new Point2D.Double(132,532));
        pointList.add(new Point2D.Double(275,258));
        pointList.add(new Point2D.Double(295,267));
        pointList.add(new Point2D.Double(433,567));
        pointList.add(new Point2D.Double(476,635));

        spline = CatmullRomSpline.create(pointList, 20, 0.0);
        
        slider = new JSlider(0, 100, 0);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                double alpha = slider.getValue() / 100.0;
                spline.setInterpolation(alpha);
                repaint();
            }
        });
        add(slider);
        
        final JCheckBox checkBox = new JCheckBox("Closed?");
        checkBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean closed = checkBox.isSelected();
                spline = CatmullRomSpline.create(pointList, 20, 0.0, closed);
                repaint();
            }
        });
        add(checkBox);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        
        g.setColor(Color.RED);
        for (Point2D p : pointList)
        {
            double r = 5;
            g.draw(new Ellipse2D.Double(p.getX()-r, p.getY()-r, r+r, r+r));
        }
        
        g.setColor(Color.BLACK);
        g.draw(Paths.fromPoints(spline.getInterpolatedPoints(), false));
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (draggedPoint != null)
        {
            draggedPoint.setLocation(e.getX(), e.getY());
            spline.updateControlPoint(draggedPointIndex, draggedPoint);
            
            repaint();
            
            System.out.println("Points: ");
            for (Point2D p : pointList)
            {
                System.out.println("    "+p);
            }
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
        final double thresholdSquared = 10 * 10;
        Point2D p = e.getPoint();
        Point2D closestPoint = null;
        double minDistanceSquared = Double.MAX_VALUE;
        for (Point2D point : pointList)
        {
            double dd = point.distanceSq(p);
            if (dd < thresholdSquared && dd < minDistanceSquared)
            {
                minDistanceSquared = dd;
                closestPoint = point;
            }
        }
        draggedPoint = closestPoint;
        draggedPointIndex = pointList.indexOf(closestPoint);
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