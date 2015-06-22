package de.javagl.geom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Integration test for the {@link Shapes#interpolate(Shape, Shape, double)}
 * method
 */
@SuppressWarnings({"javadoc"})
public class ShapesInterpolationTest
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
        f.getContentPane().add(new ShapesInterpolationTestPanel());
        f.setSize(900,900);
        f.setVisible(true);
    }
    
}

@SuppressWarnings({"javadoc", "serial"})
class ShapesInterpolationTestPanel extends JPanel
{
    private Shape outline0;
    private Shape outline1;
    
    public ShapesInterpolationTestPanel()
    {
        final FontRenderContext fontRenderContext = 
            new FontRenderContext(null, false, true);
        final Font font = new Font("Serif", Font.BOLD, 60);
        GlyphVector glyphVector = 
            font.createGlyphVector(fontRenderContext, "Test");
        Shape outline = glyphVector.getOutline(0,0);
        
        AffineTransform at = new AffineTransform();
        outline0 = at.createTransformedShape(outline);
        at.translate(400,100);
        at.scale(2,2);
        at.rotate(1.9);
        outline1 = at.createTransformedShape(outline);
    }
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,  
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(100,100);
        g.setColor(Color.BLACK);
        g.fill(outline0);
        
        g.setColor(Color.GRAY);
        int n = 25;
        for (int i=0; i<n; i++)
        {
            double alpha = i/(double)(n-1);
            int c = 255-(int)(255*alpha);
            g.setColor(new Color(c,c,c));
            Shape s = Shapes.interpolate(outline0, outline1, alpha);
            g.draw(s);
        }
        
        g.fill(outline1);
    }
    
    
}

