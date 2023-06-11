package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
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

    public int[] calculateHistogram(int color) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogram = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color rgb =  new Color(image.getRGB(x, y));
                int value = 0;
                switch (color){
                    case 0:
                        value = rgb.getRed();
                        break;
                    case 1:
                        value= rgb.getGreen();
                        break;
                    case 2:
                        value =rgb.getBlue();
                        break;
                }
                histogram[value]++;
            }
        }
        return histogram;
    }

    private int[] calculateCumulativeHistogram(int[] histogram, int totalPixels){
        int[] cumulativeHistogram = new int[256];
        cumulativeHistogram[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
        }

        return cumulativeHistogram;
    }

    public void equalizeHistogram(){
        int totalPixels = height * width;
        int[] histogramRed = calculateHistogram(0);
        int[] histogramGreen = calculateHistogram(1);
        int[] histogramBlue = calculateHistogram(2);

        int[] cumulativeHistogramRed = calculateCumulativeHistogram(histogramRed, totalPixels);
        int[] cumulativeHistogramGreen = calculateCumulativeHistogram(histogramGreen, totalPixels);
        int[] cumulativeHistogramBlue = calculateCumulativeHistogram(histogramBlue, totalPixels);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color color = new Color(image.getRGB(x,y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int equalizeRed = cumulativeHistogramRed[red] * 255 / totalPixels;
                int equalizeGreen = cumulativeHistogramGreen[green] * 255 / totalPixels;
                int equalizeBlue = cumulativeHistogramBlue[blue] * 255 / totalPixels;

                Color newColor = new Color(equalizeRed, equalizeGreen, equalizeBlue);
                image.setRGB(x,y, newColor.getRGB());
            }
        }


    }


    public void convertToXYZColorSpace() {
        ColorSpace xyzColorSpace = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
        ColorConvertOp convertOp = new ColorConvertOp(xyzColorSpace, null);
        image = convertOp.filter(image, null);
    }

    public void convertToRGBColorSpace() {
        ColorSpace rgbColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorConvertOp convertOp = new ColorConvertOp(rgbColorSpace, null);
        image = convertOp.filter(image, null);
    }
}




