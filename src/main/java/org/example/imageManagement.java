package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class imageManagement {
    private BufferedImage image;
    private int height, width;

    public void readImage(String path) throws IOException {
        File imageFile = new File(path);
        image = ImageIO.read(imageFile);
        height = image.getHeight();
        width = image.getWidth();
    }

    public void writeImage(String path) throws IOException {
        File imageFile = new File(path);
        String format = path.substring(path.lastIndexOf(".") + 1);
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

    public void addBrightnessWithThreads(int wartosc) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        int chunk = height / cores;
        Thread threads[] = new Thread[cores];
        for (int i = 0; i < cores; ++i) {
            int threadIndex = i;
            threads[i] = new Thread(() -> {
                int startline = threadIndex * chunk;
                int endline = (threadIndex == cores - 1) ?
                        height :
                        startline + chunk;
                for (int y = startline; y < endline; y++) {
                    for (int x = 0; x < width; x++) {
                        Color kolor = new Color(image.getRGB(x, y));
                        int red = kolor.getRed() + wartosc;
                        int green = kolor.getGreen() + wartosc;
                        int blue = kolor.getBlue() + wartosc;

                        red = Math.max(0, Math.min(255, red));
                        green = Math.max(0, Math.min(255, green));
                        blue = Math.max(0, Math.min(255, blue));

                        Color nowyKolor = new Color(red, green, blue);
                        image.setRGB(x, y, nowyKolor.getRGB());
                    }
                }
            });
        }

        for (int j = 0; j < cores; ++j) {
            threads[j].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public int[] calculateHistogram() {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogram = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color rgb =  new Color(image.getRGB(x, y));
                int r = rgb.getRed();
                histogram[r]++;
            }
        }

        return histogram;
    }
}




