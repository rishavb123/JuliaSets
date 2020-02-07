import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Arrays;
import java.util.List;

public class Temp {

    public int width, height;

    public double getParam(String name) {
        return 0;
    }

    public BufferedImage juliaSetCalculations2() {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                double zx = (double) width / height * (2 * x - width) / (getParam("zoom") * width); // scale * (x - width / 2) / (1/2 * zoom * width)
                double zy = (2 * y - height) / (getParam("zoom") * height); // (y - height / 2) / (1/2 * zoom * height)
                float maxIter = 300;
                float iteration = maxIter;
                while(zx * zx + zy * zy < 6 && iteration > 0) {
                    double temp = zx * zx - zy * zy + getParam("a");
                    zy = 2 * zx * zy + getParam("b");
                    zx = temp;
                    iteration--;
                }
                int c = iteration > 0? Color.HSBtoRGB((maxIter / iteration) % 1, 1, 1): Color.HSBtoRGB(maxIter / iteration, 1, 0);
                image.setRGB(x, y, c);
            }
        return image;
    }
}