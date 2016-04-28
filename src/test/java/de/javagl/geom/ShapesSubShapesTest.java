package de.javagl.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link Shapes#computeSubShapes(Shape)} method
 */
@SuppressWarnings({"javadoc"})
public class ShapesSubShapesTest
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
        JFrame f = new JFrame("Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new ShapesSubShapesTestPanel());
        f.setSize(900,900);
        f.setVisible(true);
    }
    
}

@SuppressWarnings({"javadoc", "serial"})
class ShapesSubShapesTestPanel extends JPanel
{
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,  
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        Path2D shape = new Path2D.Double();
        shape.moveTo(10, 10);
        shape.lineTo(50, 10);
        shape.lineTo(50, 50);
        shape.lineTo(10, 50);
        shape.closePath();
        shape.moveTo(60, 10);
        shape.lineTo(70, 10);
        shape.moveTo(60, 20);
        shape.lineTo(70, 20);
        shape.moveTo(60, 30);
        shape.quadTo(80, 50, 90, 30);
        
        g.translate(100,100);
        g.setColor(Color.BLACK);
        g.draw(shape);

        List<Shape> subShapes = Shapes.computeSubShapes(shape);
        g.translate(0,100);
        for (Shape subShape : subShapes)
        {
            g.draw(subShape);
            g.translate(subShape.getBounds().width, 0);
        }
    }
    
    
}

