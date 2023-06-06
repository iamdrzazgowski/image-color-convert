package org.example;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "D:\\Jet Brains\\Java Projects\\threads\\threads\\src\\zdj.jpg";
        imageManagement managementImage = new imageManagement();
        managementImage.readImage(path);
        managementImage.increaseBrightness(50);
        managementImage.writeImage(path);
        System.out.println(path);

    }
}