import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class JuliaSetProgram extends JPanel {

    private static final long serialVersionUID = -4665511166239098274L;

    private JFrame frame;

    private int width = 800;
    private int height = 700;

    private double zoom = 1;

    private double a = 0;
    private double b = 0;

    private JScrollBar aBar;
    private JScrollBar bBar;
    private JScrollBar zoomBar;

    private JPanel sliderPanel;

    private int precision = 1000;

    private ComplexFunction f;

    private final float maxIterations = 100;

    public JuliaSetProgram() {
        frame = new JFrame("Julia Set Program");
        frame.setSize(width, (int)(height*1.1));
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -precision * 6, precision * 6);
        aBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                a = (double) e.getValue() / precision;
                repaint();
            }
            
        }); 
        bBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -precision * 6, precision * 6);
        bBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                b = (double) e.getValue() / precision;
                repaint();
            }
            
        });
        zoomBar = new JScrollBar(JScrollBar.HORIZONTAL, 1, 0, 1, 1000);
        zoomBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                zoom = e.getValue();
                repaint();
            }
            
        }); 

        sliderPanel = new JPanel(new GridLayout(3, 1));
        sliderPanel.add(aBar);
        sliderPanel.add(bBar);
        sliderPanel.add(zoomBar);

        frame.add(sliderPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        f = new ComplexFunction() {
        
            @Override
            public ComplexNumber func(ComplexNumber num) {
                // return num.pow(4).add(new ComplexNumber(a, b));
                num.setReal(a * Math.sin(num.getReal()));
                num.setImaginary(b * Math.sin(num.getImaginary()));
                return num;
            }

        };

    }

    public BufferedImage juliaSetCalculations() {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                ComplexNumber z = new ComplexNumber((double) width / height * (2.0 * x - width) / width, 
                                                                              (2.0 * y - height) / height).scale(1/zoom);
                float iteration = maxIterations;
                while(z.squaredMagnitude() < 6 && iteration > 0) {
                    z = f.call(z);
                    iteration--;
                }
                int c = iteration > 0? Color.HSBtoRGB((maxIterations / iteration) % 1, 1, 1): Color.HSBtoRGB(maxIterations / iteration, 1, 0);
                image.setRGB(x, y, c);

            }
        return image;
    }

    public BufferedImage juliaSetCalculations2() {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                double zx = (double) width / height * (2 * x - width) / (zoom * width); // scale * (x - width / 2) / (1/2 * zoom * width)
                double zy = (2 * y - height) / (zoom * height); // (y - height / 2) / (1/2 * zoom * height)
                float maxIter = 300;
                float iteration = maxIter;
                while(zx * zx + zy * zy < 6 && iteration > 0) {
                    double temp = zx * zx - zy * zy + a;
                    zy = 2 * zx * zy + b;
                    zx = temp;
                    iteration--;
                }
                int c = iteration > 0? Color.HSBtoRGB((maxIter / iteration) % 1, 1, 1): Color.HSBtoRGB(maxIter / iteration, 1, 0);
                image.setRGB(x, y, c);
            }
        return image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(juliaSetCalculations(), 0, 0, null);
    }

    public static void main(String[] args) {
        JuliaSetProgram app = new JuliaSetProgram();
    }

}