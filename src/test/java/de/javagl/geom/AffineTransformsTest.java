package de.javagl.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Regression/integration test for the {@link AffineTransforms} class
 */
@SuppressWarnings({"javadoc"})
public class AffineTransformsTest
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
        f.getContentPane().add(new AffineTransformsTestPanel());
        f.setSize(500,500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class AffineTransformsTestPanel extends JPanel
{
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

        Rectangle2D r = new Rectangle2D.Double(10, 20, 30, 40);
        g.setColor(Color.GREEN);
        g.fill(r);
        
        AffineTransform at0 = new AffineTransform();
        at0.concatenate(AffineTransform.getScaleInstance(1.0, -1.0));

        g.translate(100, 100);
        g.setColor(new Color(0,255,0,32));
        g.fill(at0.createTransformedShape(r));
        
        g.setColor(new Color(255,0,0,32));
        g.fill(AffineTransforms.createTransformedShape(at0, r));
        
    }
}
