package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class imageManagement {
    private BufferedImage image;

    public void readImage(String path) throws IOException {
        File imageFile = new File(path);
        image = ImageIO.read(imageFile);
    }

    public void writeImage(String path) throws IOException {
        File imageFile = new File(path);
        String format = path.substring(path.lastIndexOf(".") + 1);
        System.out.println(format);
        ImageIO.write(image, format, imageFile);
    }

    public void increaseBrightness(int wartosc) {
        int szerokosc = image.getWidth();
        int wysokosc = image.getHeight();

        for (int y = 0; y < wysokosc; y++) {
            for (int x = 0; x < szerokosc; x++) {
                Color kolor = new Color(image.getRGB(x, y));
                int red = kolor.getRed() + wartosc;
                int green = kolor.getGreen() + wartosc;
                int blue = kolor.getBlue() + wartosc;

                // Sprawdzenie, czy wartości kolorów nie przekraczają zakresu [0, 255]
                red = Math.max(0, Math.min(255, red));
                green = Math.max(0, Math.min(255, green));
                blue = Math.max(0, Math.min(255, blue));

                Color nowyKolor = new Color(red, green, blue);
                image.setRGB(x, y, nowyKolor.getRGB());
            }
        }

        System.out.println("Jasność obrazu zwiększona o " + wartosc);
    }
}
