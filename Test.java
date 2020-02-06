import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Test extends JPanel {

    private static final long serialVersionUID = -4344136156882953517L;

    JFrame frame;
    JScrollBar redBar;
    JScrollBar blueBar;
    JScrollBar greenBar;

    JPanel scrollPanel;
    JCheckBox[] boxes;
    JPanel boxPanel;

    JPanel panelPanel;

    int redValue = 0;
    int blueValue = 200;
    int greenValue = 0;

    public Test() {
        frame = new JFrame("Julia Set Program");
        frame.setSize(1200, 800);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        redBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 100, 0, 255);
        greenBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
        blueBar = new JScrollBar(JScrollBar.HORIZONTAL, 200, 0, 200, 255);

        redBar.setUnitIncrement(50);

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(3, 1));

        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(3, 2));

        panelPanel = new JPanel();
        panelPanel.setLayout(new GridLayout(1, 2));

        AdjustmentListener adjustmentListener = new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if(e.getSource() == redBar)
                    redValue = e.getValue();
                if(e.getSource() == greenBar)
                    greenValue = e.getValue();
                if(e.getSource() == blueBar)
                    blueValue = e.getValue();
                repaint();
            }
            
        };

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(boxes[0].isSelected())
                    redValue = 0;
                else
                    redValue = redBar.getValue();
                repaint();
            }

        };

        redBar.addAdjustmentListener(adjustmentListener);
        greenBar.addAdjustmentListener(adjustmentListener);
        blueBar.addAdjustmentListener(adjustmentListener);
        scrollPanel.add(redBar);
        scrollPanel.add(greenBar);
        scrollPanel.add(blueBar);

        boxes = new JCheckBox[6];
        for(int i = 0; i < boxes.length; i++) {
            boxes[i] = new JCheckBox();
            boxes[i].addActionListener(actionListener);
            boxPanel.add(boxes[i]);
        }

        panelPanel.setLayout(new BorderLayout());
        panelPanel.add(scrollPanel, BorderLayout.CENTER);
        panelPanel.add(boxPanel, BorderLayout.EAST);
        frame.add(panelPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(redValue, blueValue, greenValue));
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    public static void main(String[] args) {
        Test app = new Test();
    }

}