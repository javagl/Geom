package de.javagl.geom;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;


/**
 * Integration test for the {@link Stars} class
 */
@SuppressWarnings({"javadoc"})
public class StarsTest
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
        f.getContentPane().add(new StarsTestPanel());
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

@SuppressWarnings({"javadoc", "serial"})
class StarsTestPanel extends JPanel
{
    private int numRays;
    private double innerRadius;
    private double outerRadius;
    private double startAngleRad;
    private double innerRoundness;
    private double outerRoundness;
    
    public StarsTestPanel()
    {
        super(new FlowLayout());
        
        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JLabel("Number of rays:"));
        JSlider numRaysSlider = new JSlider(1, 20, 1);
        p.add(numRaysSlider);
        
        p.add(new JLabel("Inner radius:"));
        JSlider innerRadiusSlider = new JSlider(0, 100, 0);
        p.add(innerRadiusSlider);

        p.add(new JLabel("Outer radius:"));
        JSlider outerRadiusSlider = new JSlider(0, 100, 0);
        p.add(outerRadiusSlider);
        
        p.add(new JLabel("Start angle:"));
        JSlider startAngleSlider = new JSlider(0, 100, 0);
        p.add(startAngleSlider);
        
        p.add(new JLabel("Inner roundness:"));
        JSlider innerRoundnessSlider = new JSlider(0, 100, 0);
        p.add(innerRoundnessSlider);
        
        p.add(new JLabel("Outer roundness:"));
        JSlider outerRoundnessSlider = new JSlider(0, 100, 0);
        p.add(outerRoundnessSlider);
        
        add(p);
        
        ChangeListener changeListener = e ->
        {
            numRays = numRaysSlider.getValue();
            innerRadius = (innerRadiusSlider.getValue() / 100.0) * 300.0;
            outerRadius = (outerRadiusSlider.getValue() / 100.0) * 300.0;
            startAngleRad = (startAngleSlider.getValue() / 100.0) * Math.PI * 2;
            innerRoundness = (innerRoundnessSlider.getValue() / 100.0);
            outerRoundness = (outerRoundnessSlider.getValue() / 100.0);
            repaint();
        };
        numRaysSlider.addChangeListener(changeListener);
        innerRadiusSlider.addChangeListener(changeListener);
        outerRadiusSlider.addChangeListener(changeListener);
        startAngleSlider.addChangeListener(changeListener);
        innerRoundnessSlider.addChangeListener(changeListener);
        outerRoundnessSlider.addChangeListener(changeListener);
        
        numRaysSlider.setValue(5);
        innerRadiusSlider.setValue(25);
        outerRadiusSlider.setValue(75);
        
    }
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.BLACK);
        g.draw(Stars.createStarShape(
            400.0, 400.0, innerRadius, outerRadius, numRays, 
            startAngleRad, innerRoundness, outerRoundness));
    }
}
