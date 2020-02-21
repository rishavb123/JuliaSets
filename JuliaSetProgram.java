import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class JuliaSetProgram extends JPanel {

    private static final long serialVersionUID = -4665511166239098274L;

    private int width = 1000;
    private int height = 580;
    private final int range = 5;
    private final int radius = 6;
    private final int movingRes = 10;
    private final List<String> paramsList = Arrays.asList("a", "b", "n", "zoom", "real_offset", "imaginary_offset", "max_iterations", "hue", "saturation", "brightness");
    private final Map<String, String> paramInfo = new HashMap<>();

    private int precision = 1000;

    private final ComplexFunction f = new ComplexFunction() {

        @Override
        public ComplexNumber func(ComplexNumber num) {
            // return num.pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));//.add(new ComplexNumber(0, 2 * num.getImaginary() * num.getReal()));
            
            // Random Function to show it works for anything
            // num.setReal(getParam("a") * Math.sin(num.getReal()));
            // num.setImaginary(getParam("b") * Math.cos(num.getImaginary()));
            // // num.multiply(new ComplexNumber(getParam("c"), getParam("d")));
            // num.pow((int) Math.round(getParam("n")));
            // return num;

            // NEWTON'S METHOD FUNCTIONS
            // int n = (int) getParam("n");
            // return new ComplexNumber(1, 0).add(num.pow(n).scale(n - 1)).divide(num.pow(n - 1).scale(n));
            // return num.clone().scale(2).subtract(new ComplexNumber(getParam("a"), getParam("b"))).add(num.pow(n).scale(n - 1)).divide(new ComplexNumber(1, 0).add(num.pow(n - 1).scale(n)));
            
            // OTHER FUNCTIONS
            // return num.exp().pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));
            // return num.sin().pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));
            // return num.cos().pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));
            // return num.tan().pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b")));
            return num.pow((int)Math.round(getParam("n"))).add(new ComplexNumber(getParam("a"), getParam("b"))).reciprocal();

        }

    };

    private int res = 1;

    // private final ComplexFunction derivative = f.derivative();
    // private final ComplexFunction newtonsMethod = f.newtonsMethod();

    private final int numOfParams = paramsList.size();

    private double[] params;
    private JScrollBar[] paramScrollBars;

    private JButton reset;
    private JButton save;

    private JFrame frame;

    private JPanel sliderPanel;
    private JPanel buttonPanel;
    private JPanel labelsPanel;
    private JPanel textFieldsPanel;

    private JPanel leftPanel;
    private JPanel panelsPanel;

    public JuliaSetProgram() {
        frame = new JFrame("Julia Set Program");
        frame.setSize(width, height + 24 * numOfParams);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        paramInfo.put("zoom", "1 0 100 100");
        paramInfo.put("n", "2 -10 10 1");
        paramInfo.put("max_iterations", "100 1 500 1");
        paramInfo.put("hue", "0 0 1 1000");
        paramInfo.put("saturation", "1 0 1 1000");
        paramInfo.put("brightness", "1 0 1 1000");

        params = new double[numOfParams];
        paramScrollBars = new JScrollBar[numOfParams];

        sliderPanel = new JPanel(new GridLayout(numOfParams, 1));
        labelsPanel = new JPanel(new GridLayout(numOfParams, 1));
        textFieldsPanel = new JPanel(new GridLayout(numOfParams, 1));

        final JPanel root = this;

        for (int i = 0; i < numOfParams; i++) {
            JLabel label = new JLabel("    " + paramsList.get(i) + " :     ");
            JTextField textField = new JTextField("0.0", 10);
            if (paramInfo.keySet().contains(paramsList.get(i))) {

                String[] arr = paramInfo.get(paramsList.get(i)).split(" ");
                int defaultValue = Integer.parseInt(arr[0]);
                int start = Integer.parseInt(arr[1]);
                int end = Integer.parseInt(arr[2]);
                int new_precision = Integer.parseInt(arr[3]);

                params[i] = defaultValue;
                textField.setText(defaultValue + "");

                final int j = i;
                paramScrollBars[i] = new JScrollBar(JScrollBar.HORIZONTAL, defaultValue * new_precision, 0,
                        new_precision * start, new_precision * end);
                paramScrollBars[i].addAdjustmentListener(new AdjustmentListener() {

                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {

                        params[j] = (double) e.getValue() / new_precision;
                        textField.setText(params[j] + "");
                        repaint();
                    }

                });

                textField.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            double value = Double.parseDouble(e.getActionCommand());
                            value = (int) (value * new_precision) / (double) new_precision;
                            if(value < start) value = start;
                            else if(value > end) value = end;
                            params[j] = value;
                            textField.setText(value + "");
                            paramScrollBars[j].setValue((int) (value * new_precision));
                        } catch(NumberFormatException exception) {
                            JOptionPane.showMessageDialog(root, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
                            textField.setText("" + params[j]);
                        }
                    }
                    
                });

            } else {
                final int j = i;
                paramScrollBars[i] = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -precision * range, precision * range);
                paramScrollBars[i].addAdjustmentListener(new AdjustmentListener() {

                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        params[j] = (double) e.getValue() / precision;
                        textField.setText(params[j] + "");
                        repaint();
                    }

                });
                textField.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            double value = Double.parseDouble(e.getActionCommand());
                            value = (int) (value * precision) / (double) precision;
                            if(value < -range) value = -range;
                            else if(value > range) value = range;
                            params[j] = value;
                            textField.setText(value + "");
                            paramScrollBars[j].setValue((int) (value * precision));
                        } catch(NumberFormatException exception) {
                            JOptionPane.showMessageDialog(root, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
                            textField.setText("" + params[j]);
                        }
                    }
                    
                });
            }

            labelsPanel.add(label);
            textFieldsPanel.add(textField);
            sliderPanel.add(paramScrollBars[i]);
        }

        buttonPanel = new JPanel(new GridLayout(1, 2));

        reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < numOfParams; i++) {
                    if (paramInfo.keySet().contains(paramsList.get(i))) {
                        String[] arr = paramInfo.get(paramsList.get(i)).split(" ");
                        int defaultValue = Integer.parseInt(arr[0]);
                        int new_precision = Integer.parseInt(arr[3]);
                        paramScrollBars[i].setValue(defaultValue * new_precision);
                    } else {
                        paramScrollBars[i].setValue(0);
                    }
                }
            }

        });
        buttonPanel.add(reset);
        save = new JButton("save");
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageIO.write(juliaSetCalculations(), "png", new File("image_saves\\saved-" + new Date().getTime() + ".png"));//Saved Image at " + new Date() + ".png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
        });

        buttonPanel.add(save);
        
        leftPanel = new JPanel(new GridLayout(1, 2));
        leftPanel.add(labelsPanel);
        leftPanel.add(textFieldsPanel);

        panelsPanel = new JPanel(new BorderLayout());
        panelsPanel.add(leftPanel, BorderLayout.WEST);
        panelsPanel.add(sliderPanel, BorderLayout.CENTER);
        frame.add(panelsPanel, BorderLayout.SOUTH);


        frame.add(buttonPanel, BorderLayout.NORTH);
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
        return frame.getHeight() - 24 * numOfParams;
    }

    public BufferedImage juliaSetCalculations() {

        BufferedImage image = new BufferedImage(getUsableWidth(), getUsableHeight(), BufferedImage.TYPE_INT_RGB);

        int maxIterations = (int) getParam("max_iterations");


        boolean moving = false;
        for(JScrollBar scrollBar: paramScrollBars) {
            if(scrollBar.getValueIsAdjusting()) moving = true;
        }
        if(moving) res = movingRes;
        if(res != 1) {
            for(int x = res / 2; x <= getUsableWidth() - res / 2; x += res)
                for(int y = res / 2; y <= getUsableHeight() - res / 2; y += res) {
                    ComplexNumber z = new ComplexNumber((double) getUsableWidth() / getUsableHeight() * (2.0 * x - getUsableWidth()) / getUsableWidth() + getParam("real_offset"), 
                                                                                (2.0 * y - getUsableHeight()) / getUsableHeight()  + getParam("imaginary_offset")).scale(1/getParam("zoom"));
                    float iteration = maxIterations;
                    while(z.squaredMagnitude() < radius && iteration > 0) {
                        z = f.call(z);
                        iteration--;
                    }
                    int c = iteration > 0? Color.HSBtoRGB((float) (getParam("hue") + (1 - getParam("hue"))*((maxIterations / iteration) % 1)), (float) getParam("saturation"), (float) getParam("brightness")): Color.HSBtoRGB(maxIterations / iteration, (float) getParam("saturation"), 0);
                    for(int i = -res / 2; i < res / 2; i++)
                        for(int j = -res / 2; j < res / 2; j++) {
                            image.setRGB(x + i, y + j, c);
                        }

                }
                res -= 2;
                if(res < 1) res = 1;
                if(!moving)
                    repaint();
        } else {
            for(int x = 0; x < getUsableWidth(); x++)
                for(int y = 0; y < getUsableHeight(); y++) {
                    ComplexNumber z = new ComplexNumber((double) getUsableWidth() / getUsableHeight() * (2.0 * x - getUsableWidth()) / getUsableWidth() + getParam("real_offset"), 
                                                                                (2.0 * y - getUsableHeight()) / getUsableHeight()  + getParam("imaginary_offset")).scale(1/getParam("zoom"));
                    float iteration = maxIterations;
                    while(z.squaredMagnitude() < radius && iteration > 0) {
                        z = f.call(z);
                        iteration--;
                    } 
                    int c = iteration > 0? Color.HSBtoRGB((float) (getParam("hue") + (1 - getParam("hue"))*((maxIterations / iteration) % 1)), (float) getParam("saturation"), (float) getParam("brightness")): Color.HSBtoRGB(maxIterations / iteration, (float) getParam("saturation"), 0);
                    image.setRGB(x, y, c);

                }
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