import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Arrays;
import java.util.List;

public class JuliaSetProgram extends JPanel {

    private static final long serialVersionUID = -4665511166239098274L;

    private int width = 1000;
    private int height = 600;
    private final float maxIterations = 100;
    private final int range = 6;
    private final List<String> paramsList = Arrays.asList("a", "b", "n", "zoom", "real_offset", "imaginary_offset");
    private int precision = 1000;

    private final ComplexFunction f = new ComplexFunction() {
        
        @Override
        public ComplexNumber func(ComplexNumber num) {
            return num.pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));

            // num.setReal(getParam("a") * Math.sin(num.getReal()));
            // num.setImaginary(getParam("b") * Math.cos(num.getImaginary()));
            // num.multiply(new ComplexNumber(getParam("c"), getParam("d")));
            // num.pow((int)Math.round(getParam("n")));
            // return num;
        }

    };

    private final int numOfParams = paramsList.size();

    private double[] params;
    private JScrollBar[] paramScrollBars;

    private JButton reset;

    private JFrame frame;

    private JPanel sliderPanel;

    public JuliaSetProgram() {
        frame = new JFrame("Julia Set Program");
        frame.setSize(width, height + 22 * numOfParams + 35);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        params = new double[numOfParams];
        paramScrollBars = new JScrollBar[numOfParams];

        sliderPanel = new JPanel(new GridLayout(numOfParams, 2));

        for(int i = 0; i < numOfParams; i++)
        {
            JLabel label = new JLabel("    " + paramsList.get(i) + " :     0");
            if(paramsList.get(i).equals("zoom")) {
                params[i] = 1;
                label.setText("    " + paramsList.get(i) + " :     1");
            
                final int j = i;
                paramScrollBars[i] = new JScrollBar(JScrollBar.HORIZONTAL, 1, 0, 1, 100);
                paramScrollBars[i].addAdjustmentListener(new AdjustmentListener() {
                    
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        params[j] = (double) e.getValue();
                        label.setText("    " + paramsList.get(j) + " :     " + params[j]);
                        repaint();
                    }
                    
                }); 
            } else {
                final int j = i;
                paramScrollBars[i] = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -precision * range, precision * range);
                paramScrollBars[i].addAdjustmentListener(new AdjustmentListener() {
                    
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        params[j] = (double) e.getValue() / precision;
                        label.setText("    " + paramsList.get(j) + " :     " + params[j]);
                        repaint();
                    }
                    
                }); 
            }
            sliderPanel.add(label);
            sliderPanel.add(paramScrollBars[i]);
        }

        reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < numOfParams; i++) {
                    if(paramsList.get(i).equals("zoom")) {
                        paramScrollBars[i].setValue(1);
                    } else {
                        paramScrollBars[i].setValue(0);
                    }
                }
            }
            
        });

        frame.add(sliderPanel, BorderLayout.SOUTH);
        frame.add(reset, BorderLayout.NORTH);
        frame.setVisible(true);

    }

    public double getParam(String name) {
        int index = paramsList.indexOf(name);
        return index < 0? 0 : params[index];
    }

    public int getUsableWidth() {
        return frame.getWidth();
    }

    public int getUsableHeight() {
        return frame.getHeight() - 22 * numOfParams - 35;
    }

    public BufferedImage juliaSetCalculations() {

        BufferedImage image = new BufferedImage(getUsableWidth(), getUsableHeight(), BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < getUsableWidth(); x++)
            for(int y = 0; y < getUsableHeight(); y++) {
                ComplexNumber z = new ComplexNumber((double) getUsableWidth() / getUsableHeight() * (2.0 * x - getUsableWidth()) / getUsableWidth() + getParam("real_offset"), 
                                                                              (2.0 * y - getUsableHeight()) / getUsableHeight()  + getParam("imaginary_offset")).scale(1/getParam("zoom"));
                float iteration = maxIterations;
                while(z.squaredMagnitude() < range && iteration > 0) {
                    z = f.call(z);
                    iteration--;
                }
                int c = iteration > 0? Color.HSBtoRGB((maxIterations / iteration) % 1, 1, 1): Color.HSBtoRGB(maxIterations / iteration, 1, 0);
                image.setRGB(x, y, c);

            }
        return image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(juliaSetCalculations(), 0, 0, null);
    }

    public static void main(String[] args) {
        new JuliaSetProgram();
    }

}