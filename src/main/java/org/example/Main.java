package org.example;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String path = "D:\\Jet Brains\\Java Projects\\threads\\test2.png";
        String path2 = "D:\\Jet Brains\\Java Projects\\threads\\testnew2.png";

        imageManagement managementImage = new imageManagement();
        managementImage.readImage(path);
//        managementImage.increaseBrightness(50);
//        managementImage.addBrightnessWithThreads(50);
//        managementImage.equalizeHistogram();
//        managementImage.convertToXYZColorSpace();
        managementImage.equalizeHistogram();
        managementImage.writeImage(path2);


    }
}